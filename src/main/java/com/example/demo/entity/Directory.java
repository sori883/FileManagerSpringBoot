package com.example.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="directory")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"createdBy", "createAt", "updatedBy", "updateAt"})
public class Directory {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	  
	  @OneToMany
	  @Column(nullable = true)
	  private List<File> filedates;
	  
	  @NotNull
	  @Column(name = "name", length = 60)
	  private String name;
	  
	  @Column(name = "path", length = 255)
	  private String path;
	  
	  @Column(name="created_by")
	  private Long createdBy;
	  
	  @Column(name="create_at")
	  @JsonFormat(pattern = "y/M/d H:m")
	  private LocalDateTime createAt;
	  
	  @Column(name="updated_by")
	  private Long updatedBy;
	  
	  @Column(name="update_at")
	  private LocalDateTime updateAt;
	  
	  @Column(name = "auth", nullable = false)
	  private Boolean auth;
	  
	  @Column(name = "enable_flag", nullable = false)
	  private Boolean enable;
	  
}
