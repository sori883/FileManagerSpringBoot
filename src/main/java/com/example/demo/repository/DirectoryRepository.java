package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Directory;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {
	List<Directory> findByPathIsNull();
	
	// 管理者は全てを取得するメソッドを使う
	List<Directory> findByEnableAndPath(boolean flag, String path);
	List<Directory> findByEnableAndPathIsNull(boolean flag);
	
	// 一般はauthで絞
	List<Directory> findByEnableAndPathIsNullAndAuth(boolean flag, boolean auth);
	List<Directory> findByEnableAndPathAndAuth(boolean flag, String path, boolean auth);
	
}
