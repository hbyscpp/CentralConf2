package com.seaky.centralconf.manager.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.seaky.centralconf.manager.config.AccountConfig;

@Controller
@RequestMapping("/centralconf")
public class LoginController {
  @Autowired
  private AccountConfig accountConfig;
  
  @RequestMapping("/login")
  public ModelAndView login(String name, String password) {
    ModelAndView mv = new ModelAndView();
    if (accountConfig.getName().equals(name) && accountConfig.getPassword().equals(password)) {
      mv.setViewName("centralconf/index");
    } else if (StringUtils.isEmpty(name) && StringUtils.isEmpty(password)) {
      mv.setViewName("centralconf/login");
    } else {
      mv.addObject("msg", "用户名或者密码错误");
      mv.setViewName("centralconf/login");
    }
    return mv;
  }
  
  @RequestMapping("/index")
  public ModelAndView index() {
    ModelAndView mv = new ModelAndView();
   
      mv.setViewName("centralconf/index");
    return mv;
  }
}
