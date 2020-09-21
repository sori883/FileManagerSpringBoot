package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Directory;
import com.example.demo.entity.File;

public interface FileRepository extends JpaRepository<File, Long> {
	List<File> findByDirectoryIsNull();
	List<File> findByEnableAndDirectoryIsNull(boolean flag);
	List<File> findByEnableAndDirectory(boolean flag, Directory directory);
}
