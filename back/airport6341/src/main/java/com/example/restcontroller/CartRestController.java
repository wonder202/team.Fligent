
package com.example.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.CartDTO;
import com.example.mapper.CartMapper;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/cart")
@RequiredArgsConstructor
public class CartRestController {
    
    final CartMapper cartMapper;

    // 장바구니에 담기 (TOKEN 필요)
    // 127.0.0.1:8080/ROOT/api/cart/insertincart.json
    // 장바구니에 담을때 쓰면 됨.
    // {"cnt":3,"ino":3}
    @PostMapping(value = "/insertincart.json")
    public Map<String,Object> insertincartPOST(
        @RequestBody CartDTO cart,
        HttpServletRequest httpServletRequest
    ) {
        // System.out.println(cart.toString());
        Map<String,Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
            cart.setUserid(userid);
            
            int ret = cartMapper.insertItemInCart(cart);
            if (ret == 1) {
                map.put("status", 200);
                map.put("result", "장바구니 담기 성공");
            } else {
                map.put("status", 200);
                map.put("result", "장바구니 담기 실패");
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 장바구니 목록 조회하기(TOKEN 필요)
    // 장바구니 목록이 너무 비어보여서 페이지 네이션 넣을건지 프론트와 상의
    // 정렬 같은 경우는 regdate로 통일
    // 장바구니에 담은 아이템 전체 목록을 들고 온다
    // 상품명 , 개당 가격 , 주문수량 , 주문 일자 , 전체 가격
    // 127.0.0.1:8080/ROOT/api/cart/selectcartlist.json
    @GetMapping(value="/selectcartlist.json")
    public Map<String,Object> selectcartlistGET(
        HttpServletRequest httpServletRequest,
        @RequestParam(name = "start", defaultValue = "1") int start
    ){
        Map<String,Object> map = new HashMap<>();
        try {

            int page = start*10 - 9 ;
            Map<String,Object> map1 = new HashMap<>();
            String userid = (String) httpServletRequest.getAttribute("username");
            map1.put("userid", userid);
            map1.put("start", page);


            List<CartDTO> list = cartMapper.SelectCartList(map1); 
            long count = cartMapper.CartListCount(map1);  // 장바구니 목록 아이템 개수
            if (list != null) {
                map.put("status", 200);
                map.put("result", "조회 성공");
                map.put("list", list);
                map.put("count", count);
            } else {
                map.put("status", 0);
                map.put("result", "조회 실패");
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 수량 불러온뒤 수량 수정
    // input으로 수량 수정 후에 수정 버튼 누르면 수정 성공
    // 127.0.0.1:8080/ROOT/api/cart/setquantity.json
    // {"cno":13,"cnt":1}
    @PutMapping(value="/setquantity.json")
    public Map<String,Object> setquantityPUT(
        @RequestBody CartDTO cart,
        HttpServletRequest httpServletRequest
    ) {
        Map<String,Object> map = new HashMap<>();
            try {
                String userid = (String) httpServletRequest.getAttribute("username");
                cart.setUserid(userid);
                
                int ret = cartMapper.updateCntFromCartItem(cart);
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
    
    // 장바구니 일괄 삭제
    // 127.0.0.1:8080/ROOT/api/cart/deletebatchcart.json
    // form-data
    @PostMapping(value="/deletebatchcart.json")
    public Map<String,Object> deletebatchcartPOST(
        HttpServletRequest httpServletRequest,
        @RequestParam(name = "cno") List<Long> cno
    ){
        Map<String,Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
            Map<String,Object> map1 = new HashMap<>();
            map1.put("userid",userid);
            map1.put("list", cno);
            
            int ret = cartMapper.deletebatchItemFromCart(map1);
            if(ret == cno.size()){
                map.put("status", 200);
                map.put("result", "수량 삭제 성공");
            }
            else{
                map.put("status", 0);
                map.put("result", "수량 삭제 실패");
            }
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }

    // 장바구니 아이템 삭제
    // 127.0.0.1:8080/ROOT/api/cart/deletecart.json?cno=
    @PostMapping(value="/deletecart.json")
    public Map<String,Object> deletecartPOST(
        HttpServletRequest httpServletRequest,
        @RequestParam(name = "cno") Long cno
    ){
        Map<String,Object> map = new HashMap<>();
        try {
            String userid = (String) httpServletRequest.getAttribute("username");
            Map<String,Object> map1 = new HashMap<>();

            map1.put("userid",userid);
            map1.put("cno", cno);

            cartMapper.deleteItemFromCart(map1);
                map.put("status", 200);
                map.put("result", "수량 삭제 성공");
        } catch (Exception e) {
            map.put("status", -1);
            map.put("result", e.getMessage());
        }
        return map;
    }
}
