<template>
    <div class="container">    
        <!-- {{state}} -->
        <h3>로그인</h3><br />

        <div>
            <div v-if="!idValid" style="font-size: 15px; color: #cccccc;">
                이메일 형식으로 입력해주세요.
            </div>
            <label class="lbl">아이디</label>
            <el-input v-model="state.userid" placeholder="1234@naver.com" style="width:300px" :ref = "el => {form[0] = el}" />
        </div><br />
        <div>
            <label class="lbl">암호</label>
            <el-input type="password" @keyup.enter="handleLogin()" style="width:300px" placeholder="****" v-model="state.userpw" autocomplete="off" :ref = "el => {form[1] = el}" />
        </div><br />
        <div style="margin-top:20px;">
            <el-button link @click="handleHome()">
                <span class="bi bi-house fs-4"></span>
            </el-button>
            <el-button color="#F76868" link @click="handleLogin" style="font-size: 20px; font-weight: 600; cursor:pointer;">로그인</el-button>
            <br/>
            <br/>
            <img style="width: 40%; margin-left: 40px; margin-right: 20px; cursor:pointer;"
                class="kakao_btn"
                src="../../assets/login/kakao_login_medium_narrow.png"
                @click="loginWithKakao"
            />
            <img style="width: 40%; margin-right: 20px; cursor:pointer;" src="../../assets/images/naver_login_longW.png" @click="naverLogin" />
            <!-- <div style="border: 1px solid #cccccc;"> -->
            <!-- 네이버 로그인 버튼 노출 영역 -->
            <!-- </div> -->
        </div>
            <hr />
        <div>
            <el-button text class="button" @click="handlePw" style="font-weight: 600; font-size: 15px; cursor:pointer;"> 비밀번호 찾기</el-button>
            <el-button text class="button" @click="handleJoin" style="font-weight: 600; font-size: 15px; cursor:pointer;"> 회원가입</el-button>
            <el-button text class="button" @click="handleAdLogin" style="font-weight: 600; font-size: 15px; cursor:pointer;"> 관리자 로그인</el-button>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import {  reactive, ref} from '@vue/reactivity';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
export default {
    computed: {
        idValid () {
            return /^[a-z0-9_+.-]+@([a-z0-9-]+\.)+[a-z0-9]{2,4}$/.test(this.state.userid)
        }
    },
    name: "LoginKakao",
        methods: {
            loginWithKakao() {
                const params = {
                // redirectUri: 'http://localhost:9090/kakao_callback', // vue 서버 구동시 리다이렉트 URI
                redirectUri: 'http://1.234.5.158:28080/fligent/kakao_callback', // 톰캣 배포시 리다이렉트 URI
            };
            window.Kakao.Auth.authorize(params);
            },
        },
    setup () {
        const router = useRouter();
        const store = useStore();
        const form = ref([]);
        
        const state = reactive({
            userid  : '',
            userpw  : '',
            token   : '',
        
            // 네이버 api 로그인용 변수
            naverClientId   : 'Wg77gLgzjURrRdBDfnhx',
            // callbackUrl  : 'http://localhost:9090/naver_callback', // vue 서버 구동시 리다이렉트 URI
            callbackUrl     : 'http://1.234.5.158:28080/fligent/naver_callback', // 톰캣 배포시 리다이렉트 URI
            states          : 'airport6341' // csrf 공격을 방지하기 위해 애플리케이션에서 생성한 상태 토큰값으로 URL 인코딩을 적용한 값을 사용
                    
        });
        
        const handleHome = () => {
            router.push({path:'/'});
        };

        const handlePw = () => {
            router.push({path:'/findpassword'});
        };

        const handleJoin = () => {
            router.push({path:'/memberjoin'});
        };

        const handleAdLogin = () => {
            router.push({path:'/adminlogin'});
        };


        const handleLogin = async() => {
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
            const url = `/fligent/api/member/login.json`;
            const headers = {"Content-Type":"application/json"};
            const body = {
                userid  : state.userid,
                userpw  : state.userpw,
                role    : 'CUSTOMER'
            }
            const {data} = await axios.post(url, body, {headers});
            // console.log(data);
            if(data.status === 200){
                // 토큰 생성
                sessionStorage.setItem("token", data.result);
                state.token = data.result;

                const url = sessionStorage.getItem("CURR_URL");
                const query = JSON.parse(sessionStorage.getItem("CURR_QRY"));

                store.commit('setLogged', true);
                handleImg();
                
                alert('로그인 되었습니다.');
                
                router.push({path:url, query:query});
                // router.go({path:url, query:query});
            } else {
                alert('아이디 또는 암호가 올바르지 않습니다');
                router.push({path:'/memberlogin'});
            }
        };



        // 네이버 로그인을 위한 url 이동 
        const naverLogin = async() => {
            // console.log("콜백주소 ==> " , state.callbackUrl)

            const url = `https://nid.naver.com/oauth2.0/authorize?response_type=code&client_id=${state.naverClientId}&redirect_uri=${state.callbackUrl}&state=${state.states}`;
            // const url = `/oauth2.0/authorize?response_type=code&client_id=${state.naverClientId}&redirect_uri=${state.callbackUrl}&state=${state.states}`;

            // 네이버 api 로그인용 변수
            // naverClientId : 'Wg77gLgzjURrRdBDfnhx',
            // // callbackUrl : 'http://localhost:9090/naver_callback', // vue 서버 구동시 리다이렉트 URI
            // callbackUrl : 'http://1.234.5.158:28080/fligent/naver_callback', // 톰캣 배포시 리다이렉트 URI
            // states : 'airport6341' // csrf 공격을 방지하기 위해 애플리케이션에서 생성한 상태 토큰값으로 URL 인코딩을 적용한 값을 사용


            // console.log("==================url====================");
            // console.log(url);
  
            window.location.href=url;
            // router.push({path:'/naver_callback'});
        };      

        const handleImg = async() => {
            if(state.token == null){return;}
            const url =`/fligent/api/customer/selectmemberimglist.json`;
            const headers = {
                "Content-Type":"application/json",
                "token" : state.token
            };
            const {data} = await axios.get(url, {headers});
            // console.log(data.result)
            if(data.status === 200) {
                store.commit('setImageurl', data.result);
            }
        };


        return {
            state,
            form,
            // ...toRefs(state),
            handleLogin, 
            handleHome,
            handleAdLogin,
            handleJoin,
            handlePw,
            naverLogin
        };
    }
}
</script>

<style lang="css" scoped>
.container {
    width   : 500px;
    padding : 20px;
    margin  :0 auto;
    text-align:center;
}
.container h3 {
    margin-top: 100px; 
    margin-left: 50px;
    font-family: 'MapoFlowerIsland';
    font-size: 40px;
}
.lbl {
    display: inline-block;
    width:90px;
    margin-top: 10px;
}
@font-face {
    font-family: 'S-CoreDream-3Light';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_six@1.2/S-CoreDream-3Light.woff') format('woff');
    font-weight: 600;
    font-style: normal;
}
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
