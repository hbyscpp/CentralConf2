package com.seaky.centralconf.core.api;

import java.io.IOException;
import java.net.URI;
import java.util.concurrent.TimeoutException;

import mousio.etcd4j.EtcdClient;
import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.requests.EtcdKeyPutRequest;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seaky.centralconf.core.ConfigValue;
import com.seaky.centralconf.core.constant.BaseConstant;
import com.seaky.centralconf.core.exception.CentralConfigException;
import com.seaky.centralconf.core.util.PathUtil;

/**
 * 
 * @Description etcd原始方法 
 * @author wangwei
 * @since TODO 
 * @Date 2016-6-1
 */
public class EtcdConfig {
	
	protected EtcdClient client;
	
	protected String basePath;
	
	protected String baseConfigPath;
	
	protected String baseResourcePath;
	
	protected static ObjectMapper mapper = new ObjectMapper();
	
	public EtcdConfig(String basePath, String urls) {
		if (PathUtil.isContainPathSeparator(basePath))
			throw new CentralConfigException("basePath can not contain / or \\");
		
		String[] urlarray = null;
		if (urls != null)
			urlarray = urls.split(",");
		if (urlarray != null && urlarray.length != 0) {
			URI[] uris = new URI[urlarray.length];
			int i = 0;
			for (String url : urlarray) {
				uris[i] = URI.create(url);
				++i;
			}
			client = new EtcdClient(uris);
		} else {
			client = new EtcdClient();
		}
		this.basePath = basePath;
		
		this.baseConfigPath = basePath + BaseConstant.APP_ADDRESS;
		
		this.baseResourcePath = basePath + BaseConstant.RSC_ADDRESS;
	}
	
	/**
	 * 
	* @Description: 创建一个目录dir
	* @param TODO 
	* @return void    
	* @throws
	 */
	protected void creatDir(String dir){
	      try {
	        EtcdResponsePromise<EtcdKeysResponse> rsp = client.putDir(dir).send();
	        rsp.get();
	      } catch (IOException e) {
	        throw new CentralConfigException(e);
	      } catch (EtcdException e) {
	        throw new CentralConfigException(e);
	      } catch (EtcdAuthenticationException e) {
	        throw new CentralConfigException(e);
	      } catch (TimeoutException e) {
	        throw new CentralConfigException(e);
	      }
	}
	
	protected boolean isDirExist(String dir){
	try {
		EtcdResponsePromise<EtcdKeysResponse> response = client.getDir(dir).send();
		response.get();
		return true;
	} catch (IOException e) {
		throw new CentralConfigException(e);
	} catch (EtcdException e) {
		if (e.errorCode == 100)
			return false;
		throw new CentralConfigException(e);
	} catch (EtcdAuthenticationException e) {
		throw new CentralConfigException(e);
	} catch (TimeoutException e) {
		throw new CentralConfigException(e);
	}

	}
	
	
	protected void deleteDir(String dir){
		try {
			EtcdResponsePromise<EtcdKeysResponse> rsp = client.deleteDir(dir).recursive().send();
			rsp.get();
		} catch (IOException e) {
			throw new CentralConfigException(e);
		} catch (EtcdException e) {
			if (e.errorCode == 100)
				return;
			throw new CentralConfigException(e);
		} catch (EtcdAuthenticationException e) {
			throw new CentralConfigException(e);
		} catch (TimeoutException e) {
			throw new CentralConfigException(e);
		}
	}
	
	protected  EtcdKeysResponse getDir(String dir){
		try {
			EtcdResponsePromise<EtcdKeysResponse> response = client.getDir(dir).sorted().send();
			EtcdKeysResponse allkeys = response.get();
			return allkeys;
		} catch (IOException e) {
			throw new CentralConfigException(e);
		} catch (EtcdException e) {
			if (e.errorCode == 100)
				return null;
			throw new CentralConfigException(e);
		} catch (EtcdAuthenticationException e) {
			throw new CentralConfigException(e);
		} catch (TimeoutException e) {
			throw new CentralConfigException(e);
		}
	}
	
	protected void addOrUpdateKey(String key,String value){
		try {
			EtcdKeyPutRequest req = client.put(key,value);
			EtcdResponsePromise<EtcdKeysResponse> rsp = null;
			rsp = req.send();
			rsp.get();
		} catch (JsonProcessingException e) {
			throw new CentralConfigException(e);
		} catch (IOException e) {
			throw new CentralConfigException(e);
		} catch (EtcdException e) {
			throw new CentralConfigException(e);
		} catch (EtcdAuthenticationException e) {
			throw new CentralConfigException(e);
		} catch (TimeoutException e) {
			throw new CentralConfigException(e);
		}
	}
	

	
}
