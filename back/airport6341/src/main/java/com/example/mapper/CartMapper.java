package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.CartDTO;

@Mapper
public interface CartMapper {

    // 아이템 장바구니에 넣기
    public int insertItemInCart(CartDTO cart);

    // 주문 수량 바꾸기
    public int updateCntFromCartItem(CartDTO cart); 

    // 장바구니 목록 조회
    public List<CartDTO> SelectCartList(Map<String,Object> map);

    // 장바구니 목록 아이템 개수
    public Long CartListCount(Map<String,Object> map);

    // 장바구니 아이템 일괄삭제
    public int deletebatchItemFromCart(Map<String,Object> map);

    // 장바구니 아이템 삭제
    public int deleteItemFromCart(Map<String,Object> map);

    // 유저아이디 조회
    public List<CartDTO> selectUseridForDeletebatch(String userid);
}
