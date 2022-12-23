package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.AddressListDTO;
import com.example.dto.InsertOrderDTO;
import com.example.dto.OrderDTO;
import com.example.dto.OrderItemDTO;
import com.example.dto.OrderListDTO;
import com.example.dto.OrderMemberDTO;
import com.example.mapper.OrderMapper;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping(value = "/api/order")
@RequiredArgsConstructor
public class OrderRestController {
    
    @Autowired OrderMapper orderMapper;
   

    // 고객 - 주소 입력 (TOKEN 필요)
    // 주소 api 쓰기로 했지만 프론트와 상의 후 혹시 모르니 놔두기로 결정
    // 127.0.0.1:8080/ROOT/api/order/insertcustomeraddress.json
    // 회원이 주소 등록 할때 쓰면 됨.
    // {"address":"부산동성직업전문학교","comment": "606호앞에놔주세요", "postcode":"우편번호" ,"name":"이름","phone":"112","detailaddress":"606호"}
    @PostMapping(value = "insertcustomeraddress.json")
    public Map<String,Object> insertcustomeraddressPOST(
        @RequestBody AddressListDTO address,
        HttpServletRequest httpServletRequest
    ){
        Map<String, Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
            address.setUserid(userid);
            int ret = orderMapper.insertcustomeraddress(address);
            if(ret == 1){
                map.put("status", 200);
                map.put("address", address);
            }
            else{
                map.put("status", 0);
                map.put("result", "등록실패");
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }


    // 고객 - 회원 정보 불러오기(닉네임,유저정보,연락처,주소정보(직전 배송지))
    // where userid
    // regdate desc
    // ROWN
    // 주문페이지에서 회원 정보,주소정보를 불러오고 싶을때 쓰면 됨.
    // (TOKEN 필요)
    // 127.0.0.1:8080/ROOT/api/order/selectmemberone.json
    @GetMapping(value = "/selectmemberone.json")
    public Map<String,Object> selectmemberoneGET(
        HttpServletRequest httpServletRequest
    ){
        Map<String, Object> map = new HashMap<>();
        String userid = (String) httpServletRequest.getAttribute("username");
        OrderMemberDTO member = orderMapper.selectcustomer(userid);
        List<AddressListDTO> list = orderMapper.selectAddressList(userid);
        if(member != null){
            map.put("list", list);
            map.put("status", 200);
            map.put("member", member);
        }
        else{
            map.put("status", 0);
        }
        return map;
    }

    // 고객 - 장바구니 상품 정보 불러오기(상품명, 상품 가격,주문 수량,총 주문 가격+(배송비)3000원)
    // 고객이 장바구니에 담은 아이템을 주문할때 상품 정보를 불러오고 싶을때 쓰면 됨.
    // 127.0.0.1:8080/ROOT/api/order/selectitem.json?cno=&cno=
    // (TOKEN 필요)
    // 장바구니와 아이템 테이블 inner join 후 주문하려는 아이템을 유저아이디,아이템번호를 통해서 들고 옴
    @GetMapping(value = "/selectitem.json")
    public Map<String,Object> selectitemGET(
        HttpServletRequest httpServletRequest,
        @RequestParam(name = "cno") List<Long> cno
    ){
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> map1 = new HashMap<>();
        String userid = (String) httpServletRequest.getAttribute("username");

        map1.put("userid", userid);
        map1.put("cno", cno);
        List<OrderItemDTO> item = orderMapper.selectitem(map1);
        if(item != null){
            map.put("status", 200);
            map.put("item", item);
        }
        else{
            map.put("status", 0);
        }
        return map;
    }

    // 고객 - 주문 수량 수정 하기
    // 127.0.0.1:8080/ROOT/api/order/updatecnt.json
    @PutMapping(value = "/updatecnt.json")
    public Map<String , Object> updatecntPUT(
        @RequestBody OrderDTO orderDTO,
        HttpServletRequest httpServletRequest
    ) {
        Map<String,Object> map = new HashMap<>();
            try {
                String userid = (String) httpServletRequest.getAttribute("username");
                orderDTO.setUserid(userid);
                int ret = orderMapper.updatecnt(orderDTO);
                if (ret == 1) {
                    map.put("status", 200);
                    map.put("result", "수량 수정 성공");
                } else {
                    map.put("status", 0);
                    map.put("result", "수량 수정 실패");
                }

            } catch (Exception e) {
                map.put("status", -1);
                map.put("result", e.getMessage());
            }
            return map;
        }

    // 고객 - 주문 n개 등록(TOKEN 필요) - 한개만 등록해도 상관 없음 오류 안남
    // 장바구니에 있는걸 체크박스로 주문하고 주문 하면 장바구니에 주문한 물품은 삭제됨.
    // 127.0.0.1:8080/ROOT/api/order/insertbatchorder.json
    // 장바구니에 담은 물품들을 한개 주문 or n개 주문 하고 싶을때 쓰면 됨.
    // 정보사항 : ino가 외래키이기 때문에 ino를 들고 올때 정보가 같이 옴.
    // [{"cno":? ,cnt":?,"ino":?},{"cno":? ,cnt":?,"ino":?},{"cno":? ,cnt":?,"ino":?}]
    @PostMapping(value="/insertbatchorder.json")
    public Map<String , Object> insertbatchorder(
        @RequestBody List<InsertOrderDTO> orders,
        HttpServletRequest httpServletRequest) {
        Map<String, Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
           
            for(int i=0;i<orders.size(); i++) {
                orders.get(i).setUserid(userid);
            }
            
            //
            int ret = orderMapper.insertbatchCustomerOrders(orders);
            if(ret == orders.size()){
                System.out.println(ret);
                // 주문 등록시 장바구니에 있던 물품 삭제
                int ret1 = orderMapper.deleteItemFromCartWhenOrder(orders);
                if(ret1 == ret){
                    // 결제시 주문번호 입력 위해 주문된 주문번호(ORDERCODE) 리턴해주기
                    Long ordercode = orderMapper.selectOrdercode(userid);

                    map.put("status", 200);
                    map.put("ordercode", ordercode);
                }
            }
        }
        catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }


    // 고객 - 주문 목록 조회하기(주문 완료 후에)
    // 상품명 , 가격 , 주문수량 , 주문 일자, 주문그룹핑번호
    // 고객이 주문한 물품내역들을 불러올때 쓰면 됨.
    // userid + start
    // 127.0.0.1:8080/ROOT/api/order/selectorderlist.json
    // param 값 받아야함
    @GetMapping(value = "/selectorderlist.json")
    public Map<String, Object> selectorder(
            HttpServletRequest httpServletRequest,
            @RequestParam(name = "start", defaultValue = "1") int start){
        Map<String, Object> map = new HashMap<>();
        try {
            int page = start*10 - 9 ;
            Map<String, Object> map1 = new HashMap<>();
            String userid = (String) httpServletRequest.getAttribute("username");
                map1.put("userid", userid);
                map1.put("start", page);
            List<OrderListDTO> order = orderMapper.selectOrderList(map1);
            long count = orderMapper.OrderCountList(userid);
            if(order != null){
                map.put("status", 200);
                map.put("orderlist", order);
                map.put("count", count);
            }
            else{
                map.put("status", 0);
                map.put("result", "정보가 없습니다");
            }
        }
        catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 고객 - (주문 완료된 상황에서)주문 취소하기 (TOKEN 필요)
    // PARAM값으로
    // 127.0.0.1:8080/ROOT/api/order/deleteorder.json?ono=
    @PostMapping(value = "/deleteorder.json")
    public Map<String, Object> deleteorder(
        HttpServletRequest httpServletRequest,
        @RequestParam(name = "ono") Long ono
    ){
        Map<String, Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
            Map<String, Object> map1 = new HashMap<>();
            map1.put("userid",userid);
            map1.put("ono", ono);
            int ret = orderMapper.deleteOrder(map1);

            if (ret == 1) {
                map.put("status", 200);
                map.put("result", "주문 취소 완료");
            } else {
                map.put("status", 0);  
                map.put("result", "주문 취소 실패");
            }

        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
}
