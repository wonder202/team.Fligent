package com.example.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class LikeCountViewDTO {
    Long cnt;
    
    Long bno; 
    
    String content; 
    
    Long hit; 

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd", timezone="Asia/Seoul") //날짜 포멧 바꾸기
    Date regdate; 
    
    String title; 
    
    String userid; 
}

