package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.entity.ItemImg;
import com.example.entity.ItemImgProject;

@Repository
public interface ItemImgRepository extends JpaRepository<ItemImg, Long>{
    // 글번호에 해당하는 아이템 이미지 번호 오름차순으로 1개만 가져오기 + 반환타입은 ItemProjection 사용
    // ItemImgProject 사용하지 않고 ItemImg 반환하는 경우 모든 데이터를 읽어오기 때문에 느리다
    // 정렬할때 모든 데이터를 다 가져오는 순간 이미지 데이터도 읽어오기 때문에 느림

    Long  findTopByIimagenoOrderByIimagenoAsc(ItemImgProject ItemImgProject);

    ItemImgProject findTopByItem_inoOrderByIimagenoAsc(Long no);

    // no에 해당하는 물품 하위의 이미지 번호를 오름차순으로 가져오기(projection)
    List<ItemImgProject> findByItem_inoOrderByIimagenoAsc(Long no);

    
}
