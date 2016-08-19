package com.seaky.centralconf.manager.test;

import java.util.ArrayList;
import java.util.List;

import com.seaky.centralconf.core.ConfigItem;


public class CommomItem {
	List<ConfigItem> allList;
	
	public CommomItem() {
		allList = new ArrayList<ConfigItem>();
		ConfigItem item = new ConfigItem();
		item.setApp("a1");
		item.setDesc("d1");
		item.setEnv("e1");
		item.setKey("k1");
		item.setValue("v1");
		allList.add(item);
//		return allList;
 }
}
