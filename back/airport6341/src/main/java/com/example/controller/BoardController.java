package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "/board")
@RequiredArgsConstructor
public class BoardController {

    final BoardRepository bRepository;


    @GetMapping(value="/select.do")
    public String selectGET(){
        return "board_select";
    }
}
