<template>  
    <div class="container">    
        <!-- {{state}} -->
        <h3 style="margin-top: 100px; margin-left: 50px; text-decoration: underline; text-underline-position:under; 
                font-family: 'MapoFlowerIsland';">관리자 로그인</h3><br />
        <div>
            <label class="lbl">아이디</label>
            <el-input v-model="state.userid" placeholder="아이디를 입력하세요" style="width:300px" :ref = "el => {form[0] = el}" />
        </div><br />
        <div>
          <label class="lbl">암호</label>
          <el-input type="password" style="width:300px" v-model="state.userpw" autocomplete="off" :ref = "el => {form[1] = el}" />
        </div><br />
        <div style="margin-top:30px; margin-left: 50px;">
            <el-button color="#F76868" link @click="handleLogin" style="font-size: 20px; font-weight: 600;">로그인</el-button>
            <el-button link @click="handleHome()">
                <span class="bi bi-house fs-4"></span>
            </el-button>
        </div>
    </div>

</template>

<script>
import axios from 'axios';
import {  reactive, ref, toRefs } from '@vue/reactivity';
import { useRouter } from 'vue-router';
import { useStore } from 'vuex';
export default {
    setup () {
        const router = useRouter();
        const store = useStore();
        const form = ref([]);

        const state = reactive({
            userid: 'admin',
            userpw: '',
        });
  
        const handleHome = () => {
            router.push({path:'/'});
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
                role    : 'ADMIN'
            }
            const {data} = await axios.post(url, body, {headers});
            // console.log(data);
            if(data.status === 200){
                sessionStorage.setItem("token", data.result);

                store.commit('setLogged', true);
                store.commit('setCounter');
                
                alert('로그인 되었습니다.\n관리자 페이지로 이동합니다');
                router.push({path:'/adminmypage'});
            }
            else {
                alert('아이디 또는 암호가 올바르지 않습니다');
                router.push({path:'/adminlogin'});
            }
        };

        return {
            state,
            form,
            ...toRefs(state),
            handleLogin, 
            handleHome,
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
.lbl {
    display: inline-block;
    width:90px;
    margin-top: 10px;
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
