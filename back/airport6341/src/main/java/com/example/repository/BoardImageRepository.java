package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import com.example.entity.BoardImg;

@Repository
public interface BoardImageRepository extends JpaRepository<BoardImg, String>{
}
