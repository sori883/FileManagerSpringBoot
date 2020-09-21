package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.example.demo.entity.Directory;
import com.example.demo.entity.User;

public interface DirService {
	void create(String name, String path, Long createdBy, int auth, LocalDateTime createAt);	
	
	Optional<Directory> findById(Long id);
	List<Directory> findHomeDir(User user);
	List<Directory> findChildDir(User user, String path);
	
	void delete(Long id);
	void update(Long id, String name, String path, int auth);
	
}
