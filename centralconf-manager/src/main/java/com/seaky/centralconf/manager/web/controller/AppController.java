package com.seaky.centralconf.manager.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.seaky.centralconf.manager.api.CentralConfigManager;
import com.seaky.centralconf.manager.util.JqgridDataUtil;
import com.seaky.centralconf.manager.web.vo.Result;

@Controller
@RequestMapping("/centralconf")
public class AppController {
  @Autowired
  private CentralConfigManager manager;

  @ResponseBody
  @RequestMapping(value = "/updApp", method = {RequestMethod.POST})
  public String updApp(String name, String oper, String id) {

    if (oper.equals("add")) {
      List<String> names = manager.getAllApp();
      if (null != names) {
        for (String appName : names) {
          if (name.equals(appName)) {
            // 已经存在这个应用
            return null;
          }
        }
      }
      manager.createApp(name);
    } else if (oper.equals("del")) {
      manager.deleteApp(id);
    }

    return "true";
  }

  @RequestMapping("/toApp")
  public ModelAndView toApp() {
    ModelAndView mv = new ModelAndView();
    mv.setViewName("centralconf/ajax/jqgrid_app");
    return mv;
  }

  @ResponseBody
  @RequestMapping(value = "/getApp")
  public Result getApp() {
    List<String> apps = manager.getAllApp();
    Result r = JqgridDataUtil.getJqgridData(apps);
    return r;
  }
}
