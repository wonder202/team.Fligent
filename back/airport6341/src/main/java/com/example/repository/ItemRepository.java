package com.example.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long>{
    List<Item> findByItemcateIcatenoOrderByRegdateDesc(Long itemcate, Pageable pageable);

    long countByItemcate_icateno(Long icateno);
}
