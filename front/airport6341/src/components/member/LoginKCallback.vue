<template>
    <div>
    </div>
</template>

<script>
import axios from 'axios';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
const kakaoHeader = {
    'Content-type': 'application/x-www-form-urlencoded;charset=utf-8'   
};
const getKakaoToken = async (code) => {
    try {
        const data = {
            grant_type: 'authorization_code',
            client_id: '0cd4b2c06665d59daf1522e2a28e0bb3',
            // redirect_uri: 'http://localhost:9090/kakao_callback', // vue 서버 구동시 리다이렉트 URI
            redirect_uri: 'http://1.234.5.158:28080/fligent/kakao_callback', // 톰캣 배포시 리다이렉트 URI
            code: code
        };
        const queryString = Object.keys(data)
            .map(k => encodeURIComponent(k) + '=' + encodeURIComponent(data[k]))
            .join('&');
        const result = await axios.post('https://kauth.kakao.com/oauth/token', queryString, {kakaoHeader});
        return result;
    } catch (e) {
        return e;
    }
};

const getKakaoUserInfo = async () => {
    let data = '';
    await window.Kakao.API.request({
        url: "/v2/user/me",
        success: function (response) {
            data = response;
        },
        fail: function (error) {
            console.log(error);
        },
    });
    return data;
}

var code = '';
export default {
    created() {
        if (this.$route.query.code) {
            code = this.$route.query.code;
            this.setKakaoToken();
        }
    },
    setup () {
        // const route = useRoute();
        const router = useRouter();
        const store = useStore();

        const state = ({
            userid    :'',
            userpw    :'',
            role      : 'CUSTOMER',
            birth     :'', // DB에 넣을 생일
            nickname  :'',
            phone     :'',
            apiimageurl : ''
        })
        
        const setKakaoToken = async() => {
            const { data } = await getKakaoToken(code);
            if (data.error) {
                alert('카카오톡 로그인 오류입니다.');
                // this.$router.replace('/login');
                // router.push({ path: '/' });
                return;
            }
            window.Kakao.Auth.setAccessToken(data.access_token);
            await setUserInfo();
            // router.push({ path: '/' });
        }

        const setUserInfo = async() => {
            const res = await getKakaoUserInfo();
            // 아이디(이메일)
            if(res.kakao_account.email != undefined){
                state.userid = res.kakao_account.email;
            }
            else{
                state.userid = res.id;
            }
            // 비번 생략
            // 닉네임
            state.nickname = res.kakao_account.profile.nickname;
            // 프로필 이미지
            state.apiimageurl = res.kakao_account.profile.profile_image_url;
            // 휴대폰 번호
            state.phone = '000-0000-0000';
            // 생일
            if(res.kakao_account.birthday != undefined){
                state.birth = '1933'+res.kakao_account.birthday;
            }
            else{
                state.birth = '330101';
            }
            kakaoUseridCheck();
        }

      
        // 회원가입시 아이디(이메일) 중복여부 확인
        const kakaoUseridCheck = async() => {
            const url = `/fligent/api/member/idcheck.json?userid=${state.userid}`;
            const headers = {"Content-Type":"application/json"};
            const { data } = await axios.get(url,{headers});

            if(data.result === true){ //DB에 일치하는 아이디가 존재하는 경우
                kakaoUserLogin() // 회원가입 안하고 로그인 진행
            } else { //DB에 일치하는 아이디가 존재하지 않는 경우
                kakaoUserJoin(); //회원가입 후 로그인 진행
            }
        }



        // 회원가입 
        const kakaoUserJoin = async() => {
            const url = `/fligent/api/member/join.json`;
            const headers = {"Content-Type":"application/json"};
            const body = {
                userid : state.userid,
                userpw : state.userpw,
                role : state.role,
                birth : state.birth,
                nickname : state.nickname,
                phone : state.phone,
                airportname:'GMP'
            }
            const { data } = await axios.post(url, body, {headers});
            if(data.status === 200){
                alert('회원가입완료');
            }
            await kakaoUserJoinImg();
        }

        // 회원 프로필 사진 저장
        const kakaoUserJoinImg = async() => {
            const url = `/fligent/api/memberimage/apiinsert.json`;
            const headers = {"Content-Type":"application/json"};
            
            const member1 = {
            userid : state.userid
            }
            const body = {
            member : member1,
            apiimageurl : state.apiimageurl
            }
            const response = await axios.post(url, body, {headers});
            if(response.status == 200){
                kakaoUserLogin();
            }
            else{
                alert("이미지 저장 실패")
            }
        }

        // 로그인처리 + 토큰발행
        const kakaoUserLogin = async() => {
            const url = `/fligent/api/member/login.json`;
            const headers = { "Content-Type": "application/json" };
            const body = {
                    userid: state.userid,
                    userpw: state.userpw,
                    role: 'CUSTOMER'
                }
            const { data } = await axios.post(url, body, { headers });
                if (data.status === 200) {
                    sessionStorage.setItem("token", data.result);

                    store.commit('setLogged', true);
                    store.commit('setCounter');
                    store.commit('setImageurl', state.apiimageurl);

                    router.push({ path: '/' });
                }
        }
        return {state, setKakaoToken, setUserInfo}
    },
}
</script>

<style lang="scss" scoped>

</style>