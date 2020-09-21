package com.example.demo.auth;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.example.demo.entity.User;

public class SimpleLoginUser extends org.springframework.security.core.userdetails.User {
	 // DBより検索したUserエンティティ
	 // アプリケーションから利用されるのでフィールドに定義
	 private User user;
	
	/**
	 * データベースより検索したuserエンティティよりSpring Securityで使用するユーザー認証情報の
	 * インスタンスを生成する
	 *
	 * @param user userエンティティ
	 */
	public SimpleLoginUser(User user) {
		// User Entityのコンストラクタを実行して、値を格納する
		super(user.getName(), user.getPassword(), user.getEnable(), true, true,
        true, convertGrantedAuthorities(user.getRoles()));
		this.user = user; // フィールドのuserにuserを格納する
	}
	
	/**
	 * フィールド(user)のgetter
	 */
	public User getUser() {
		return user;
	}
	
	 /**
	  * カンマ区切りのロールをSimpleGrantedAuthorityのコレクションへ変換する
	  *
	  * @param roles カンマ区切りのロール(一つの文字列)
	  * @return SimpleGrantedAuthorityのコレクション(role毎にコレクションを作成する)
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
