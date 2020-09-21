package com.example.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="user")
// setter/getterを自動生成する:https://blog.y-yuki.net/entry/2016/09/29/000000
@Data
//https://qiita.com/supreme0110/items/391505da8f4321736421
@NoArgsConstructor
@AllArgsConstructor
//excludeしたもの以外をToStringでオーバライドする
@ToString(exclude = {"email", "password"})
public class User {
	  @Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	  
	  @Column(name = "name", length = 60, nullable = false, unique = true)
	  private String name;
	  
	  @Column(name = "email", length = 120, nullable = false, unique = true)
	  private String email;
	  
	  @Column(name = "password", length = 120, nullable = false)
	  private String password;
	  
	  @Column(name = "roles", length = 120)
	  private String roles;
	  
	  @Column(name = "enable_flag", nullable = false)
	  private Boolean enable;
}
