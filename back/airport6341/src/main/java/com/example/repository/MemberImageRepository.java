package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.MemberImg;

@Repository
public interface MemberImageRepository extends JpaRepository<MemberImg, Long>{

    
}
