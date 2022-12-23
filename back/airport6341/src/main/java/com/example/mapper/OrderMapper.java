package com.example.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.example.dto.AddressListDTO;
import com.example.dto.InsertOrderDTO;
import com.example.dto.OrderDTO;
import com.example.dto.OrderItemDTO;
import com.example.dto.OrderListDTO;
import com.example.dto.OrderMemberDTO;


@Mapper
public interface OrderMapper {
    
    // 주소 등록하기
    public int insertcustomeraddress(AddressListDTO address);

    // 주문페이지에 회원 정보 불러오기(이름,유저아이디,휴대폰)
    public OrderMemberDTO selectcustomer(String userid);

    // 주문 등록하기
    public int insertCustomerOrders(InsertOrderDTO orders);

    // 주문 일괄 등록하기
    public int insertbatchCustomerOrders(List<InsertOrderDTO> orders);

    // 주문 완료후 주문 목록 조회 + 페이지 네이션 (10개)
    public List<OrderListDTO> selectOrderList(Map<String,Object> map);

    // 주문 완료 후 주문 번호 조회
    public Long selectOrdercode(String userid);

    // 주문 목록 카운트
    public long OrderCountList(String userid);

    // 주문 취소
    public int deleteOrder(Map<String,Object> map);

    // 상품 정보 불러 오기
    public List<OrderItemDTO> selectitem(Map<String,Object> map);

    // 주문 수량 수정
    public int updatecnt(OrderDTO orderDTO);

    // 주문시 장바구니에 있는 아이템도 지우기
    public int deleteItemFromCartWhenOrder(List<InsertOrderDTO> orders);

    // 주소 정보 불러오기
    public List<AddressListDTO> selectAddressList(String userid);

}
