package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Directory;
import com.example.demo.entity.File;
import com.example.demo.repository.DirectoryRepository;
import com.example.demo.repository.FileRepository;
import com.example.demo.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
	private final FileRepository fileRepository;
	private final DirectoryRepository directoryRepository;
	
	public FileServiceImpl(FileRepository fileRepository, DirectoryRepository directoryRepository){
		this.fileRepository = fileRepository;
		this.directoryRepository = directoryRepository;
	}

	@Transactional
	@Override
	public void create(String name, Directory directory, long size, LocalDateTime createdBy, LocalDateTime createAt) {
		log.info(name, directory, size, createdBy, createAt);
		File file = new File(null, name, name, directory, size, createdBy, createAt, null, null, Boolean.TRUE);
		fileRepository.saveAndFlush(file);
	}

	@Transactional(readOnly = true)
	@Override
	public List<File> findHomeFile() {
		return fileRepository.findByEnableAndDirectoryIsNull(Boolean.TRUE);
	}

	@Transactional(readOnly = true)
	@Override
	public List<File> findChildFile(Directory directory) {
		return fileRepository.findByEnableAndDirectory(Boolean.TRUE, directory);
	}
	
	@Transactional
	@Override
	public void delete(Long id) {
		Optional<File> file = fileRepository.findById(id);
		file.get().setEnable(Boolean.FALSE);
		fileRepository.saveAndFlush(file.get());
	}

	@Transactional
	@Override
	public void update(Long id, String name, Directory directory) {
		Optional<File> file = fileRepository.findById(id);
		
		file.get().setVirtualName(name);
		file.get().setDirectory(directory);
		
		fileRepository.saveAndFlush(file.get());
	}

}
