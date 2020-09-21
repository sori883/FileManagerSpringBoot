package com.example.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.FileNotFoundException;
import com.example.demo.StorageException;
import com.example.demo.StorageProperties;
import com.example.demo.service.StorageService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class StrageServiceImpl implements StorageService {

	// application.prop��strage.location
	private final Path rootLocation;

    @Autowired
    public StrageServiceImpl(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage location", e);
        }
    }

    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }

        return filename;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
        	// Files.walk�ő�1������艺�̃t�@�C���A�t�H���_���2��������������
            return Files.walk(this.rootLocation, 1)
            		// filter�ŁA�p�X��uploads�ƈ�v���Ȃ����̂����O����
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    /**
     * �t�@�C���l�[���̃t���p�X�𐶐�����
     */
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
        		// filename�̃t���p�X���擾����
            	Path file = load(filename);
            	// Resource�̃C���X�^���X��(file��URL���������̂��g����)
            	Resource resource = new UrlResource(file.toUri());
            	// ���\�[�X����or���݂��邩�m�F:https://spring.pleiades.io/spring-framework/docs/current/javadoc-api/org/springframework/core/io/Resource.html
            	if (resource.exists() || resource.isReadable()) {
            		return resource;
            	} else {
            		throw new FileNotFoundException("Could not read file: " + filename);
            	}
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

}
