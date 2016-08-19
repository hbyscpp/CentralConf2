package com.seaky.centralconf.manager.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.seaky.centralconf.core.ConfigItem;
import com.seaky.centralconf.core.ConfigValue;
import com.seaky.centralconf.core.ResourceItem;
import com.seaky.centralconf.core.api.EtcdCentralConfigClient;
import com.seaky.centralconf.core.constant.BaseConstant;
import com.seaky.centralconf.core.exception.CentralConfigException;
import com.seaky.centralconf.core.util.PathUtil;

import mousio.etcd4j.promises.EtcdResponsePromise;
import mousio.etcd4j.requests.EtcdKeyPutRequest;
import mousio.etcd4j.responses.EtcdAuthenticationException;
import mousio.etcd4j.responses.EtcdException;
import mousio.etcd4j.responses.EtcdKeysResponse;
import mousio.etcd4j.responses.EtcdKeysResponse.EtcdNode;

public class EtcdCentralConfigManager extends EtcdCentralConfigClient
    implements CentralConfigManager {

  public EtcdCentralConfigManager(String basePath, String urls) {
    super(basePath, urls);
  }

  public void createApp(String app) {
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");
    String dir = super.baseConfigPath + "/" + app;
    super.creatDir(dir);
  }

  public boolean isExist(String app) {
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");
    String dir = super.baseConfigPath + "/" + app;
    return super.isDirExist(dir);
  }

  public void deleteApp(String app) {
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");
    String dir = super.baseConfigPath + "/" + app;
    super.deleteDir(dir);
  }

  public List<String> getAllApp() {
    EtcdKeysResponse allkeys = super.getDir(baseConfigPath);
    if (allkeys == null)
      return new ArrayList<String>();
    List<EtcdNode> allNodes = allkeys.node.nodes;

    if (allNodes == null)
      return new ArrayList<String>();
    List<String> allAppNames = new ArrayList<String>();

    for (EtcdNode node : allNodes) {
      String keyName = PathUtil.lastPath(node.key);
      allAppNames.add(keyName);
    }
    return allAppNames;
  }

  public void createEvn(String app, String env) {
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\ ");

    if (PathUtil.isContainPathSeparator(env))
      throw new CentralConfigException("env can not contain / or \\");
    try {
      EtcdResponsePromise<EtcdKeysResponse> rsp =
          client.putDir(baseConfigPath + "/" + app + "/" + env).send();
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

  public void deleteEnv(String app, String env) {
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");

    if (PathUtil.isContainPathSeparator(env))
      throw new CentralConfigException("env can not contain / or \\");
    try {
      EtcdResponsePromise<EtcdKeysResponse> rsp =
          client.deleteDir(baseConfigPath + "/" + app + "/" + env).recursive().send();
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

  // 复制,
  public void copyEnv(String app, String srcEnv, String dstEnv, boolean isOverride) {
    copyEnv(app, srcEnv, app, dstEnv, isOverride);

  }

  private void copyEnv(String app, String srcEnv, String dstApp, String dstEnv,
      boolean isOverride) {
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");

    if (PathUtil.isContainPathSeparator(dstApp))
      throw new CentralConfigException("app can not contain / or \\");

    if (PathUtil.isContainPathSeparator(srcEnv))
      throw new CentralConfigException("srcEnv can not contain / or \\");

    if (PathUtil.isContainPathSeparator(dstEnv))
      throw new CentralConfigException("dstEnv can not contain / or \\");

    List<ConfigItem> srcItems = getExportItem(app, srcEnv);

    if (srcItems == null)
      return;
    for (ConfigItem ci : srcItems) {
      ci.setEnv(dstEnv);
      ci.setApp(dstApp);
      updateConfig(ci, isOverride);

    }

  }

  public List<String> getAllEnv(String app) {
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");
    String dir = super.baseConfigPath + "/" + app;
    EtcdKeysResponse allkeys = super.getDir(dir);
    List<EtcdNode> allNodes = allkeys.node.nodes;

    if (allNodes == null)
      return null;
    List<String> allAppNames = new ArrayList<String>();

    for (EtcdNode node : allNodes) {
      String keyName = PathUtil.lastPath(node.key);
      // 公共资源 不显示在平常的列表中
      if (!keyName.equals(BaseConstant._COMMON)) {
        allAppNames.add(keyName);
      }
    }
    return allAppNames;

  }

  public void updateConfig(ConfigItem item) {
    updateConfig(item, true);
  }

  // 已经存在的是否覆盖
  public void updateConfig(ConfigItem item, boolean isOverride) {
    if (PathUtil.isContainPathSeparator(item.getApp()))
      throw new CentralConfigException("app can not contain / or \\");

    if (PathUtil.isContainPathSeparator(item.getEnv()))
      throw new CentralConfigException("env can not contain / or \\");

    if (PathUtil.isContainPathSeparator(item.getKey()))
      throw new CentralConfigException("key can not contain / or \\");

    ConfigItem oldItem = getItem(item.getApp(), item.getEnv(), item.getKey());

    boolean isExist = false;
    if (oldItem != null) {
      isExist = true;
      if (StringUtils.equals(oldItem.getValue(), item.getValue())
          && StringUtils.equals(oldItem.getDesc(), item.getDesc())) {
        return;
      }
      if (!isOverride)
        return;
    }
    ConfigValue cv = new ConfigValue();
    cv.setDesc(item.getDesc());
    cv.setValue(item.getValue());
    try {
      EtcdKeyPutRequest req = client.put(
          baseConfigPath + "/" + item.getApp() + "/" + item.getEnv() + "/" + item.getKey(),
          mapper.writeValueAsString(cv));
      EtcdResponsePromise<EtcdKeysResponse> rsp = null;
      if (isExist) {
        rsp = req.prevExist(true).send();
      } else {
        rsp = req.send();
      }
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

  public void deleteConfig(String app, String env, String key) {
    if (PathUtil.isContainPathSeparator(app))
      throw new CentralConfigException("app can not contain / or \\");

    if (PathUtil.isContainPathSeparator(env))
      throw new CentralConfigException("env can not contain / or \\");

    if (PathUtil.isContainPathSeparator(key))
      throw new CentralConfigException("key can not contain / or \\");
    String dir = baseConfigPath + "/" + app + "/" + env + "/" + key;
    super.deleteDir(dir);
    // try {
    // EtcdResponsePromise<EtcdKeysResponse> rsp = client.deleteDir(baseConfigPath + "/" + app + "/"
    // + env + "/" + key)
    // .send();
    // rsp.get();
    // } catch (IOException e) {
    // throw new CentralConfigException(e);
    // } catch (EtcdException e) {
    // if (e.errorCode == 100)
    // return;
    // throw new CentralConfigException(e);
    // } catch (EtcdAuthenticationException e) {
    // throw new CentralConfigException(e);
    // } catch (TimeoutException e) {
    // throw new CentralConfigException(e);
    // }
  }

  public void rename(String oldApp, String newApp) {

    if (StringUtils.equals(oldApp, newApp))
      return;
    if (PathUtil.isContainPathSeparator(oldApp) || PathUtil.isContainPathSeparator(newApp))
      throw new CentralConfigException("app can not contain / or \\");
    try {
      if (isExist(newApp))
        throw new RuntimeException("new app name exist");
      List<String> allEnv = getAllEnv(oldApp);

      if (allEnv != null) {
        for (String env : allEnv) {
          copyEnv(oldApp, env, newApp, env, true);
        }
      } else {
        createApp(newApp);
      }
      deleteApp(oldApp);
    } catch (Exception e) {
      throw new CentralConfigException(e);
    }

  }

  public void renameItem(String app, String env, String oldName, String newName,
      boolean isOverride) {
    ConfigItem ci = getItem(app, env, oldName);
    if (ci == null)
      throw new RuntimeException("item is not exist");

    ci.setKey(newName);

    updateConfig(ci, isOverride);

  }

  public void createRsc(String rsc) {
    if (PathUtil.isContainPathSeparator(rsc))
      throw new CentralConfigException("rsc can not contain / or \\");
    String dir = super.baseResourcePath + "/" + rsc;
    super.creatDir(dir);
  }

  public void deleteRsc(String rsc) {
    if (PathUtil.isContainPathSeparator(rsc))
      throw new CentralConfigException("rsc can not contain / or \\");
    String dir = super.baseResourcePath + "/" + rsc;
    super.deleteDir(dir);
  }

  public List<String> getAllRsc() {
    EtcdKeysResponse allkeys = super.getDir(super.baseResourcePath);
    // 后期可删，初始化，生成resource节点
    if (allkeys == null) {
      super.creatDir(super.baseResourcePath);
      return null;
    }
    List<EtcdNode> allNodes = allkeys.node.nodes;

    if (allNodes == null)
      return null;
    List<String> allAppNames = new ArrayList<String>();

    for (EtcdNode node : allNodes) {
      String keyName = PathUtil.lastPath(node.key);
      allAppNames.add(keyName);
    }
    return allAppNames;
  }

  public void updateRscItem(ResourceItem item) {
    if (PathUtil.isContainPathSeparator(item.getRsc()))
      throw new CentralConfigException("rsc can not contain / or \\");

    if (PathUtil.isContainPathSeparator(item.getKey()))
      throw new CentralConfigException("key can not contain / or \\");

    try {
      String key = super.baseResourcePath + "/" + item.getRsc() + "/" + item.getKey();
      ConfigValue cv = new ConfigValue();
      cv.setValue(item.getValue());
      cv.setDesc(item.getDesc());
      String value = mapper.writeValueAsString(cv);
      super.addOrUpdateKey(key, value);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  public void deleteRscItem(String rsc, String key) {
    if (PathUtil.isContainPathSeparator(rsc))
      throw new CentralConfigException("rsc can not contain / or \\");

    if (PathUtil.isContainPathSeparator(key))
      throw new CentralConfigException("key can not contain / or \\");
    String dir = super.baseResourcePath + "/" + rsc + "/" + key;
    super.deleteDir(dir);
  }

}
