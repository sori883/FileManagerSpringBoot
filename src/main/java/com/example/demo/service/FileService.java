package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.Directory;
import com.example.demo.entity.File;

public interface FileService {
	void create (String name, Directory directory, long size, LocalDateTime createdBy, LocalDateTime createAt);
	
	List<File> findHomeFile();
	List<File> findChildFile(Directory directory);
	
	void update(Long id, String name, Directory directory);
	void delete(Long id);
}
