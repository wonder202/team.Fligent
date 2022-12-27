<template>
    <div>
        <div class="container">
            <h3>회원가입</h3>
            <div style="font-size: 13px; margin-bottom: 20px;">*표시된 항목은 필수입력입니다.</div>
            <div>
                <div v-if="!idValid" style="font-size: 15px; color: #cccccc;">
                    이메일 형식으로 입력해주세요.
                </div>
                <label class="lbl">*아이디</label>
                <el-input type="email" v-model="userid" @keyup="handleIDCheck" placeholder="abc@example.com" required style="width:300px" :ref = "el => {form[0] = el}"/>
                <br />
                <label v-html="state.idcheck"></label>
                </div>
            <div>
              <label class="lbl">*암호</label>
              <el-input type="password" style="width:300px" v-model="userpw" :ref = "el => {form[1] = el}" />
            </div><br />
            <div>
                <label class="lbl">*암호확인</label>
                <el-input type="password" style="width:300px" v-model="userpw1" :ref = "el => {form[2] = el}" />
            </div><br />
            <div>
                <div class="demo-date-picker">
                    <label style="width:90px; margin-right: px;">*생년월일</label>
                    <el-date-picker type="date" v-model="birth" 
                        placeholder="생년월일을 고르세요"
                        style="width:300px; float: right;"
                        format="YYYY/MM/DD"
                        value-format="YYYYMMDD"
                        :ref = "el => {form[3] = el}" />
                </div>
            </div><br />
            <div>
                <label class="lbl">*연락처</label>
                <el-input style="width:90px" v-model="phone" :ref = "el => {form[4] = el}"/>
                <label>-</label>
                <el-input style="width:100px" v-model="phone1" :ref = "el => {form[5] = el}"/>
                <label>-</label>
                <el-input style="width:100px" v-model="phone2" :ref = "el => {form[6] = el}"/>
            </div><br />
            <div>
                <label class="lbl">*닉네임</label>
                <el-input style="width:300px" v-model="nickname" @keyup="handleNicknameCheck" :ref = "el => {form[7] = el}" />
                <br />
                <label v-html="state.nicknamecheck"></label>
            </div>

            <div>
                <label class="lbl"> 프로필 사진</label> 
                <div style="margin-top:10px; width: 60%; margin: auto;" >
                    <img style="width:250px; height: 200px;" :src="state.fileurl" />
                    <input type="file" @change="handleFile($event)" style="margin-left: 70px;" :ref = "el => {form[8] = el}"/>
                </div>
                <br/><br/> 
            </div><br />
            <div>
                <label style="width: 150px; font-size: 18px;">즐겨찾는 공항 ></label>
                <input type="radio" id="GMP" v-model="airport" value="GMP">
                <label for="GMP" style="margin-right: 20px;">김포공항</label>

                <input type="radio" id="ICN" v-model="airport" value="ICN">
                <label for="ICN" style="margin-right: 20px;">인천공항</label>

                <input type="radio" id="PUS" v-model="airport" value="PUS">
                <label for="PUS" style="margin-right: 20px;">김해공항</label>

                <input type="radio" id="CJU" v-model="airport" value="CJU">
                <label for="CJU" style="margin-right: 20px;">제주공항</label>
            </div>
            <div>
                <el-input type="hidden" v-model="role" />
            </div>
            <br />
            <div style="margin-top:50px; margin-bottom: 100px;">
                <el-button color="#F76868" link @click="handleJoin()" style="font-size: 20px; font-weight: 600;">가입하기</el-button>
                <el-button link @click="handleHome()">
                    <span class="bi bi-house fs-4"></span>
                </el-button>
                <el-button color="#F76868" link @click="handleLogin()" style="font-size: 20px; font-weight: 600;">로그인</el-button>
            </div>
        </div>
    </div>
</template>

<script>
import { reactive, ref, toRefs } from '@vue/reactivity'
import axios from 'axios';
import { useRouter } from 'vue-router';
export default {
    computed: {
        idValid () {
            return /^[a-z0-9_+.-]+@([a-z0-9-]+\.)+[a-z0-9]{2,4}$/.test(this.state.userid)
        }
    },
    setup () {
        const router = useRouter();
        const form = ref([]);
        const state = reactive({
            file     : null,
            userid   : '',
            userpw   : '',
            userpw1  : '',
            role     : 'CUSTOMER',
            birth    : '',
            nickname : '',
            phone    : '',
            phone1   : '',
            phone2   : '',
            idcheck  : '',
            nicknamecheck : '',
            airport       : 'GMP',
            fileurl       : require('../../assets/defaultprofile.png'),
            parsebirth    : 0,
        });

        const handleNicknameCheck = async() => {
            if(state.nickname.length > 0){
                const url =`/fligent/api/member/nicknamecheck.json?nickname=${state.nickname}`;
                const headers = {"Content-Type":"application/json"};
                const { data } = await axios.post(url, {headers});
                // console.log(data);
                if(data.status === 200) {
                    state.nicknamecheck = '<font color="green">사용가능</font>';
                    }
                else {
                    state.nicknamecheck = '<font color="red">사용불가</font>';
                }
            }
        };

        const handleIDCheck = async() => {
            if(state.userid.length > 0) {
                const url = `/fligent/api/member/idcheck.json?userid=${state.userid}`;
                const headers = {"Content-Type":"application/json"};
                const { data } = await axios.get(url,{headers});
                // console.log(data);
                if(data.status === 200) {
                    if(data.result === true){
                        state.idcheck = '<font color="red">사용불가</font>';
                    }
                    else {
                        state.idcheck = '<font color="green">사용가능</font>';
                    }
                }
            }
        };

        const handleHome = () => {
            router.push({path:'/'});
        };

        const handleLogin = () => {
            router.push({path:'/memberlogin'});
        };
            
        const handleJoin = async() => {
            if(state.userid ===''){
                alert('아이디 입력하세요.')
                form.value[0].focus();
                return false;
            }

            if(state.userpw === ''){
                alert("암호를 입력하세요")
                form.value[1].focus();
                return false;
            }

            if(state.userpw1 === ''){
                alert("암호를 확인하세요")
                form.value[2].focus();
                return false;
            }

            if(state.birth === ''){
                alert("생일을 입력하세요")
                form.value[3].focus();
                return false;
            }
            
            if(state.phone === ''){
                alert("연락처을 입력하세요")
                form.value[4].focus();
                return false;
            }
            
            if(state.phone1 === ''){
                alert("연락처을 입력하세요")
                form.value[5].focus();
                return false;
            }
            
            if(state.phone2 === ''){
                alert("연락처을 입력하세요")
                form.value[6].focus();
                return false;
            }

            if(state.nickname === ''){
                alert("닉네임을 입력하세요")
                form.value[7].focus();
                return false;
            }

            const url = `/fligent/api/member/join.json`;
            const headers = {"Content-Type":"application/json"};
            const body = {
                userid :state.userid,
                userpw:state.userpw,
                role:state.role,
                birth:state.birth,
                nickname:state.nickname,
                phone:`${state.phone}-${state.phone1}-${state.phone2}`,
                airportname:state.airport
            }
            const today = new Date(); // 오늘 날짜를 가져옴
            const yearNow = String(today.getFullYear()); //년도 변수 저장
            const monthNow = String(today.getMonth() + 1); //month 변수 저장
            const dayNow = String(today.getDate()); //일자 변수 저장
            let dayynow = "";
            //일자 불러올때 형식 변경을 위한 로직
            //ex) 202212'2' => 202212'02'
            if(dayNow.length == 1){
                dayynow = "0" + dayNow;
            }
            else{
                dayynow = dayNow
            }
            const todayy = Number(yearNow + monthNow + dayynow); //ex)20221202
            //사용자에 의해 입력된 birth변수를 문자열에서 숫자로 변환
            state.parsebirth = parseInt(state.birth);
            //입력한 날짜가 현재 실제 날짜보다 클 경우 리턴
            if(state.parsebirth > todayy){
                alert('오늘 이후의 날짜는 선택할 수 없습니다.')
                return false;
            }
            const { data } = await axios.post(url, body, {headers});
            // console.log(data);
            if(data.status === 200){
                handleIMGInsert();
                handleAirport();
                alert('회원가입완료');
                router.push({path:'/memberlogin'});
            }
        };
        //state.file의 변수에 수동으로 파일 정보를 넣기
        const handleFile = (e) =>{
            // const reader = new FileReader();
            state.file = null;
            if(e.target.files.length > 0) {
                state.file = e.target.files[0];
                if(e.target.files && e.target.files[0]){
                    state.fileurl = URL.createObjectURL(e.target.files[0])
                    // 위는 바꾼 로직, 밑은 처음에 만든 로직
                    // reader.onload = function(e) {
                    //     // document.createElement("preview");
                    //     document.getElementById('preview').src = e.target.result;
                    // }
                    // reader.readAsDataURL(e.target.files[0]);
                }
            }
            else if(e.target.files.length == 0){
                state.fileurl = require('../../assets/defaultprofile.png');
            }
        };
        
        const handleIMGInsert = async() => {
            const url = `/fligent/api/memberimage/insert.json`;
            const headers ={"Content-Type":"multipart/form-data"};
            // 이미지가 있는 경우  body 정보 담기
            let body = new FormData();
                body.append("member.userid", state.userid);                                            
                body.append("file", state.file);
            const { data }  = await axios.post(url, body, {headers});
            console.log(data);
        };

        const handleAirport = async() => {
            const url =`/fligent/api/customer/mypage/myairfavorite.json?airportname=${state.airport}`;
            const headers = {"Content-Type":"application/json"};
            const body = {
                airportname :state.airport
            };
            const { data } = await axios.put(url, body, {headers});
            console.log(data);
        };

        return {
            form,
            state, 
            ...toRefs(state),
            handleIDCheck,
            handleNicknameCheck,
            handleFile,
            handleIMGInsert,
            handleJoin,
            handleLogin,
            handleHome,
        };
    }
}
</script>

<style lang="css" scoped>
.demo-date-picker {
    display: flex;
    width: 395px;
    flex-wrap: wrap;
    /* border: 1px solid #cccccc; */
    margin:0 auto;
}
.container {
    width:50%;
    margin:0 auto;
    text-align:center;
    /* border: 1px solid #cccccc; */
}
.container h3 {
    margin-left: 50px;
    font-family: 'MapoFlowerIsland';
    font-size: 40px;
    padding: 35px;
}
.lbl {
    display: inline-block;
    width:90px;
    margin-top: 10px;
    /* border: 1px solid #cccccc; */
}
/* @font-face {
    font-family: 'S-CoreDream-3Light';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_six@1.2/S-CoreDream-3Light.woff') format('woff');
    font-weight: 600;
    font-style: normal;
} */
* {
font-family: 'S-CoreDream-3Light';
}
/* @font-face {
    font-family: 'MapoFlowerIsland';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/MapoFlowerIslandA.woff') format('woff');
    font-weight: normal;
    font-style: normal;
} */

</style>