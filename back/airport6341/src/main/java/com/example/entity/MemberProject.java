package com.example.entity;

import java.util.Date;

// Member enttity의 항목중에서 필요한 일부 컬럼만 가져오기
public interface MemberProject {

    String getUserid();

    int getBlock();
    
    String getNickname();
    
    Date getRegdate();
}
