package com.seaky.centralconf.manager.util;

import java.io.IOException;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * <p>Json工具类</p>
 * @author yangheng@cmhk.com 2014-7-18 上午8:57:30
 * @version V1.0   
 * @date 2014-7-18 上午8:57:30
 */
public class JsonUtil {
	
	private static ObjectMapper mapper = null ;
	
	/**
	 * 
	 * @Title: toJsonString 
	 * @Description: TODO
	 * @param @param object
	 * @param @return
	 * @return String
	 * @user zhaoyu@cmhk.com
	 * @throws
	 */
	public static String toJsonString(Object object) {
		String str = "";
		try {
			str = JsonUtil.getInstance().writeValueAsString(object);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return str;
	}
	
	/**
	 * 获取集合转换类型
	 * @Title: getCollectionType 
	 * @Description: TODO
	 * @param @param collectionClass
	 * @param @param classes
	 * @param @return
	 * @return JavaType
	 * @user huanghelin
	 * @throws
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>...classes) {
		return JsonUtil.getInstance().getTypeFactory().constructParametricType(collectionClass, classes);
	}
	
	/**
	 * 
	 * @Title: getInstance 
	 * @Description: TODO
	 * @param @return
	 * @return ObjectMapper
	 * @user yangheng@cmhk.com
	 * @throws
	 */
	public static ObjectMapper getInstance(){
		if(mapper == null){
			mapper = new ObjectMapper();
		}
		return mapper;
	}
}
