package com.example.demo.controller;



import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateDirForm{
	
	@NotNull
	@Size(max = 60)
	private String name;
	
	@NotNull
	private int authDir;
	
	@NotNull
	@Size(max = 255)
	private String path;
}
