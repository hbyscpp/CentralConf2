package com.seaky.centralconf.manager.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seaky.centralconf.core.ResourceItem;
import com.seaky.centralconf.manager.api.CentralConfigManager;
import com.seaky.centralconf.manager.util.JqgridDataUtil;
import com.seaky.centralconf.manager.web.vo.Result;

@Controller
@RequestMapping("/centralconf")
public class ResourceItemController {

  @Autowired
  private CentralConfigManager manager;

  public final ObjectMapper mapper = new ObjectMapper();

  @RequestMapping("/toResourceItem")
  public ModelAndView toResourceItem(String rscName) {
    ModelAndView mv = new ModelAndView();
    mv.addObject("rsc", rscName);
    mv.setViewName("centralconf/ajax/jqgrid_rscItem");
    return mv;
  }

  @ResponseBody
  @RequestMapping(value = "/getResourceItem")
  public Result getResourceItem(String rsc) {
    List<ResourceItem> confItems = manager.getResourceItem(rsc);
    Result jsondata = JqgridDataUtil.getJqgridDataByResourceItem(confItems, false);
    return jsondata;
  }


  @ResponseBody
  @RequestMapping(value = "/updResourceItem", method = {RequestMethod.POST})
  public String updResourceItem(String rsc, String key, String value, String desc, String oper,
      String id) {

    if (oper.equals("add") || oper.equals("edit")) {
      ResourceItem rscItem = new ResourceItem();
      rscItem.setRsc(rsc);
      rscItem.setKey(key);
      rscItem.setValue(value);
      rscItem.setDesc(desc);
      // 防止修改key之后，新增一条，而不是修改
      if (!id.equals(key)) {
        manager.deleteRscItem(rsc, id);
      }
      manager.updateRscItem(rscItem);
    } else if (oper.equals("del")) {
      manager.deleteRscItem(rsc, id);
    }

    return "true";
  }


}
