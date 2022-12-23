package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Member;
import com.example.entity.MemberProject;

@Repository// 기존의 mapper와 같은 역할              
// 기존에 생성되어있는 저장소 상속(NTT, 기본키 타입)  <NTT, NTT key Type>

public interface MemberRepository 
        extends JpaRepository<Member, String> {
    // String name에 해당하는 항목만 검색해준다
    List<MemberProject> findByOrderByRegdateDesc(Pageable pageable);
}