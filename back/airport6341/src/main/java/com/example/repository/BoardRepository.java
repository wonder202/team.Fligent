package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Board;
import com.example.entity.BoardProject;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long>{
    
    List<BoardProject> findByOrderByBnoDesc(Pageable pageable);

}