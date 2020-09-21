package com.example.demo.service;

import java.util.List;
import com.example.demo.entity.User;

public interface AccountService {
	  List<User> findAll(boolean flag);
	  void register(String name, String email, String password, String[] roles);
	  void delete(Long id);
	  void addRole(Long id);
	  void delRole(Long id);
}
