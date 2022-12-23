import {  createRouter, createWebHistory } from 'vue-router';
// import {  createRouter } from 'vue-router';

import HomePage from '@/components/HomePage.vue'

import MemberLogin from '@/components/member/MemberLoginPage.vue';
import MemberLogout from '@/components/member/MemberLogoutPage.vue';
import MemberJoin from '@/components/member/MemberJoinPage.vue';
import MemberFindPassword from '@/components/member/MemberFindPassword.vue';

import LoginNCallback from '@/components/member/LoginNCallback.vue';
import LoginKCallback from '@/components/member/LoginKCallback.vue'

import CustomerMypage from '@/components/member/CustomerMypagePage.vue';
import CustomerItemPage from '@/components/member/CustomerItemPage.vue';
import CustomerItemSelectOnePage from '@/components/member/CustomerItemSelectOnePage.vue';

import AdminMypage from '@/components/member/AdminMypagePage.vue';
import AdminLogin from '@/components/member/AdminLoginPage.vue';
import AdminIteminsertPage from '@/components/member/AdminIteminsertPage.vue';
import AdminItemUpdatePage from '@/components/member/AdminItemUpdatePage.vue';

import IncheonPage from '@/components/AirportCate/IncheonPage.vue'
import GimhaePage from '@/components/AirportCate/GimhaePage.vue'
import GimpoPage from '@/components/AirportCate/GimpoPage.vue'
import JejuPage from '@/components/AirportCate/JejuPage.vue'

import BoardwritePage from '@/components/board/BoardwritePage.vue'
import BoardPage from '@/components/board/BoardPage.vue'
import BoardselectonePage from '@/components/board/BoardselectonePage.vue'
import BoardupdatePage from '@/components/board/BoardupdatePage.vue'

import CartPage from '@/components/cart/CartPage.vue'
import OrderPage from '@/components/cart/OrderPage.vue'
import OrderSuccessPage from '@/components/cart/OrderSuccessPage.vue'


const routes = [
    {path:'/', name:'HomePage', component:HomePage},

    {path:'/memberlogin', name:'MemberLoginPage', component:MemberLogin},
    {path:'/memberlogout', name:'MemberLogoutPage', component:MemberLogout},
    {path:'/memberjoin', name:'MemberJoinPage', component:MemberJoin},
    {path:'/findpassword', name:'MemberFindPassword', component:MemberFindPassword},
    {path:'/naver_callback', name:'LoginNCallback', component:LoginNCallback},
    {path:'/kakao_callback', name:'LoginKCallback', component:LoginKCallback},

    {path:'/customermypage', name:'CustomerMypage', component:CustomerMypage},
    {path:'/customeritem', name:'CustomerItemPage', component:CustomerItemPage},
    {path:'/customeritemselectone', name:'CustomerItemSelectOnePage', component:CustomerItemSelectOnePage},

    {path:'/adminmypage', name:'AdminMypage', component:AdminMypage},
    {path:'/adminlogin', name:'AdminLogin', component:AdminLogin},
    {path:'/adminiteminsert', name:'AdminIteminsertPage', component:AdminIteminsertPage},
    {path:'/adminitemupdate', name:'AdminItemUpdatePage', component:AdminItemUpdatePage},

    {path:'/incheon', component:IncheonPage},
    {path:'/gimhae', component:GimhaePage},
    {path:'/gimpo', component:GimpoPage},
    {path:'/jeju', component:JejuPage},

    {path:'/boardwrite', component:BoardwritePage},
    {path:'/board', component:BoardPage},
    {path:'/boardselectone', component:BoardselectonePage},
    {path:'/boardupdate', component:BoardupdatePage},  
    
    {path:'/cart', component:CartPage},
    {path:'/order', component:OrderPage},
    {path:'/ordersuccess', component:OrderSuccessPage},
]

// console.log('process.env.BASE_URL ==> ',process.env.BASE_URL)

const router = createRouter({
    history : createWebHistory( '/fligent' ), //주소표시방법 //127.0.0.1:8080/#/login
    // history : createWebHistory(), //주소표시방법 //127.0.0.1:8080/fligent
    // history : createWebHistory(process.env.BASE_URL), //<================ 여기변경
    
    routes  : routes,
});

router.beforeEach((to, from, next) => {
    // console.log('to =>', to);
    // console.log('from =>', from);
    
    // 로그인, 로그아웃 url 저장하지 않음
    if(to.path !== '/memberlogin' && to.path !== '/memberlogout' && to.path !== '/memberjoin'  && to.path !== '/findpassword'){
        sessionStorage.setItem("CURR_URL", to.path);

        //object -> string로 변경 => JSON.stringify(변경할오브젝트)
        //string -> object로 변경 => JSON.parse(변경할문자)
        sessionStorage.setItem("CURR_QRY", JSON.stringify(to.query));
    }

    next();
});

export default router;