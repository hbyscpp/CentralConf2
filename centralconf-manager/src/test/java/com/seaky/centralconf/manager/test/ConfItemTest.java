package com.seaky.centralconf.manager.test;

import java.io.IOException;
import java.util.List;

import org.springframework.boot.test.SpringApplicationConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seaky.centralconf.core.ConfigItem;
import com.seaky.centralconf.manager.App;

@SpringApplicationConfiguration(classes = App.class)
public class ConfItemTest extends CommomItem{
   public static final ObjectMapper mapper = new ObjectMapper();
	
   
	public List<ConfigItem> getAllItem() {
//		super.getAllItem();
		for(ConfigItem item:allList){
			System.out.println(item.getKey());
		}
		return allList;
	 }
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ConfItemTest test = new ConfItemTest();
		test.getAllItem();
//		File file = new File("d://a.json");
//		JavaType javaType = getCollectionType(ArrayList.class, ConfigItem.class); 
//		List<ConfigItem> list = mapper.readValue(file, javaType);
//		for(ConfigItem item:list){
//			System.out.println(item.getKey());
//		}
		
//		List<ConfigItem> list = new ArrayList<ConfigItem>();
//		ConfigItem item = new ConfigItem();
//		item.setApp("a1");
//		item.setDesc("d1");
//		item.setEnv("e1");
//		item.setKey("k1");
//		item.setValue("v1");
//		list.add(item);
//		list.add(item);
//		System.out.println(mapper.writeValueAsString(list));
//		Calendar now = new Calendar();
//		String fName = ""+(now.getMonth()+1)+now.getDate()+now.getMinutes()+now.getSeconds();
//		System.out.println( Calendar.getInstance());
		
//		 Calendar ca = Calendar.getInstance();      
//		 int year = ca.get(Calendar.YEAR);//获取年份     
//		 int month=ca.get(Calendar.MONTH)+1;//获取月份      
//		 int day=ca.get(Calendar.DATE);//获取日     
//		 int minute=ca.get(Calendar.MINUTE);//分       
//		 int hour=ca.get(Calendar.HOUR);//小时       
//		 int second=ca.get(Calendar.SECOND);//秒      
//		 int WeekOfYear = ca.get(Calendar.DAY_OF_WEEK);                 
//		 System.out.println("用Calendar.getInstance().getTime()方式显示时间: " + ca.getTime());      
//		 System.out.println("用Calendar获得日期是：" + year +"年"+ month +"月"+ day + "日");           
//		 System.out.println("用Calendar获得时间是：" + hour +"时"+ minute +"分"+ second +"秒");  
//		 String fName = ""+year+month+day+hour+minute+second;
//		 System.out.println(DateUtil.DateToString(new Date(), DateStyle.YYYY_MM_DD_HH_MM_SS_WW));
    }


//	private static JavaType getCollectionType(Class<ArrayList> collectionClass, Class<ConfigItem> elementClasses) {
//		return mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);   
//    }

}
