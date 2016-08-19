package com.seaky.centralconf.core.api;

import java.util.List;

import com.seaky.centralconf.core.ConfigItem;

public interface CentralConfigClient {

  /**
   * 
   * @Description: 页面展示的内容，含有公共配置与资源的具体配置 @param TODO @return List<ConfigItem> @throws
   */
  public List<ConfigItem> getAllItem(String app, String env);

  /**
   * 
   * @Description: 需要导出的原始数据 @param TODO @return List<ConfigItem> @throws
   */
  public List<ConfigItem> getExportItem(String app, String env);


  public ConfigItem getItem(String app, String env, String key);

  public void registerConfigNotifier(String app, String env, String key,
      ConfigChangeProcessor notifier);

  public void registerConfigNotifier(String app, String env, List<String> key,
      ConfigChangeProcessor notifier);

  public void close();
}
