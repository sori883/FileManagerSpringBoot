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

	// アカウント登録時のパスワードエンコードで利用する
	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		// 静的リソースの除外設定をする
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
	        .mvcMatchers("/", "/login", "/signup", "/download/**").permitAll() // トップ、login、signupはアクセスフリーにする(logoutも?)
	        .mvcMatchers("/members/user/**").hasRole("USER") // URL(members/user/**)は、USERロールを持つログインユーザがアクセスできる 
	        .mvcMatchers("/members/admin/**").hasRole("ADMIN") // URL(members/admin/**)は、ADMINロールを持つログインユーザがアクセスできる
	        .anyRequest().authenticated() // 上記以外のページはログインユーザのみがアクセスできる
	      .and()
	      .formLogin() // ログインフォームが使えるようにする
	      	.loginPage("/login") // ログインページのURLを指定
	        .defaultSuccessUrl("/members/") // ログイン成功時のリダイレクト先
	      .and()
	      .logout() // ログアウトを使えるようにする
	        .invalidateHttpSession(true) // セッションを無効にする
	        .deleteCookies("JSESSIONID") // JSESSIONIDのクッキーを削除する
	        .logoutSuccessUrl("/"); // ログアウト完了後のリダイレクト先
	    // TODO: H2コンソールの有効化(のために入れたけど、これを書かない方法とlogoutとか403になるので)
	    http.csrf().disable();
	    http.headers().frameOptions().disable();
	    // @formatter:on
	 }
}
