package com.example.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping(value = {"/","/fligent", "/fligent/vue"})
    public String homeGET(@AuthenticationPrincipal User user){
        if(user != null){
            // System.out.println(user.toString());
        }
        // return "home"; // vue 서버 구동시
        return "forward:/vue/index.html"; // 배포시
    }

    //     // 메인페이지로 이동
//     @GetMapping(value = {"/", "/home", "/home.json"})
//     public Map<String, Object> homeGET(Model model, @AuthenticationPrincipal User user){ // UserDetails에서 반환된 User
//         Map<String, Object> map = new HashMap<>();
//         if( user == null ){
//         System.out.println("로그인 실패");
//         map.put("status", -1);
//         return map;
//     } else {
//         System.out.println("로그인 성공");
//         // model.addAttribute("user", user); // user의 정보를 담아서 
//         map.put("user", user);
//         map.put("status", 200);
//         return map;
//     }
// }

    @GetMapping(value = "/page403.do")
    public String page403Get(){
        return "page403";
    }
    

}