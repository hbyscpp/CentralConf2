package com.seaky.centralconf.manager.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.seaky.centralconf.manager.api.CentralConfigManager;
import com.seaky.centralconf.manager.util.JqgridDataUtil;
import com.seaky.centralconf.manager.web.vo.JsonResult;
import com.seaky.centralconf.manager.web.vo.Result;

@Controller
@RequestMapping("/centralconf")
public class EnvController {
  @Autowired
  private CentralConfigManager manager;

  @RequestMapping("/toEnv")
  public ModelAndView toEnv(String app) {
    ModelAndView mv = new ModelAndView();
    mv.addObject("app", app);
    mv.setViewName("centralconf/ajax/jqgrid_env");
    return mv;
  }

  @ResponseBody
  @RequestMapping(value = "/getEnv")
  public Result getEnv(String app) {
    List<String> envs = manager.getAllEnv(app);
    Result jsondata = JqgridDataUtil.getJqgridData(envs);
    return jsondata;
  }


  @ResponseBody
  @RequestMapping(value = "/updEnv", method = {RequestMethod.POST})
  public String updEnv(String app, String envName, String oper, String id) {
    if (oper.equals("add")) {
      List<String> names = manager.getAllEnv(app);// TODO
      if (null != names) {
        for (String appName : names) {
          if (envName.equals(appName)) {
            // 已经存在这个环境
            return null;
          }
        }
      }
      manager.createEvn(app, envName);
    } else if (oper.equals("del")) {
      manager.deleteEnv(app, id);
    }
    return null;
  }

  @ResponseBody
  @RequestMapping(value = "/copyEnv")
  public JsonResult copyEnv(@RequestParam String app, @RequestParam String srcEnv,
      @RequestParam String dstEnv, @RequestParam boolean isOverrie) {
    manager.copyEnv(app, srcEnv, dstEnv, isOverrie);
    return new JsonResult(true, "操作成功", null);
  }


}
