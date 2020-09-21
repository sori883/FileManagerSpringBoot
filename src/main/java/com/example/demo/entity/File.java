package com.example.demo.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name="file")
@Data
@AllArgsConstructor
@ToString(exclude = {"directory", "size", "createdBy", "createAt", "updatedBy", "updateAt"})
public class File {
	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Long id;
	 
	 @Column(name="name", length=60)
	 private String name;
	 
	 @Column(name="vartual_name", length=60)
	 private String virtualName;
	 
	 @ManyToOne
	 private Directory directory;
	 
	 @Column(name="size")
	 private float size;
	 
	 @Column(name="created_by")
	 private LocalDateTime createdBy;

	 @Column(name="create_at")
	 @JsonFormat(pattern = "y/M/d H:m")
	 private LocalDateTime createAt;
	  
	 @Column(name="updated_by")
	 private LocalDateTime updatedBy;
	  
	 @Column(name="update_at")
	 private LocalDateTime updateAt;
	  
	 @Column(name = "enable_flag", nullable = false)
	 private Boolean enable;
	 
	 public File() {
		 super();
		 directory = new Directory();
	 }
}
