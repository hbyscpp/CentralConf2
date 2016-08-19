package com.seaky.centralconf.manager.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seaky.centralconf.core.ConfigItem;
import com.seaky.centralconf.core.ResourceItem;
import com.seaky.centralconf.manager.api.CentralConfigManager;
import com.seaky.centralconf.manager.util.DateStyle;
import com.seaky.centralconf.manager.util.DateUtil;
import com.seaky.centralconf.manager.util.JqgridDataUtil;
import com.seaky.centralconf.manager.web.vo.Result;

@Controller
@RequestMapping("/centralconf")
public class ConfItemController {

  @Autowired
  private CentralConfigManager manager;

  public final ObjectMapper mapper = new ObjectMapper();

  @RequestMapping("/toConfItem")
  public ModelAndView toConfItem(String app, String env) {

    ModelAndView mv = new ModelAndView();
    mv.addObject("app", app);
    mv.addObject("env", env);
    mv.setViewName("centralconf/ajax/jqgrid_confItem");
    return mv;
  }


  @RequestMapping("/toConfItemDetail")
  public ModelAndView toConfItemDetail(String app, String env) {
    ModelAndView mv = new ModelAndView();
    mv.addObject("app", app);
    mv.addObject("env", env);
    mv.setViewName("centralconf/ajax/jqgrid_confItem_detail");
    return mv;
  }

  @ResponseBody
  @RequestMapping(value = "/getConfItem")
  public Result getAppByPage(String app, String env) {
    List<ConfigItem> confItems = manager.getOriAllItem(app, env);
    Result r = JqgridDataUtil.getJqgridDataByConfItem(confItems);
    return r;
  }


  @ResponseBody
  @RequestMapping(value = "/getCommonConfItem")
  public Result getCommonConfItem(String app) {
    List<ConfigItem> confItems = manager.getCommonItem(app);
    Result r = JqgridDataUtil.getJqgridDataByConfItem(confItems);
    return r;
  }

  @ResponseBody
  @RequestMapping(value = "/getRelativeResourceItem")
  public Result getRelativeResourceItem(String app, String env) {
    List<ResourceItem> resourceItems = manager.getRelativeResourceItem(app, env);
    Result jsondata = JqgridDataUtil.getJqgridDataByResourceItem(resourceItems, true);
    return jsondata;
  }

  @ResponseBody
  @RequestMapping(value = "/updConfItem", method = {RequestMethod.POST})
  public String updEnv(String app, String env, String key, String value, String desc, String oper,
      String id) {

    if (oper.equals("add") || oper.equals("edit")) {
      ConfigItem cfItem = new ConfigItem();
      cfItem.setApp(app);
      cfItem.setDesc(desc);
      cfItem.setEnv(env);
      cfItem.setKey(key);
      cfItem.setValue(value);
      // 防止修改key之后，新增一条，而不是修改
      if (!id.equals(key)) {
        manager.deleteConfig(app, env, id);
      }
      manager.updateConfig(cfItem);
    } else if (oper.equals("del")) {
      manager.deleteConfig(app, env, id);
    }

    return "true";
  }

  /**
   * @throws IOException
   * @throws JsonMappingException
   * @throws JsonParseException
   * 
   * @Description: 导入json配置
   * @param TODO
   * @return String
   * @throws
   */
  @ResponseBody
  @RequestMapping(value = "/importConfItem")
  public String importConfItem(@RequestParam(value = "file", required = false) MultipartFile file,
      String app, String env) {
    try {
      InputStream in = file.getInputStream();
      StringBuffer out = new StringBuffer();
      byte[] buf = new byte[4096];
      int len = -1;
      while ((len = in.read(buf)) != -1) {
        out.append(new String(buf, 0, len));
      }
      JavaType javaType = getCollectionType(ArrayList.class, ConfigItem.class);
      List<ConfigItem> list = mapper.readValue(out.toString(), javaType);
      // 为每个配置项增加app和env,并导入配置
      for (ConfigItem item : list) {
        item.setApp(app);
        item.setEnv(env);
        manager.updateConfig(item);
      }
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
    return "true";
  }

  /**
   * 
   * @Description: 导出json配置
   * @param TODO
   * @return String
   * @throws
   */
  @RequestMapping(value = "/exportConfItem")
  public void exportConfItem(String app, String env, HttpServletRequest request,
      HttpServletResponse response) {
    List<ConfigItem> confItems = manager.getExportItem(app, env);
    String content;
    try {
      String path = request.getSession().getServletContext().getRealPath("/");
      content = mapper.writeValueAsString(confItems);
      String fileName =
          path + DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_WW) + "E.json";
      // 1.step 创建文件
      File file = new File(fileName);
      if (!file.exists())
        file.createNewFile();
      FileOutputStream out = new FileOutputStream(file, true);
      out.write(content.getBytes("utf-8"));
      out.close();
      // 2 step 输入文件流给浏览器

      // 设置文件MIME类型
      response.setContentType(request.getSession().getServletContext().getMimeType(fileName));
      // 设置Content-Disposition
      response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
      // 读取目标文件，通过response将目标文件写到客户端
      // 读取文件
      InputStream in = new FileInputStream(fileName);
      OutputStream outRsp = response.getOutputStream();

      // 写文件
      int b;
      while ((b = in.read()) != -1) {
        outRsp.write(b);
      }

      in.close();
      outRsp.close();

    } catch (JsonProcessingException e) {
      e.printStackTrace();
    } catch (IOException e1) {
      e1.printStackTrace();
    }
  }

  private JavaType getCollectionType(Class<ArrayList> collectionClass,
      Class<ConfigItem> elementClasses) {
    return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
  }
}
