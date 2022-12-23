package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.dto.CustomUser;


@Controller
@RequestMapping(value="/customer")
public class CustomerController {
    
    // @AuthenticationPrincipal User user 세션정보 꺼내기
    // 아이디, 암호, 권한, (연락처)
    @GetMapping(value="/home.do")
    public String getMethodName(Model model,
        @AuthenticationPrincipal CustomUser user) {
            
        if(user == null){
            return "redirect:/member/login.do";
        }
        model.addAttribute("user", user);
        return "customer_home";
    }
    
}
