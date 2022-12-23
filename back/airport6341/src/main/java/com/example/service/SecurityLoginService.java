package com.example.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.entity.Member;
import com.example.repository.MemberRepository;

@Service
public class SecurityLoginService implements UserDetailsService {

    @Autowired MemberRepository mRepository;
    
    // 0. 로그인 화면에서 전달되어 호출되는 오브라이드된 메소드
    // 1. 로그인에서 전송되는 항목은 아이디가 전송됨.
    // 2. 아이디를 이용해서 db에서아이디, 암호, 권한을 꺼냄
    // 3. UserDetails의 객체를 만들다음 반환하면 시큐리티 비교후에 로그인처리
    @Override
    public UserDetails loadUserByUsername(String username) 
        throws UsernameNotFoundException { 
        
        // DB에서 아이디를 이용해서 정보를 가져온다음    
        Member member = mRepository.findById(username).orElse(null);

        // Member role = mRepository.findById(username).orElse(null);

        
        // member의 정보가 비어있지 않은 경우 && member의 block값이 0인 경우 = 차단되지 않은 경우
        if(member != null && member.getBlock() == 0) {



            Collection<GrantedAuthority> role 
                = AuthorityUtils.createAuthorityList(member.getRole());
            //아이디, 암호, 권한들..(연락처)
            User user = new User(member.getUserid(), member.getUserpw(), role);


            return user;
        }
        else {
            Collection<GrantedAuthority> role =  AuthorityUtils.createAuthorityList("_");
            User user = new User("", "_", role );
            return user;    
        }
    }
    
}
