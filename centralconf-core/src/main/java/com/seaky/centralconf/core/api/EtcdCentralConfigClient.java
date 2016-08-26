package com.seaky.centralconf.core.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.seaky.centralconf.core.ConfigItem;
import com.seaky.centralconf.core.ConfigValue;
import com.seaky.centralconf.core.ResourceItem;
import com.seaky.centralconf.core.constant.BaseConstant;
import com.seaky.centralconf.core.exception.CentralConfigException;
import com.seaky.centralconf.core.util.PathUtil;

import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;
import mousio.etcd4j.responses.EtcdKeysResponse.EtcdNode;

public class EtcdCentralConfigClient extends EtcdConfig implements CentralConfigClient {


  public EtcdCentralConfigClient(String basePath, String urls) {
    super(basePath, urls);
  }


  private static Logger logger = LoggerFactory.getLogger(EtcdCentralConfigClient.class);


  /**
   * 
   * @Description: 获得一应用下的所有公共配置 @param TODO @return List<ConfigItem> @throws
   */
  public List<ConfigItem> getCommonItem(String app) {

    return new ArrayList<ConfigItem>(getAllCommonConfigAsMap(app).values());

  }


  /**
   * @throws IOException @throws JsonMappingException @throws JsonParseException
   * 
   * @Description: 获得该应用，环境下关联的公共资源 @param TODO @return List<ConfigItem> @throws
   */
  public List<ResourceItem> getRelativeResourceItem(String app, String env) {
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");

    if (PathUtil.isContainPathSeparator(env))
      throw new CentralConfigException("env can not contain / or \\");
    try {
      EtcdResponsePromise<EtcdKeysResponse> response =
          client.getDir(baseConfigPath + "/" + app + "/" + env).sorted()
              .timeout(5, TimeUnit.SECONDS).send();
      EtcdKeysResponse allkeys = response.get();
      List<EtcdNode> allNodes = allkeys.node.nodes;
      if (allNodes == null)
        return null;

      List<ResourceItem> resourceList = new ArrayList<ResourceItem>();
      for (EtcdNode node : allNodes) {
        String key = PathUtil.lastPath(node.key);
        // 如果是资源，则取出资源内容显示出来
        if (key.startsWith(BaseConstant.PERFIX_RESOURCE)) {
          List<ResourceItem> itemList =
              this.getResourceItem(key.substring(key.indexOf(BaseConstant.PERFIX_RESOURCE) + 1));
          resourceList.addAll(itemList);
        }
      }
      return resourceList;
    } catch (JsonProcessingException e) {
      throw new CentralConfigException(e);
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

  public List<ResourceItem> getResourceItem(String rsc) {
    if (PathUtil.isContainPathSeparator(rsc))
      throw new CentralConfigException("rsc can not contain / or \\");
    String dir = super.baseResourcePath + "/" + rsc;
    EtcdKeysResponse allkeys = super.getDir(dir);
    List<EtcdNode> allNodes = allkeys.node.nodes;
    if (allNodes == null)
      return null;

    List<ResourceItem> resourceList = new ArrayList<ResourceItem>();
    for (EtcdNode node : allNodes) {
      String key = PathUtil.lastPath(node.key);
      ResourceItem item = new ResourceItem();
      item.setRsc(rsc);
      item.setKey(key);
      ConfigValue cv;
      try {
        cv = mapper.readValue(node.value, ConfigValue.class);
        if (cv != null) {
          item.setDesc(cv.getDesc());
          item.setValue(cv.getValue());
        }
      } catch (JsonParseException e) {
        e.printStackTrace();
        throw new CentralConfigException("mapper readValue is not correct");
      } catch (JsonMappingException e) {
        e.printStackTrace();
        throw new CentralConfigException("mapper readValue is not correct");
      } catch (IOException e) {
        e.printStackTrace();
        throw new CentralConfigException("mapper readValue is not correct");
      }

      resourceList.add(item);
    }
    return resourceList;
  }

  /**
   * 
   * @Description: 获得原始的配置项内容 没有公共配置与资源的详细 @param TODO @return List<ConfigItem> @throws
   */
  public List<ConfigItem> getOriAllItem(String app, String env) {

    return new ArrayList<ConfigItem>(getConfigAsMap(app, env).values());

  }

  /**
   * 
   * @Description: 导出的原始数据 @param TODO @return List<ConfigItem> @throws
   */
  public List<ConfigItem> getExportItem(String app, String env) {
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");

    if (PathUtil.isContainPathSeparator(env))
      throw new CentralConfigException("env can not contain / or \\");
    try {
      EtcdResponsePromise<EtcdKeysResponse> response =
          client.getDir(baseConfigPath + "/" + app + "/" + env).sorted().send();
      EtcdKeysResponse allkeys = response.get();
      List<EtcdNode> allNodes = allkeys.node.nodes;
      if (allNodes == null)
        return null;
      List<ConfigItem> allOrigConfigItems = new ArrayList<ConfigItem>();
      for (EtcdNode node : allNodes) {
        String key = PathUtil.lastPath(node.key);
        ConfigItem item = new ConfigItem();
        item.setApp(app);
        item.setEnv(env);
        item.setKey(key);
        ConfigValue cv = mapper.readValue(node.value, ConfigValue.class);
        if (cv != null) {
          item.setDesc(cv.getDesc());
          item.setValue(cv.getValue());
        }
        allOrigConfigItems.add(item);
      }
      return allOrigConfigItems;
    } catch (JsonProcessingException e) {
      throw new CentralConfigException(e);
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

  /**
   * 
   * 
   * @Description: 页面展示的内容，含有公共配置与资源的具体配置 @param TODO @return List<ConfigItem> @throws
   */
  public List<ConfigItem> getAllItem(String app, String env) {



    Map<String, ConfigItem> commonItems = this.getAllCommonConfigAsMap(app);
    // 如果是公共配置环境 则直接取出
    if (env.equals(BaseConstant._COMMON)) {
      return new ArrayList<ConfigItem>(commonItems.values());
    }

    Map<String, ConfigItem> resItems = this.getAllResourceConfigAsMap(app, env);

    Map<String, ConfigItem> oriItem = this.getConfigAsMap(app, env);

    resItems.putAll(commonItems);
    resItems.putAll(oriItem);

    return new ArrayList<ConfigItem>(resItems.values());

  }


  private Map<String, ConfigItem> getConfigAsMap(String app, String env) {

    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");

    if (PathUtil.isContainPathSeparator(env))
      throw new CentralConfigException("env can not contain / or \\");
    Map<String, ConfigItem> itemsMap = new HashMap<String, ConfigItem>();
    try {
      EtcdResponsePromise<EtcdKeysResponse> response =
          client.getDir(baseConfigPath + "/" + app + "/" + env).sorted()
              .timeout(5, TimeUnit.SECONDS).send();
      EtcdKeysResponse allkeys = response.get();
      List<EtcdNode> allNodes = allkeys.node.nodes;
      if (allNodes == null)
        return itemsMap;

      for (EtcdNode node : allNodes) {
        String key = PathUtil.lastPath(node.key);
        // 如果是资源，则取出资源内容显示出来
        if (!key.startsWith(BaseConstant.PERFIX_RESOURCE)) {
          // if (itemsMap.containsKey(key))
          // throw new RuntimeException(key + " is duplicate");
          ConfigItem item = new ConfigItem();
          item.setApp(app);
          item.setEnv(env);
          item.setKey(key);
          ConfigValue cv = mapper.readValue(node.value, ConfigValue.class);
          if (cv != null) {
            item.setDesc(cv.getDesc());
            item.setValue(cv.getValue());
          }
          itemsMap.put(key, item);
        }
      }
      return itemsMap;
    } catch (

    JsonProcessingException e) {
      throw new CentralConfigException(e);
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

  private Map<String, ConfigItem> getAllCommonConfigAsMap(String app) {

    Map<String, ConfigItem> itemsMap = new HashMap<String, ConfigItem>();
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");

    try {
      EtcdResponsePromise<EtcdKeysResponse> response =
          client.getDir(baseConfigPath + "/" + app + "/" + BaseConstant._COMMON).sorted()
              .timeout(5, TimeUnit.SECONDS).send();
      EtcdKeysResponse allkeys = response.get();
      List<EtcdNode> allNodes = allkeys.node.nodes;
      if (allNodes == null)
        return itemsMap;
      for (EtcdNode node : allNodes) {

        ConfigItem item = new ConfigItem();
        item.setApp(app);
        item.setEnv(BaseConstant._COMMON);
        item.setKey(PathUtil.lastPath(node.key));
        // if (itemsMap.containsKey(item.getKey()))
        // throw new RuntimeException(item.getKey() + " is duplicate");
        ConfigValue cv = mapper.readValue(node.value, ConfigValue.class);
        if (cv != null) {
          item.setDesc(cv.getDesc());
          item.setValue(cv.getValue());
        }
        itemsMap.put(item.getKey(), item);
      }
      return itemsMap;
    } catch (JsonProcessingException e) {
      throw new CentralConfigException(e);
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

  private Map<String, ConfigItem> getAllResourceConfigAsMap(String app, String env) {

    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");

    if (PathUtil.isContainPathSeparator(env))
      throw new CentralConfigException("env can not contain / or \\");
    Map<String, ConfigItem> itemsMap = new HashMap<String, ConfigItem>();
    try {
      EtcdResponsePromise<EtcdKeysResponse> response =
          client.getDir(baseConfigPath + "/" + app + "/" + env).sorted()
              .timeout(5, TimeUnit.SECONDS).send();
      EtcdKeysResponse allkeys = response.get();
      List<EtcdNode> allNodes = allkeys.node.nodes;
      if (allNodes == null)
        return itemsMap;

      for (EtcdNode node : allNodes) {
        String key = PathUtil.lastPath(node.key);
        // 如果是资源，则取出资源内容显示出来
        if (key.startsWith(BaseConstant.PERFIX_RESOURCE)) {
          List<ResourceItem> itemList =
              this.getResourceItem(key.substring(key.indexOf(BaseConstant.PERFIX_RESOURCE) + 1));


          if (itemList != null) {
            for (ResourceItem ri : itemList) {
              // if (itemsMap.containsKey(ri.getKey()))
              // throw new RuntimeException(ri.getKey() + " is duplicate");
              ConfigItem ci = new ConfigItem();
              ci.setApp(app);
              ci.setDesc(ri.getDesc());
              ci.setEnv(env);
              ci.setKey(ri.getKey());
              ci.setValue(ri.getValue());
              itemsMap.put(ci.getKey(), ci);
            }

          }

        }
      }
      return itemsMap;
    } catch (JsonProcessingException e) {
      throw new CentralConfigException(e);
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

  public ConfigItem getItem(String app, String env, String key) {
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");

    if (PathUtil.isContainPathSeparator(env))
      throw new CentralConfigException("env can not contain / or \\");

    if (PathUtil.isContainPathSeparator(key))
      throw new CentralConfigException("key can not contain / or \\");
    EtcdResponsePromise<EtcdKeysResponse> rsp;
    try {
      rsp = client.get(baseConfigPath + "/" + app + "/" + env + "/" + key).send();
      ConfigItem ci = new ConfigItem();
      ci.setApp(app);
      ci.setEnv(env);
      ci.setKey(key);
      EtcdNode n = rsp.get().node;
      ConfigValue cv = mapper.readValue(n.value, ConfigValue.class);
      if (cv != null) {
        ci.setDesc(cv.getDesc());
        ci.setValue(cv.getValue());
      }
      return ci;
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

  public void registerConfigNotifier(final String app, final String env, final String key,
      ConfigChangeProcessor notifier) {

    // TODO
  }

  public void registerConfigNotifier(String app, String env, List<String> key,
      ConfigChangeProcessor notifier) {
    // TODO
  }

  public void unregisterConfigNotifier(String app, String env, String key,
      ConfigChangeProcessor notifier) {
    // TODO
  }

  public void unregisterConfigNotifier(String app, String env, List<String> key,
      ConfigChangeProcessor notifier) {
    // TODO
  }

  public void close() {
    try {
      client.close();
    } catch (Exception e) {
      logger.error("close  error ", e);
    }
  }
}
