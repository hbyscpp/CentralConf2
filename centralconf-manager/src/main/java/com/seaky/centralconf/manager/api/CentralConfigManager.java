package com.seaky.centralconf.manager.api;

import java.util.List;

import com.seaky.centralconf.core.ConfigItem;
import com.seaky.centralconf.core.ResourceItem;
import com.seaky.centralconf.core.api.CentralConfigClient;

public interface CentralConfigManager extends CentralConfigClient {

  public void createApp(String app);

  public void deleteApp(String app);

  public List<String> getAllApp();

  public void createEvn(String app, String env);

  public void deleteEnv(String app, String env);

  public void copyEnv(String app, String srcEnv, String dstEnv, boolean isOverrie);

  public List<String> getAllEnv(String app);

  public void updateConfig(ConfigItem item);

  public void deleteConfig(String app, String env, String key);

  public void rename(String oldApp, String newApp);

  public void renameItem(String app, String env, String oldName, String newName,
      boolean isOverride);

  /**
   * 公共资源
   */
  public void createRsc(String rsc);

  public void deleteRsc(String rsc);

  public List<String> getAllRsc();
  
  public void updateRscItem(ResourceItem item);
  
  public void deleteRscItem(String rsc, String key);

  
  /**
   * 获取方法
   */
  
  /**
   * 
  * @Description: 获得etcd中所存储的内容，没有公共配置与资源的具体配置
  * @param TODO 
  * @return List<ConfigItem>    
  * @throws
   */
  public List<ConfigItem> getOriAllItem(String app, String env);
  
  /**
   * 
  * @Description: 一应用下所有环境的 公共配置
  * @param TODO 
  * @return List<ConfigItem>    
  * @throws
   */
  public List<ConfigItem> getCommonItem(String app);
  
  /**
   * 
  * @Description: 一应用，一环境下的 所关联的所有公共资源
  * @param TODO 
  * @return List<ConfigItem>    
  * @throws
   */
  public List<ResourceItem> getRelativeResourceItem(String app, String env);
  
  /**
   * 
  * @Description: 获得资源名下所有配置
  * @param TODO 
  * @return List<ResourceItem>    
  * @throws
   */
  public List<ResourceItem> getResourceItem(String rsc);
}
