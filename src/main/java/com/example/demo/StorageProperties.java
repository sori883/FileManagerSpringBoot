package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * Application Propertiesで定義された設定を読み込む
 * localtion = アップロードファイルの保存先
 */
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {
    private String location;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
	
}
