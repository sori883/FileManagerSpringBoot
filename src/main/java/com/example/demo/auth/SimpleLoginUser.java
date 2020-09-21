package com.example.demo.auth;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.demo.entity.User;

public class SimpleLoginUser extends org.springframework.security.core.userdetails.User {
	 // DB��茟������User�G���e�B�e�B
	 // �A�v���P�[�V�������痘�p�����̂Ńt�B�[���h�ɒ�`
	 private User user;
	
	/**
	 * �f�[�^�x�[�X��茟������user�G���e�B�e�B���Spring Security�Ŏg�p���郆�[�U�[�F�؏���
	 * �C���X�^���X�𐶐�����
	 *
	 * @param user user�G���e�B�e�B
	 */
	public SimpleLoginUser(User user) {
		// User Entity�̃R���X�g���N�^�����s���āA�l���i�[����
		super(user.getName(), user.getPassword(), user.getEnable(), true, true,
        true, convertGrantedAuthorities(user.getRoles()));
		this.user = user; // �t�B�[���h��user��user���i�[����
	}
	
	/**
	 * �t�B�[���h(user)��getter
	 */
	public User getUser() {
		return user;
	}
	
	 /**
	  * �J���}��؂�̃��[����SimpleGrantedAuthority�̃R���N�V�����֕ϊ�����
	  *
	  * @param roles �J���}��؂�̃��[��(��̕�����)
	  * @return SimpleGrantedAuthority�̃R���N�V����(role���ɃR���N�V�������쐬����)
	  */
	 static Set<GrantedAuthority> convertGrantedAuthorities(String roles) {
	   if (roles == null || roles.isEmpty()) {
	     return Collections.emptySet();
	   }
	   Set<GrantedAuthority> authorities = Stream.of(roles.split(","))
	       .map(SimpleGrantedAuthority::new)
	       .collect(Collectors.toSet());
	   return authorities;
	 }
	
}
