package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Directory;
import com.example.demo.entity.User;
import com.example.demo.repository.DirectoryRepository;
import com.example.demo.service.DirService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DirServiceImpl implements DirService {

	private final DirectoryRepository directoryRepository;
	
	public DirServiceImpl(DirectoryRepository directoryRepository){
		this.directoryRepository = directoryRepository;
	}
	
	@Transactional
	@Override
	public void create(String name, String path, Long createdBy, int auth, LocalDateTime createAt) {
		log.info(name, path, createdBy, createAt);
		
		boolean boolAuth = Boolean.FALSE; // ëSàıâ{óóâ¬
		if (auth == 1){
			boolAuth = Boolean.TRUE; // ä«óùé“ÇÃÇ›â{óóâ¬
		}

		Directory directory = new Directory(null, null, name, path, createdBy, createAt, null, null, boolAuth, Boolean.TRUE);
		directoryRepository.saveAndFlush(directory);
	}

	@Transactional(readOnly = true)
	@Override
	public List<Directory> findHomeDir(User user) {
		
		List<Directory> result = null;
		
		if (user.getRoles().contains("ADMIN")) {
			result = directoryRepository.findByEnableAndPathIsNull(Boolean.TRUE);
		} else {
			result = directoryRepository.findByEnableAndPathIsNullAndAuth(Boolean.TRUE, Boolean.FALSE);
		}
		
		return result;
	}

	@Transactional(readOnly = true)
	@Override
	public Optional<Directory> findById(Long id) {
		return directoryRepository.findById(id);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Directory> findChildDir(User user, String path) {
		List<Directory> result = null;
		
		if (user.getRoles().contains("ADMIN")) {
			result = directoryRepository.findByEnableAndPath(Boolean.TRUE, path);
		} else {
			result = directoryRepository.findByEnableAndPathAndAuth(Boolean.TRUE, path, Boolean.FALSE);
		}
		
		
		return result;
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		Optional<Directory> dir = directoryRepository.findById(id);
		dir.get().setEnable(Boolean.FALSE);
		directoryRepository.saveAndFlush(dir.get());
	}

	@Transactional
	@Override
	public void update(Long id, String name, String path, int auth) {
		Optional<Directory> dir = directoryRepository.findById(id);
		dir.get().setName(name);
		dir.get().setPath(path);
		boolean boolAuth = Boolean.FALSE; // ëSàıâ{óóâ¬
		if (auth == 1){
			boolAuth = Boolean.TRUE; // ä«óùé“ÇÃÇ›â{óóâ¬
		}
		dir.get().setAuth(boolAuth);
		
		directoryRepository.saveAndFlush(dir.get());
	}
	

	

}
