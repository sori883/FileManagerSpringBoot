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
	 * トップページ
	 *
	 * @param signupForm サインアップフォームデータ
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
	 * アカウント登録処理
	 *
	 * @param signupForm サインアップフォームデータ
	 * @param redirectAttributes リダイレクト先へメッセージを送るため
	 * @return index (redirect)
	 */
	@RequestMapping(value = "signup", method=RequestMethod.POST)
	public ModelAndView signup(@ModelAttribute("signup") @Validated SignupForm signupForm, BindingResult result,
			ModelAndView mav) {
		mav.setViewName("index");
		if (!result.hasErrors()){
			// USERロールを付与する
			String[] roles = {"ROLE_USER"};
			accountService.register(signupForm.getName(), signupForm.getEmail(), signupForm.getPassword(), roles);
			mav.addObject("successMessage", "アカウントの登録が完了しました");
		} else {
			mav.addObject("failureMessage", "アカウントの登録に失敗しました");
		}
		
		return mav;
	 }
	
	/**
	 * カスタムしたログイン画面を表示する
	 * @return login
	 */
	@RequestMapping(value = "/login", method=RequestMethod.GET)
	public String login(@RequestParam(value = "error", required = false) String error,
			Model model, HttpSession session, Principal principal){
		
		Authentication authentication = (Authentication) principal;
		
		if (authentication != null) {
			 return "redirect:members/";
		}
		
		model.addAttribute("showErrorMsg", false); // エラーメッセージ表示用のフラグ
		
        if (error != null) {
            if (session != null) {
            	// URLに?errorがあった場合の処理
            	// sessionを取得してAuthenticationExceptionにキャストする
                AuthenticationException ex = (AuthenticationException) session
                		// 取得しているsessionは、SPRING_SECURITY_LAST_EXCEPTIONの例外
                        .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                if (ex != null) {
                	// エラーが存在したら、フラグをtrueにして、メッセージを格納
                    model.addAttribute("showErrorMsg", true);
                    model.addAttribute("errorMsg", ex.getMessage());
                }
            }
        }
		
	    return "login";
	}
}
