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
	// Email�Ńf�[�^���������邾��
	private final UserRepository userRepository;
	
	
	public SimpleUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	/**
	 * ���[���A�h���X�Ō����������[�U�[��user�G���e�B�e�B��SimpleLoginUser�N���X�̃C���X�^���X�֕ϊ�����
	 *
	 * @param email �������郆�[�U�[�̃��[���A�h���X
	 * @return ���[���A�h���X�Ō����ł������[�U�[�̃��[�U�[���
	 * @throws UsernameNotFoundException ���[���A�h���X�Ń��[�U�[�������ł��Ȃ������ꍇ�ɃX���[����B
	 */
	@Transactional(readOnly=true) // DB�̓ǂݍ��݂���������
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		assert(name != null);
	    log.debug("loadUserByUsername(email):[{}]", name);
	    return userRepository.findByName(name)
	        .map(SimpleLoginUser::new)
	        .orElseThrow(() -> new UsernameNotFoundException("User not found by Name:[" + name + "]"));
	 }
}
