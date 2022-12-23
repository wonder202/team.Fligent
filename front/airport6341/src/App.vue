<template>
    <div class="button">
        <!-- {{state.imageurl}} -->
        <!-- {{state.logged}} -->
        <div class="body">
            <br />
            <!--회원정보관리-->
            <el-button link @click="handleHome()" style="margin-left: 20px; margin-bottom: 10px;">
                <span class="bi bi-house fs-3"></span>
            </el-button>
            <el-button color="#F76868" link
                        @click="handleJoin()" v-if="!state.logged" style="float: right; font-weight: 600; font-size: 18px; margin-right: 40px;">회원가입</el-button>       
            <el-button color="#F76868" link  
                            @click="handleLogin()" v-if="!state.logged" style="float: right; font-weight: 600; font-size: 18px;">로그인</el-button>
            <!-- 마이페이지 이동되는 회원프로필이미지 -->
            <el-button link 
                        @click="handleMypage()" v-if="state.logged" style="float: right;">
                <img :src="imageurl" style="width:50px; height: 50px; margin-right: 25px; border-radius: 30px;" />
            </el-button>
            <el-button color="#F76868" link 
                        @click="handleLogout()" v-if="state.logged" style="float: right; font-weight: 600; font-size: 18px; margin-top: 10px;">로그아웃</el-button>
            <!--상세페이지로 이동-->
            <el-button color="#0339A6" round style="margin-left: 10px; height: 40px; margin-bottom: 20px; font-weight: 600; font-size: 18px;" @click="handleGimpo()">#김포공항</el-button><r />
            <el-button color="#F2D1C9" round style="margin-left: 10px; height: 40px; margin-bottom: 20px; font-weight: 600; font-size: 18px;" @click="handleGimhae()">#김해공항</el-button><r />
            <el-button color="#0339A6" round style="margin-left: 10px; height: 40px; margin-bottom: 20px; font-weight: 600; font-size: 18px;" @click="handleJeju()">#제주공항</el-button><r />
            <el-button color="#F2D1C9" round style="margin-left: 10px; height: 40px; margin-bottom: 20px; font-weight: 600; font-size: 18px;" @click="handleIncheon()">#인천공항</el-button><r />
            <el-button color="#0339A6" round style="margin-left: 10px; height: 40px; margin-bottom: 20px; font-weight: 600; font-size: 18px;" @click="handleBoard()">#게시판</el-button><r />
            <el-button color="#F2D1C9" round style="margin-left: 10px; height: 40px; margin-bottom: 20px; font-weight: 600; font-size: 18px;" @click="handleItem()">#쇼핑하기</el-button><r />
            <el-button color="#0339A6" round style="margin-left: 10px; height: 40px; margin-bottom: 20px; font-weight: 600; font-size: 18px;" @click="handleCart()" v-if="state.logged">#장바구니</el-button><r />
        </div>
        <router-view></router-view>
    </div>
</template>

<script>
import { reactive, toRefs } from '@vue/reactivity'
import { useStore } from 'vuex'
import { computed, onMounted } from '@vue/runtime-core';
import { useRouter} from 'vue-router';
export default {
  setup () {
      const router = useRouter();
      
      const store = useStore();

      const state = reactive({
        logged   : computed(() => store.getters.getLogged),
        // counter  : computed(() => store.getters.getCounter),
        name     : '임시',
        imageurl : computed(() => store.getters.getImageurl)
      });

      onMounted(()=>{
        store.dispatch("handleImg")
        //store의 actions를 불러옴
      });

      const handleHome = () => {
          router.push({path:'/'});
      }

      const handleJoin = () => {
          router.push({path:'/memberjoin'});
      }

      const handleLogin = () => {
          router.push({path:'/memberlogin'});
      }

      const handleLogout = () => {
          router.push({path:'/memberlogout'});
      }

      const handleMypage= () => {
          router.push({path:'/customermypage'});
      }
      
      const handleGimhae= () => {
          router.push({path:'/gimhae'});
      }
      
      const handleGimpo= () => {
          router.push({path:'/gimpo'});
      }
      
      const handleJeju= () => {
          router.push({path:'/jeju'});
      }
      
      const handleIncheon= () => {
          router.push({path:'/incheon'});
      }  

      const handleBoard= () => {
          router.push({path:'/board'});
      }

      const handleCart= () => {
          router.push({path:'/cart'});
      }

      const handleItem= () => {
          router.push({path:'/customeritem'});
      }
      
      return {
          state,
          ...toRefs(state),
          handleLogin, 
          handleLogout, 
          handleHome, 
          handleJoin,
          handleMypage,

          handleGimpo,
          handleIncheon,
          handleJeju,
          handleGimhae,

          handleBoard,
          handleCart,
          handleItem,
      };
  }
}
</script>

<style lang="css" scoped>
.body{
    background-color: #F2E9E9;
}
@font-face {
    font-family: 'IBMPlexSansKR-Regular';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
    font-weight: normal;
    font-style: normal;
}
* {
	font-family: 'IBMPlexSansKR-Regular';
}


</style>


