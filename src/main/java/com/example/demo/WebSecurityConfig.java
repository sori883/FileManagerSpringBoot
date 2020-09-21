package com.example.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// �A�J�E���g�o�^���̃p�X���[�h�G���R�[�h�ŗ��p����
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// �ÓI���\�[�X�̏��O�ݒ������
		// @formatter:off
		web
		.debug(false)
		.ignoring()
		.antMatchers("/images/**", "/js/**", "/css/**", "/uploads/**", "/download/**");
		// @formatter:on
	 }
	
	 @Override
	 protected void configure(HttpSecurity http) throws Exception {
	    // @formatter:off
	    http
	      .authorizeRequests()
	        .mvcMatchers("/", "/login", "/signup", "/download/**").permitAll() // �g�b�v�Alogin�Asignup�̓A�N�Z�X�t���[�ɂ���(logout��?)
	        .mvcMatchers("/members/user/**").hasRole("USER") // URL(members/user/**)�́AUSER���[���������O�C�����[�U���A�N�Z�X�ł��� 
	        .mvcMatchers("/members/admin/**").hasRole("ADMIN") // URL(members/admin/**)�́AADMIN���[���������O�C�����[�U���A�N�Z�X�ł���
	        .anyRequest().authenticated() // ��L�ȊO�̃y�[�W�̓��O�C�����[�U�݂̂��A�N�Z�X�ł���
	      .and()
	      .formLogin() // ���O�C���t�H�[�����g����悤�ɂ���
	      	.loginPage("/login") // ���O�C���y�[�W��URL���w��
	        .defaultSuccessUrl("/members/") // ���O�C���������̃��_�C���N�g��
	      .and()
	      .logout() // ���O�A�E�g���g����悤�ɂ���
	        .invalidateHttpSession(true) // �Z�b�V�����𖳌��ɂ���
	        .deleteCookies("JSESSIONID") // JSESSIONID�̃N�b�L�[���폜����
	        .logoutSuccessUrl("/"); // ���O�A�E�g������̃��_�C���N�g��
	    // TODO: H2�R���\�[���̗L����(�̂��߂ɓ��ꂽ���ǁA����������Ȃ����@��logout�Ƃ�403�ɂȂ�̂�)
	    http.csrf().disable();
	    http.headers().frameOptions().disable();
	    // @formatter:on
	 }
}
