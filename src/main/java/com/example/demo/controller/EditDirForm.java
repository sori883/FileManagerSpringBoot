package com.example.demo.controller;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditDirForm {
	@NotEmpty
	private Long id;
	@NotEmpty
	@Size(max = 60)
	private String name;
	@NotEmpty
	private int authDir;
}
