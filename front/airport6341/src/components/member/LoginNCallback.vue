<template>
  <div>

  </div>
</template>

<script>
import { onMounted } from '@vue/runtime-core';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';
import { useStore } from 'vuex';

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();
    const store = useStore();

    const state = ({
      naverClientId: 'Wg77gLgzjURrRdBDfnhx',
      clientSecret: 'jIesIkc7SG',
      // callbackUrl: 'http://localhost:9090/naver_callback', // vue 서버 구동시 리다이렉트 URI
      callbackUrl : 'http://1.234.5.158:28080/fligent/naver_callback', // 톰캣 배포시 리다이렉트 URI
      code: route.query.code,
      states: route.query.state, // csrf 공격을 방지하기 위해 애플리케이션에서 생성한 상태 토큰값으로 URL 인코딩을 적용한 값을 사용
      access_token: "", // 발급받은 access_token 저장을 위한 변수
      refresh_token: "", // 발급받은 refresh_token 저장을 위한 변수
      userid: '',
      userpw: '',
      role: 'CUSTOMER',
      birth: '', // DB에 넣을 생일
      birthday: '', // 가공할 생일 정보
      birthyear: '', // 가공할 연도 정보
      nickname: '',
      phone: '',
      apiimageurl: '',
    })

    // callback url
    const naverCallback = async () => {
      // console.log('route.query.code => ', route.query.code); //파라미터로 전달받은 code값
      // console.log('route.query.states => ', route.query.state); //파라미터로 전달받은 state값

      // 배포 후 vue.config.js 프록시 연동이 되지 않아 백엔드로 콜백url 설정 
      // https://nid.naver.com/oauth2.0/token?grant_type=authorization_code&client_id=jyvqXeaVOVmV&client_secret=527300A0_COq1_XV33cf&code=EIc5bFrl4RibFls1&state=9kgsGTfH4j7IyAkg  
      const url = `/fligent/api/login/oauth2.0?clientid=${state.naverClientId}&secret=${state.clientSecret}&code=${route.query.code}&state=${route.query.state}`;
      const headers = { "Content-Type": "application/json" };
      const body = {}
      const {data} = await axios.get(url, body, headers);


      state.access_token = data.access_token
      state.refresh_token = data.refresh_token

      naverUserInfo();
    }

    // 사용자 정보 전달받기
    const naverUserInfo = async () => {
      const url = `/fligent/api/login/v1?token=${state.access_token}`;
      const headers = { "Content-Type": "application/json" };
      const { data } = await axios.get(url, { headers });
      // console.log("조회한 사용자 정보 data => ", data)

      // 변수에 값 넣기
      // console.log("이메일 아이디 =====> ", data.response.email)
      state.userid = data.response.email;
      // console.log("닉네임 닉네임 =====> ", data.response.nickname)
      state.nickname = data.response.nickname;
      // console.log("프로필 이미지 =====> ", data.response.profile_image)
      state.apiimageurl = data.response.profile_image;

      // 생일 쪼개기
      let birtharr = data.response.birthday.split('-');
      state.birthday = birtharr.join('');
      state.birthyear = data.response.birthyear.substring(2);
      state.birth = state.birthyear + state.birthday

      // 휴대폰 번호 쪼개기
      state.phone = data.response.mobile;

      naverUseridCheck();
    }

    // 회원가입시 아이디(이메일) 중복여부 확인
    const naverUseridCheck = async () => {
      const url = `/fligent/api/member/idcheck.json?userid=${state.userid}`;
      const headers = { "Content-Type": "application/json" };
      const { data } = await axios.get(url, { headers });
      // console.log('이메일중복확인 data=>' ,data);

      if (data.result === true) { //DB에 일치하는 아이디가 존재하는 경우
        naverUserLogin() // 회원가입 안하고 로그인 진행
      } else { //DB에 일치하는 아이디가 존재하지 않는 경우
        naverUserJoin(); //회원가입 후 로그인 진행
      }
    }

    // 회원가입 
    const naverUserJoin = async () => {
      // 변수에 값 넣기
      // state.userid = data2.response.email;
      // state.nickname = data2.response.nickname;
      // state.apiimageurl = data2.response.profile_image;

      const url = `/fligent/api/member/join.json`;
      const headers = { "Content-Type": "application/json" };
      const body = {
        userid: state.userid,
        userpw: state.userpw,
        role: state.role,
        birth: state.birth,
        nickname: state.nickname,
        phone: state.phone,
        airportname: 'GMP'
      }
      const { data } = await axios.post(url, body, { headers });
      // console.log(data);
      if (data.status === 200) {
        alert('회원가입완료');
      }
      naverUserJoinImg();
    }

    // 회원 프로필 사진 저장
    const naverUserJoinImg = async () => {
      const url = `/fligent/api/memberimage/apiinsert.json`;
      const headers = { "Content-Type": "application/json" };

      const member1 = {
        userid: state.userid
      }
      const body = {
        member: member1,
        apiimageurl: state.apiimageurl
      }
      const response = await axios.post(url, body, { headers });
      console.log(response);
      naverUserLogin();
    }

    // 로그인처리 + 토큰발행
    const naverUserLogin = async () => {
      const url = `/fligent/api/member/login.json`;
      const headers = { "Content-Type": "application/json" };
      const body = {
        userid: state.userid,
        userpw: state.userpw,
        role: 'CUSTOMER'
      }
      const { data } = await axios.post(url, body, { headers });
      // console.log(data);
      if (data.status === 200) {
        sessionStorage.setItem("token", data.result);

        store.commit('setLogged', true);
        store.commit('setCounter');
        store.commit('setImageurl', state.apiimageurl);

        router.push({ path: '/' });
      }
    }

    onMounted(() => {
      naverCallback();
    })

    return { state, naverCallback }
  }
}
</script>

<style lang="scss" scoped>

</style>