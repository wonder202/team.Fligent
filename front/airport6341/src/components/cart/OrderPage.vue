<template>
    <div class="container">
        <h3>주문하기</h3>
        <hr />

        <div class="order">
            <el-form :model="form" label-width="120px" style="width: 45%; margin:0 auto;">
                <h4>*구매자정보</h4>
                <br />
                <el-form-item label="닉네임:">
                    <span style="font-size: 25px;">{{ state.row1.nickname }}</span>님
                </el-form-item>
                <el-form-item label="연락처:">
                    <span style="font-size: 18px;">{{ state.row1.phone }}</span>
                </el-form-item>
                <el-form-item label="이메일:">
                    <span style="font-size: 18px;">{{ state.row1.userid }}</span>
                </el-form-item>
                <hr />
                <h4>*수령인정보</h4>
                <el-form-item label="받는 분">
                    <el-input type="text" v-model="name" :ref="el => { form[0] = el }" />
                </el-form-item>
                <el-form-item label="받는 분 연락처">
                    <el-input style="width:90px" v-model="phone" :ref="el => { form[1] = el }" />
                    <label>-</label>
                    <el-input style="width:100px" v-model="phone1" :ref="el => { form[6] = el }" />
                    <label>-</label>
                    <el-input style="width:100px" v-model="phone2" :ref="el => { form[7] = el }" />
                </el-form-item>
                <el-form-item label="주소">
                    <el-input type="text" id="postcode" v-model="postcode" placeholder="우편번호" style="width: 150px;"
                        :ref="el => { form[2] = el }" />
                    <el-input type="button" value="우편번호 찾기" style="width: 150px;" @click="search()" />
                    <el-input type="text" id="roadAddress" v-model="address" placeholder="도로명주소"
                        :ref="el => { form[3] = el }" />
                    <el-input type="text" id="jibunAddress" placeholder="지번주소" />
                    <span id="guide" style="color:#000;display:none"></span>
                    <el-input type="text" id="extraAddress" placeholder="참고항목" />
                    <el-input type="text" id="detailAddress" v-model="detailaddress" placeholder="상세주소"
                        :ref="el => { form[4] = el }" />
                </el-form-item>
                <el-form-item label="배송 요청사항">
                    <el-select v-model="state.comment" placeholder="업체 혹은 배송기사님께 요청사항" style="width: 250px;"
                        :ref="el => { form[5] = el }">
                        <el-option label="없음" value="없음" />
                        <el-option label="부재 시 문 앞에 둬주세요." value="부재 시 문 앞에 둬주세요." />
                        <el-option label="배송 전 연락 바랍니다." value="배송 전 연락 바랍니다." />
                    </el-select>
                </el-form-item>
                <br />
                <hr />
                <h4>*상품정보</h4>
                <div class="iteminfo" v-for="obj in state.row" :key="obj">
                    <br />
                    <el-form-item label="상품명">
                        <td style="border: 1px solid; width: 400px;">{{ obj.name }}</td>
                    </el-form-item>
                    <el-form-item label="가격/개당">
                        <span style="font-size: 25px; color: brown;">{{ obj.price }}</span>원
                    </el-form-item>
                    <el-form-item label="주문수량">
                        <el-input-number v-model="obj.cnt" style="margin-right:20px;" />
                        <el-button @click="handleCnt(obj.cnt, obj.cno)" style="margin-right:20px">수량수정</el-button>
                    </el-form-item>
                    <br />
                    <hr />
                </div>
                <h4>*주문금액</h4>
                <el-form-item label="총 결제금액">
                    <td style="border: 1px solid; width: 400px;"><span style="font-size: 25px; color: brown;">{{state.total}}</span>원</td>
                </el-form-item>
                <!-- <el-form-item label="결제방법">
                    <el-radio-group>
                        <el-radio label="계좌이체" value="" />
                        <el-radio label="카드결제" value="" />
                    </el-radio-group>
                </el-form-item> -->
            </el-form>
        </div>
        <div class="butt">
            <el-button round @click="handleCart()" type="info" plain
                style="height: 40px; margin-right: 200px; font-size: 25px; font-weight: 600; color: black;">장바구니로 돌아가기
            </el-button>
            
            <img src="../../assets/pay/KGinicis.png" @click="iamportPay('html5_inicis')" style="width: 130px; padding: 10px; cursor:pointer;" />
            <img src="../../assets/pay/payment_icon_yellow_medium.png" @click="iamportPay('kakaopay')" style="width: 130px; padding: 10px; cursor:pointer;" />
            <img src="../../assets/pay/logo-toss-pay.png" @click="iamportPay('tosspay')" style="width: 130px; padding: 10px; cursor:pointer;" />
            
            <!-- <el-button round @click="iamportPay('html5_inicis')" type="primary" -->
                <!-- style="height: 40px; font-size: 25px; font-weight: 600;">카드결제</el-button> -->
            <!-- <el-button round @click="iamportPay('kakaopay')" type="primary" -->
                <!-- style="height: 40px; font-size: 25px; font-weight: 600;">카카오페이</el-button> -->
            <!-- <el-button round @click="iamportPay('tosspay')" type="primary" -->
                <!-- style="height: 40px; font-size: 25px; font-weight: 600;">토스페이</el-button> -->
                
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import { onMounted } from '@vue/runtime-core'
import { reactive, ref, toRefs } from '@vue/reactivity';
import { useRoute, useRouter } from 'vue-router';

export default {
    setup() {
        const router = useRouter();
        const route = useRoute();
        const form = ref([]);

        const state = reactive({
            token   : sessionStorage.getItem("token"),
            cno     : [],
            // cno     : route.query.cno,
            row     : '',
            row1    : '',
            name    : '',
            phone   : '',
            phone1  : '',
            phone2  : '',
            postcode: '',
            address : '',
            detailaddress: '',
            comment : '',
            cnt     : '',
            ino     : '',
            itemprice: [],
            total   : '',
            ordercode: '', // 주문DB저장 완료시 리턴 되는 주문번호(ordercode)
            orderret : false

        });

        //장바구니정보 조회
        const handleData = async () => {
            let url = `/fligent/api/order/selectitem.json`;
            const headers = {
                "Content-Type": "application/json",
                "token": state.token
            };
            var params = new URLSearchParams();
            for (let i = 0; i < state.cno.length; i++) {
                params.append("cno", state.cno[i]);
            }
            const { data } = await axios.get(url, { params: params, headers: headers });
            if (data.status === 200) {
                state.row = data.item;
                for (let i = 0; i < state.row.length; i++) {
                    state.itemprice[i] = state.row[i].tot
                }
                state.total = 0;
                for (let i = 0; i < state.itemprice.length; i++) {
                    state.total += state.itemprice[i];
                }
            }
        };

        //고객정보 조회
        const handleData1 = async () => {
            const url = `/fligent/api/order/selectmemberone.json`;
            const headers = {
                "Content-Type": "application/json",
                "TOKEN": state.token
            };
            const { data } = await axios.get(url, { headers });

            if (data.status === 200) {
                state.row1 = data.member
            }
        };

        // 주소 저장
        const handleAddress = async () => {
            const url = `/fligent/api/order/insertcustomeraddress.json`;
            const headers = {
                "Content-Type": "application/json",
                "token": state.token
            };
            const body = {
                name: state.name,
                phone: `${state.phone}-${state.phone1}-${state.phone2}`,
                postcode: state.postcode,
                address: state.address,
                comment: state.comment,
                detailaddress: state.detailaddress
            }
            const { data } = await axios.post(url, body, { headers });
            // console.log(data);
            if (data.status == 200) {
                state.orderret = true;
            } else {
                alert('오류발생! 다시 시도해주세요')
            }
        };

        // 물품 수량 변경
        const handleCnt = async (cnt, obj) => {
            const url = `/fligent/api/order/updatecnt.json`;
            const headers = {
                "Content-Type": "application/json",
                "token": state.token
            };
            const body = {
                cno: obj,
                cnt: cnt
            }
            const { data } = await axios.put(url, body, { headers });
            // console.log(data);
            if (data.status === 200) {
                alert('수정 되었습니다.')
                handleData();
            } else {
                alert('수정 실패하였습니다.')
            }
        };

        // 주문
        const handleOrder = async () => {
            // if (state.name === '') {
            //     alert('받는 분 성함을 입력하세요.')
            //     form.value[0].focus();
            //     return false;
            // }
            // if (state.phone === '') {
            //     alert('받는 분 연락처를 입력하세요.')
            //     form.value[1].focus();
            //     return false;
            // }
            // if (state.phone1 === '') {
            //     alert('받는 분 연락처를 입력하세요.')
            //     form.value[6].focus();
            //     return false;
            // }
            // if (state.phone2 === '') {
            //     alert('받는 분 연락처를 입력하세요.')
            //     form.value[7].focus();
            //     return false;
            // }
            // if (state.postcode === '') {
            //     alert('우편번호를 입력하세요.')
            //     form.value[2].focus();
            //     return false;
            // }
            // if (state.address === '') {
            //     alert('주소를 입력하세요.')
            //     form.value[3].focus();
            //     return false;
            // }
            // if (state.detailaddress === '') {
            //     alert('상세주소를 입력하세요.')
            //     form.value[4].focus();
            //     return false;
            // }
            // if (state.comment === '') {
            //     alert('요청사항을 입력하세요.')
            //     form.value[5].focus();
            //     return false;
            // }
            
            const url = `/fligent/api/order/insertbatchorder.json`;
            const headers = {
                "Content-Type": "application/json",
                "token": state.token
            };
            let body = []
            for (let i = 0; i < state.row.length; i++) {
                body.push({
                    "cno": state.row[i].cno,
                    "cnt": state.row[i].cnt,
                    "ino": state.row[i].ino
                });
            }
            const {data} = await axios.post(url, body, { headers });

            if (data.status === 200) {
                state.ordercode = data.ordercode
                await handleAddress();
                if (state.orderret === true) {
                    // router.push({path:'/ordersuccess'});
                }
            }
            else {
                alert('주문에 실패하였습니다.');
            }
        };

        onMounted(() => {
            if (Array.isArray(route.query.cno)) {
                for (let i = 0; i < route.query.cno.length; i++) {
                    state.cno.push(route.query.cno[i])
                }
            }
            else {
                state.cno.push(route.query.cno)
            }
            handleData();
            handleData1();

            let scripts = [
                // 주소 찾기 라이브러리
                "//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js",
                // 제이쿼리 사용을 위한 라이브러리
                "https://code.jquery.com/jquery-1.12.4.min.js",
                // 아임포트 라이브러리
                // "https://cdn.iamport.kr/js/iamport.payment-1.2.0.js"
            ];
            for (let i = 0; i < scripts.length; i++) {
                let script = document.createElement('script');
                script.setAttribute('type', 'text/javascript');
                script.setAttribute('src', scripts[i]);
                script.async = true;
                document.getElementsByTagName('head')[0].appendChild(script);
            }

        });

        // 카카오 주소검색 서비스
        const search = () => {
            new window.daum.Postcode({
                oncomplete: (data) => { //function이 아니라 => 로 바꿔야한다.
                    // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                    // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                    // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                    var roadAddr = data.roadAddress; // 도로명 주소 변수
                    var extraRoadAddr = ''; // 참고 항목 변수

                    // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                    // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                    if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                        extraRoadAddr += data.bname;
                    }
                    // 건물명이 있고, 공동주택일 경우 추가한다.
                    if (data.buildingName !== '' && data.apartment === 'Y') {
                        extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                    }
                    // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                    if (extraRoadAddr !== '') {
                        extraRoadAddr = ' (' + extraRoadAddr + ')';
                    }

                    // 우편번호와 주소 정보를 해당 필드에 넣는다.
                    document.getElementById('postcode').value = data.zonecode;
                    document.getElementById("roadAddress").value = roadAddr;
                    document.getElementById("jibunAddress").value = data.jibunAddress;

                    //다음 api에서 받아온 데이터를 내가 만든 변수에 넣어주기위한 로직
                    state.postcode = data.zonecode;
                    state.address = roadAddr;

                    // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                    if (roadAddr !== '') {
                        document.getElementById("extraAddress").value = extraRoadAddr;
                    } else {
                        document.getElementById("extraAddress").value = '';
                    }

                    var guideTextBox = document.getElementById("guide");
                    // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
                    if (data.autoRoadAddress) {
                        var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                        guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                        guideTextBox.style.display = 'block';

                    } else if (data.autoJibunAddress) {
                        var expJibunAddr = data.autoJibunAddress;
                        guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                        guideTextBox.style.display = 'block';
                    } else {
                        guideTextBox.innerHTML = '';
                        guideTextBox.style.display = 'none';
                    }
                }
            }).open();
        }


        // 결제 API서비스(아임포트) 
        // const iamportPay = async (paycode) => {
        //     // 결제 전 유효성 검사
        //     if (state.name === '') {
        //         alert('받는 분 성함을 입력하세요.')
        //         form.value[0].focus();
        //         return false;
        //     }
        //     if (state.phone === '') {
        //         alert('받는 분 연락처를 입력하세요.')
        //         form.value[1].focus();
        //         return false;
        //     }
        //     if (state.phone1 === '') {
        //         alert('받는 분 연락처를 입력하세요.')
        //         form.value[6].focus();
        //         return false;
        //     }
        //     if (state.phone2 === '') {
        //         alert('받는 분 연락처를 입력하세요.')
        //         form.value[7].focus();
        //         return false;
        //     }
        //     if (state.postcode === '') {
        //         alert('우편번호를 입력하세요.')
        //         form.value[2].focus();
        //         return false;
        //     }
        //     if (state.address === '') {
        //         alert('주소를 입력하세요.')
        //         form.value[3].focus();
        //         return false;
        //     }
        //     if (state.detailaddress === '') {
        //         alert('상세주소를 입력하세요.')
        //         form.value[4].focus();
        //         return false;
        //     }
        //     if (state.comment === '') {
        //         alert('요청사항을 입력하세요.')
        //         form.value[5].focus();
        //         return false;
        //     }
            

        //     // console.log('paycode => ', paycode)
        //     // await handleOrder();
        //     // console.log('결제 결과 true ? => ',state.orderret)

        //     // if (state.orderret === true) {
        //         var IMP = window.IMP;
        //         IMP.init("imp54605428");
        //             // console.log('주문번호=> ', state.ordercode)
        //             // console.log('주문자 이름=> ', state.name)
        //             // console.log('주문 총 금액=> ', state.total)
        //             // console.log('주문자 연락처=> ', state.row1.phone)
        //             // console.log('주문자 주소=> ', state.address + state.detailaddress)
        //             // console.log('주문자 우편번호=> ', state.postcode)
        //         // IMP.request_pay(param, callback) //결제창 호출
        //         IMP.request_pay({ //param
        //             pg: paycode,
        //             pay_method: "card",
        //             merchant_uid: state.ordercode,
        //             name: state.name,
        //             amount: state.total,
        //             buyer_email: state.row1.userid,
        //             buyer_name: state.name,
        //             buyer_tel: state.row1.phone,
        //             buyer_addr: state.address + state.detailaddress,
        //             buyer_postcode: state.postcode,
        //             popup : true
        //         }, 
        //         async function (rsp) { // callback
        //             //성공할 때 실행 될 콜백 함수
        //             // console.log('결제정보 ===> ', window.IMP.request_Pay)

        //             if (rsp.success == true) {
        //                 await handleAddress();
        //                 await handleOrder();

        //                 if(state.orderret === true){
        //                     // console.log('성공')
        //                     var msg = '결제가 완료되었습니다.';
        //                     // msg += '고유ID : ' + result_success.imp_uid;
        //                     // msg += '상점 거래ID : ' + result_success.merchant_uid;
        //                     // msg += '결제 금액 : ' + result_success.paid_amount;
        //                     // msg += '카드 승인번호 : ' + result_success.apply_num;
        //                     alert(msg);
        //                     router.push({path:'/ordersuccess'});
        //                 }
        //                 else 
        //                 {
        //                     alert('주문에 실패하였습니다. 관리자에게 문의해주세요');
        //                     router.push({path:'/home'});
        //                 }
        //             }
        //             //실패시 실행 될 콜백 함수
        //             else {
        //                 var msg1 = '결제에 실패하였습니다.';
        //                 msg1 += '에러내용 : ' + rsp.error_msg;
        //                 alert(msg1);
        //                 router.push({path:'/cart'});
        //                 // router.go();
        //             }
        //         })
        //         window.IMP
        //     // }
        // }


        // const tossPay = async (paycode) => {
        //     console.log('paycode => ', paycode)
        //     console.log('11111111111111')
        //     await handleOrder();
        //     console.log('결제 결과 true ? => ',state.orderret)
        //     if (state.orderret === true) {
        //         // console.log('22222222222')
        //         var IMP = window.IMP;
        //         IMP.init("imp54605428");
        //             console.log('주문번호=> ', state.ordercode)
        //             console.log('주문자 이름=> ', state.name)
        //             console.log('주문 총 금액=> ', state.total)
        //             console.log('주문자 연락처=> ', state.row1.phone)
        //             console.log('주문자 주소=> ', state.address + state.detailaddress)
        //             console.log('주문자 우편번호=> ', state.postcode)
        //         // IMP.request_pay(param, callback) //결제창 호출
        //         IMP.request_pay({ //param
        //             pg: paycode,
        //             pay_method: "card",
        //             merchant_uid: state.ordercode,
        //             name: state.name,
        //             amount: state.total,
        //             buyer_email: state.row1.userid,
        //             buyer_name: state.name,
        //             buyer_tel: state.row1.phone,
        //             buyer_addr: state.address + state.detailaddress,
        //             buyer_postcode: state.postcode,
        //             // m_redirect_url : '{http://localhost:9090/}' 
        //             m_redirect_url : '{http://localhost:9090/ordersuccess}' 
        //             // m_redirect_url : router.push({path:'/ordersuccess'})
        //         }, 
        //         function (rsp) { // callback
        //             //성공할 때 실행 될 콜백 함수
        //             // 토스페이는 콜백되지 않음! m_redirect_url로 이동한다
        //             // console.log('결제정보 ===> ', window.IMP.request_Pay)

        //             console.log('결제완료!!!!!!!!!!!11', rsp)
        //             if (rsp.success == true) {
        //                 var msg = '결제가 완료되었습니다.';
        //                 // msg += '고유ID : ' + result_success.imp_uid;
        //                 // msg += '상점 거래ID : ' + result_success.merchant_uid;
        //                 // msg += '결제 금액 : ' + result_success.paid_amount;
        //                 // msg += '카드 승인번호 : ' + result_success.apply_num;
        //                 alert(msg);
        //                 router.push({path:'/ordersuccess'});
        //             }
        //             //실패시 실행 될 콜백 함수
        //             else {
        //                 var msg1 = '결제에 실패하였습니다.';
        //                 msg1 += '에러내용 : ' + rsp.error_msg;
        //                 alert(msg1);
        //             }
        //         })
        //         window.IMP
        //     }
        // }

        const handleCart = () => {
            router.push({ path: '/cart' });
        };

        return {
            form,
            state,
            ...toRefs(state),
            handleCart,
            handleOrder,
            search,
            handleCnt,
            // iamportPay,
            // tossPay
        }
    }
}
</script>

<style lang="css" scoped>
.container {
    /* border:1px solid #cccccc; */
    text-align: center;
    width: 80%;
}

.order {
    /* border:1px solid #cccccc; */
    text-align: center;
    width: 100%;
}

.order h4 {
    font-weight: 600;
    margin-top: 30px;
    margin-bottom: 30px;
}

.butt {
    width: 100%;
    /* border:1px solid #cccccc; */
    margin: 0 auto;
    margin-top: 50px;
    margin-bottom: 150px;
}

.container h3 {
    font-family: 'MapoFlowerIsland';
    font-weight: 600;
    font-size: 50px;
    margin-bottom: 50px;
    margin-top: 50px;
}
/* @font-face {
  font-family: 'MapoFlowerIsland';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/MapoFlowerIslandA.woff') format('woff');
  font-weight: normal;
  font-style: normal;
} */
</style>