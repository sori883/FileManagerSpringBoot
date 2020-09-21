package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Directory;

public interface DirectoryRepository extends JpaRepository<Directory, Long> {
	List<Directory> findByPathIsNull();
	
	// �Ǘ��҂͑S�Ă��擾���郁�\�b�h���g��
	List<Directory> findByEnableAndPath(boolean flag, String path);
	List<Directory> findByEnableAndPathIsNull(boolean flag);
	
	// ��ʂ�auth�ōi
	List<Directory> findByEnableAndPathIsNullAndAuth(boolean flag, boolean auth);
	List<Directory> findByEnableAndPathAndAuth(boolean flag, String path, boolean auth);
	
}
