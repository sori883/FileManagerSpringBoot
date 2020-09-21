package com.example.demo.controller;

import java.security.Principal;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.auth.SimpleLoginUser;
import com.example.demo.service.AccountService;

@Controller
public class IndexController {
	private final AccountService accountService;
	
	public IndexController(AccountService accountService) {
		this.accountService = accountService;
	}
	
	/**
	 * �g�b�v�y�[�W
	 *
	 * @param signupForm �T�C���A�b�v�t�H�[���f�[�^
	 * @return index
	 */
	@RequestMapping(value = "/", method=RequestMethod.GET)
	public String index(@ModelAttribute("signup") SignupForm signupForm, Principal principal) {
		Authentication authentication = (Authentication) principal;
		
		if (authentication != null) {
			 return "redirect:members/";
		}
		
		return "index";
	}
	
	/**
	 * �A�J�E���g�o�^����
	 *
	 * @param signupForm �T�C���A�b�v�t�H�[���f�[�^
	 * @param redirectAttributes ���_�C���N�g��փ��b�Z�[�W�𑗂邽��
	 * @return index (redirect)
	 */
	@RequestMapping(value = "signup", method=RequestMethod.POST)
	public ModelAndView signup(@ModelAttribute("signup") @Validated SignupForm signupForm, BindingResult result,
			ModelAndView mav) {
		mav.setViewName("index");
		if (!result.hasErrors()){
			// USER���[����t�^����
			String[] roles = {"ROLE_USER"};
			accountService.register(signupForm.getName(), signupForm.getEmail(), signupForm.getPassword(), roles);
			mav.addObject("successMessage", "�A�J�E���g�̓o�^���������܂���");
		} else {
			mav.addObject("failureMessage", "�A�J�E���g�̓o�^�Ɏ��s���܂���");
		}
		
		return mav;
	 }
	
	/**
	 * �J�X�^���������O�C����ʂ�\������
	 * @return login
	 */
	@RequestMapping(value = "/login", method=RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error,
			Model model, HttpSession session, Principal principal){
		
		Authentication authentication = (Authentication) principal;
		
		if (authentication != null) {
			 return "redirect:members/";
		}
		
		model.addAttribute("showErrorMsg", false); // �G���[���b�Z�[�W�\���p�̃t���O
		
        if (error != null) {
            if (session != null) {
            	// URL��?error���������ꍇ�̏���
            	// session���擾����AuthenticationException�ɃL���X�g����
                AuthenticationException ex = (AuthenticationException) session
                		// �擾���Ă���session�́ASPRING_SECURITY_LAST_EXCEPTION�̗�O
                        .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                if (ex != null) {
                	// �G���[�����݂�����A�t���O��true�ɂ��āA���b�Z�[�W���i�[
                    model.addAttribute("showErrorMsg", true);
                    model.addAttribute("errorMsg", ex.getMessage());
                }
            }
        }
		
	    return "login";
	}
}
