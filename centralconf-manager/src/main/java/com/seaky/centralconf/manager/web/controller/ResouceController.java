package com.seaky.centralconf.manager.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seaky.centralconf.core.ConfigItem;
import com.seaky.centralconf.core.ConfigValue;
import com.seaky.centralconf.core.constant.BaseConstant;
import com.seaky.centralconf.manager.api.CentralConfigManager;
import com.seaky.centralconf.manager.util.JqgridDataUtil;
import com.seaky.centralconf.manager.web.vo.Result;

@Controller
@RequestMapping("/centralconf")
public class ResouceController {

  ObjectMapper mapper = new ObjectMapper();

  @Autowired
  private CentralConfigManager manager;

  @RequestMapping("/toResouce")
  public ModelAndView toResouce() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("centralconf/ajax/jqgrid_rsc");
    return mv;
  }

  @ResponseBody
  @RequestMapping(value = "/getRsc")
  public Result getRsc() {
    List<String> resouces = manager.getAllRsc();
    Result jsondata = JqgridDataUtil.getJqgridData(resouces);
    return jsondata;
  }

  @ResponseBody
  @RequestMapping(value = "/getRscList")
  public List<String> getRscList() {
    List<String> rscList = manager.getAllRsc();
    return rscList;
  }

  @ResponseBody
  @RequestMapping(value = "/updRsc", method = {RequestMethod.POST})
  public String updRsc(String rscName, String oper, String id) {
    if (oper.equals("add")) {
      List<String> names = manager.getAllRsc();
      if (null != names) {
        for (String rsc : names) {
          if (rscName.equals(rsc)) {
            // 已经存在这个环境
            return null;
          }
        }
      }
      manager.createRsc(rscName);
    } else if (oper.equals("del")) {
      manager.deleteRsc(id);
    }
    return null;
  }

  /**
   * @throws IOException 
   * @throws JsonMappingException 
   * @throws JsonParseException 
   * 
  * @Description: app，env下关联的公共资源
  * @param TODO 
  * @return String    
  * @throws
   */
  @ResponseBody
  @RequestMapping(value = "/addRelativeRsc", method = {RequestMethod.POST})
  public String addRelativeRsc(String app, String env, String checks)
      throws JsonParseException, JsonMappingException, IOException {
    JavaType javaType = getCollectionType(ArrayList.class, ConfigValue.class);
    List<ConfigValue> checkList = (List<ConfigValue>) mapper.readValue(checks, javaType);
    for (ConfigValue cv : checkList) {
      String isHave = cv.getDesc();
      String key = BaseConstant.PERFIX_RESOURCE + cv.getValue();
      if (!isHave.equals(BaseConstant.YES)) {
        manager.deleteConfig(app, env, key);
        continue;
      }
      ConfigItem cfItem = new ConfigItem();
      cfItem.setApp(app);
      cfItem.setEnv(env);
      cfItem.setKey(key);
      cfItem.setValue(isHave);
      cfItem.setDesc("公共资源");
      manager.updateConfig(cfItem);
    }
    return "true";
  }


  /**
   * 
  * @Description: 获得app，env下关联的公共资源
  * @param TODO 
  * @return List<ConfigItem>    
  * @throws
   */
  @ResponseBody
  @RequestMapping(value = "/getEnvRscShow", method = {RequestMethod.POST})
  public List<ConfigItem> getEnvRscShow(String app, String env) {
    List<ConfigItem> confItems = manager.getExportItem(app, env);
    List<ConfigItem> rscItems = new ArrayList<ConfigItem>();
    for (ConfigItem ci : confItems) {
      String key = ci.getKey();
      String value = ci.getValue();
      if (null != value && value.equals(BaseConstant.YES)
          && key.startsWith(BaseConstant.PERFIX_RESOURCE)) {
        String newKey = key.substring(key.indexOf(BaseConstant.PERFIX_RESOURCE) + 1);
        ci.setKey(newKey);
        rscItems.add(ci);
      }
    }
    return rscItems;
  }

  private JavaType getCollectionType(Class<ArrayList> collectionClass,
      Class<ConfigValue> elementClasses) {
    return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
  }
}
