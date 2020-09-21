package com.example.demo.auth;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SimpleUserDetailsService implements UserDetailsService {
	// Repository\UserRepository
	// Emailでデータを検索するだけ
	private final UserRepository userRepository;
	
	
	public SimpleUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
	 * メールアドレスで検索したユーザーのuserエンティティをSimpleLoginUserクラスのインスタンスへ変換する
	 *
	 * @param email 検索するユーザーのメールアドレス
	 * @return メールアドレスで検索できたユーザーのユーザー情報
	 * @throws UsernameNotFoundException メールアドレスでユーザーが検索できなかった場合にスローする。
	 */
	@Transactional(readOnly=true) // DBの読み込みだけ許可する
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		assert(name != null);
	    log.debug("loadUserByUsername(email):[{}]", name);
	    return userRepository.findByName(name)
	        .map(SimpleLoginUser::new)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found by Name:[" + name + "]"));
	 }
}
