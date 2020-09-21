package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * Application Properties�Œ�`���ꂽ�ݒ��ǂݍ���
 * localtion = �A�b�v���[�h�t�@�C���̕ۑ���
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
