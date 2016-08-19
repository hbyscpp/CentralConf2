package com.seaky.centralconf.manager.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.seaky.centralconf.manager.api.CentralConfigManager;
import com.seaky.centralconf.manager.api.EtcdCentralConfigManager;

@Component
@ConfigurationProperties(prefix = "etcd")
public class EtcdConfig {

  private String basePath;
  
  private String urls;

  public String getBasePath() {
    return basePath;
  }

  public void setBasePath(String basePath) {
    this.basePath = basePath;
  }

  public String getUrls() {
    return urls;
  }

  public void setUrls(String urls) {
    this.urls = urls;
  }

  @Bean
  public CentralConfigManager getcentralConfgManager() {
    CentralConfigManager manager = new EtcdCentralConfigManager(basePath,urls);
    return manager;
  }

}
