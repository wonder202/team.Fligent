<template>
    <div class="container">
        <!-- {{state.row}} -->
        <el-tabs tab-position="left" class="demo-tabs" v-model="state.activeName">
            <el-tab-pane label="주문목록" name="orderlist">
                <h3 width="500px">주문목록</h3>
                <div class="list">
                    <el-table :data="state.row" style="width: 100%; margin-left: 100px;">
                        <el-table-column prop="ordercode" label="주문번호"/>
                        <el-table-column prop="name" label="물품명"/>
                        <el-table-column prop="cnt" label="주문수량" />
                        <el-table-column prop="price" label="가격" />
                        <el-table-column prop="regdate" label="주문일" />
                        <el-table-column fixed="right" label="주문관리">
                            <template #default="scope">
                                <!-- {{scope.row.ono}} -->
                                <el-button text class="button" type="danger" style="font-weight: 600;" @click="handleCancle(scope.row.ono)" >취소</el-button>
                            </template>
                        </el-table-column>
                        <el-table-column fixed="right" width="120">
                        </el-table-column>
                    </el-table>
                    <el-button style="margin-bottom:50px;" v-for="tmp in state.pages" :key="tmp" @click="handlePage(tmp)">
                        {{tmp}}
                    </el-button>
                </div>
            </el-tab-pane>

            <el-tab-pane label="회원정보 수정" name="userInfoChange">
                <h3 style="padding:20px;" width="500px">회원정보 수정</h3>
                <div class="change">
                    <el-form class="info" label-width="150px">
                        <el-form-item label="닉네임">
                            <el-input v-model="nickname" style="width:250px;" :ref = "el => {form[0] = el}" />
                        </el-form-item>
                        <el-form-item label="연락처">
                            <el-input style="width:80px" v-model="phone" :ref = "el => {form[1] = el}" />
                            <label>-</label>
                            <el-input style="width:80px" v-model="phone1" :ref = "el => {form[2] = el}" />
                            <label>-</label>
                            <el-input style="width:80px" v-model="phone2" :ref = "el => {form[3] = el}" />
                        </el-form-item>
                        <el-form-item label="생년월일" >
                            <el-input v-model="birth" style="width:250px; margin-bottom: 10px;" :ref = "el => {form[4] = el}" />
                            <div class="demo-date-picker">
                                <el-date-picker type="date" v-model="birth"
                                placeholder="변경할 생년월일을 입력해주세요" 
                                style="width:250px;"
                                format="YYYY/MM/DD"
                                value-format="YYYYMMDD" />
                            </div>
                        </el-form-item>
                        <br />
                        <el-form-item label="즐겨찾기한 공항">
                            <input type="radio" id="GMP" v-model="airport" value="GMP">
                            <label for="GMP" style="margin-right: 20px;">김포공항</label>

                            <input type="radio" id="ICN" v-model="airport" value="ICN">
                            <label for="ICN" style="margin-right: 20px;">인천공항</label>

                            <input type="radio" id="PUS" v-model="airport" value="PUS">
                            <label for="PUS" style="margin-right: 20px;">김해공항</label>

                            <input type="radio" id="CJU" v-model="airport" value="CJU">
                            <label for="CJU" style="margin-right: 20px;">제주공항</label>
                        </el-form-item>
                        <br />
                        <el-form-item label="회원이미지">
                            <img :src="imageurl" style="width:300px; height: 300px; margin-bottom: 30px;" id="preview"/>
                            <input type="file" @change="handleFile($event)"  style="margin-left: 70px;"  />
                        </el-form-item>
                        <br />
                        <el-form-item>
                            <el-button type="primary" round @click="handleUpdate" style="font-size: 20px;">수정하기</el-button>
                            <el-button type="info" plain round @click="handleNotUpdate" style="margin: auto; font-size: 20px;">취소</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-tab-pane>

            <el-tab-pane label="암호 변경">
                <h3 style="padding:20px" width="500px">암호 변경</h3>
                <div class="change">
                    <el-form class="info" label-width="150px">
                        <el-form-item label="현재 암호">
                            <el-input v-model="state.userpw" type="password" autocomplete="off" style="width:250px" />
                        </el-form-item>
                        <br />
                        <el-form-item label="변경할 암호">
                            <el-input v-model="state.userpw1" type="password" autocomplete="off" style="width:250px" />
                        </el-form-item><br/>
                        <el-form-item label="변경할 암호 확인">
                            <el-input v-model="state.userpw2" type="password" autocomplete="off" style="width:250px" />
                        </el-form-item><br/>
                        <el-form-item>
                            <el-button type="primary" round style="margin-left: 150px; font-size: 20px;" @click="handleUpdatePw">변경하기</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-tab-pane>

            <el-tab-pane label="나의 활동" style="margin-left: 50px;">
                <h3 width="500px">나의 활동</h3>
                <el-tabs tab-position="top" type="border-card" class="demo-tabs" style="border: 1px solid #ffffff">
                    <el-tab-pane label="나의 글">
                        <el-table :data="state.blist" style="width: 100%; margin-left: 100px;">
                            <el-table-column prop="title" label="제목" />
                            <el-table-column prop="content" label="내용" />
                            <el-table-column prop="hit" label="조회수" />
                            <el-table-column prop="regdate" label="작성일"/>
                            <el-table-column fixed="right" label="글관리">
                                <template #default="scope">
                                    <el-button round class="button" style="font-weight: 600;" @click="handleContent(scope.row.bno)" >보러가기</el-button>
                                    <el-button text class="button" type="danger" style="font-weight: 600;" @click="handleDeleteboard(scope.row.bno)" >삭제</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column fixed="right" width="120">
                            </el-table-column>
                        </el-table>
                        <el-button style="margin-bottom:50px;" v-for="tmp in state.pages1" :key="tmp" @click="handlebPage(tmp)">
                            {{tmp}}
                        </el-button>
                    </el-tab-pane>
                    <el-tab-pane label="나의 댓글">
                        <el-table :data="state.rlist" style="width: 80%; margin-left: 100px;">
                            <el-table-column prop="content" label="내용" />
                            <el-table-column prop="regdate" label="작성일" />
                            <el-table-column fixed="right" label="댓글관리">
                                <template #default="scope">
                                    <el-button text class="button" type="danger" style="font-weight: 600;" @click="handleDeletereply(scope.row.rno)" >삭제</el-button>
                                </template>
                            </el-table-column>
                            <el-table-column fixed="right" width="120">
                            </el-table-column>
                        </el-table>
                        <el-button style="margin-bottom:50px;" v-for="tmp in state.pages2" :key="tmp" @click="handlerPage(tmp)">
                            {{tmp}}
                        </el-button>
                    </el-tab-pane>
                    <el-tab-pane label="좋아요한 글">
                        <el-table :data="state.llist" style="width: 100%; margin-left: 100px;">
                            <el-table-column prop="title" label="제목" />
                            <el-table-column prop="content" label="내용" />
                            <el-table-column prop="hit" label="조회수" />
                            <el-table-column prop="regdate" label="작성일" />
                            <el-table-column fixed="right" label="해제">
                                <template #default="scope">
                                    <el-button text class="button" type="danger" style="font-weight: 600;" @click="handleUnlike(scope.row.bno)">해제</el-button>
                                </template>
                            </el-table-column>
                        </el-table>
                        <el-button style="margin-bottom:50px;" v-for="tmp in state.pages3" :key="tmp" @click="handlelPage(tmp)">
                            {{tmp}}
                        </el-button>
                    </el-tab-pane>
                </el-tabs>
            </el-tab-pane>

            <el-tab-pane label="회원탈퇴" style="margin-left: 50px;">
                <h3 style="padding:20px" width="500px">회원탈퇴</h3>
                <div class="change">
                    <el-form class="info" label-width="150px">
                        <el-form-item label="현재 암호">
                            <el-input v-model="state.userpw" type="password" autocomplete="off" style="width:250px" :ref = "el => {form[5] = el}" />
                        </el-form-item><br/>
                        <el-form-item label="암호 확인">
                            <el-input v-model="state.userpw3" type="password" autocomplete="off" style="width:250px" :ref = "el => {form[6] = el}" />
                        </el-form-item><br/>
                        <el-form-item>
                            <el-button type="primary" round style="margin-left: 150px; font-size: 20px;" @click="handleDelete">탈퇴하기</el-button>
                        </el-form-item>
                    </el-form>
                </div>
            </el-tab-pane>

        </el-tabs>
    </div>
</template>

<script>
import { reactive } from '@vue/reactivity'
import { onMounted, toRefs } from '@vue/runtime-core';
import axios from 'axios';
import { useRoute, useRouter } from 'vue-router';
import { UploadFilled } from '@element-plus/icons-vue'
import { ref } from 'vue'
export default {
    setup () {
        const form = ref([]);
        const router = useRouter();
        const route = useRoute();
        const state = reactive({
            row      : [],
            blist    : [],
            rlist    : [],
            llist    : [],
            file     : null,
            mimageno : '',
            imageurl : [],
            nickname : '',
            birth    : '',
            phone    : '',
            phone1   : '',
            phone2   : '',
            role     : 'CUSTOMER',  // 고정으로 넣기(ADMIN은 따로X)
            token    : sessionStorage.getItem("token"),
            userpw   : '',  // 현재 암호
            userpw1  : '',  // 새로운 암호
            userpw2  : '',  // 새로운 암호 확인
            userpw3  : '',  // 탈퇴 암호 확인
            page     : 1,
            page1    : 1,
            page2    : 1,
            page3    : 1,
            pages    : 0,
            pages1   : 0,
            pages2   : 0,
            pages3   : 0,
            count    : 0,
            count1   : 0,
            count2   : 0,
            count3   : 0,
            airport  : '',
            type     : 1,
            activeName : 'userInfoChange'
        });



        // 로그인한 사용자의 이름과 나이 정보 받기
        const handleData = async() => {
            const url =`/fligent/api/customer/mypage/update.json`;
            const headers = {
                "Content-Type":"application/json",
                "TOKEN" : state.token
            };

            const {data} = await axios.get(url, {headers});
            // console.log(data);

            if(data.status === 200) {
                state.nickname = data.result.nickname;
                state.birth = data.result.birth;

                const phone = data.result.phone.split('-');
                state.phone = phone[0]
                state.phone1 = phone[1]
                state.phone2 = phone[2]
                state.airport = data.result.airportname;
            }
        };
        // 회원정보 수정하기 (POST)
        const handleUpdate = async() => {
            if(state.nickname === ''){
                alert("닉네임을 입력하세요")
                form.value[0].focus();
                return false;
            }
            if(state.phone === ''){
                alert("전화번호를 입력하세요")
                form.value[1].focus();
                return false;
            }
            if(state.phone1 === ''){
                alert("전화번호를 입력하세요")
                form.value[2].focus();
                return false;
            }
            if(state.phone2 === ''){
                alert("전화번호를 입력하세요")
                form.value[3].focus();
                return false;
            }
            if(state.birth === ''){
                alert("생년월일을 입력하세요")
                form.value[4].focus();
                return false;
            }
            const url =`/fligent/api/customer/mypage/update.json`;
            const headers = {
                "Content-Type":"application/json",
                "token" : state.token
            };
            const body = {
                nickname :state.nickname,
                birth : state.birth,
                phone:`${state.phone}-${state.phone1}-${state.phone2}`,
            };

            const { data } = await axios.post(url, body, {headers});
            // console.log(data);
            if(data.status === 200){
                handleAirport();
                handleIMGUpdate();
                alert('정보가 변경되었습니다.');
                router.go();
            }
            else{
                alert('다시 시도해 주세요.');
            }
        };

        const handleNotUpdate = () => {
            handleData();
            handleImg();
        };

        //즐겨찾기한 공항 변경
        const handleAirport = async() => {
            const url =`/fligent/api/customer/mypage/myairfavorite.json?airportname=${state.airport}`;
            const headers = {
                "Content-Type":"application/json",
                "token" : state.token
            };
            // console.log(headers)
            const body = {
                airportname :state.airport,
            };
            const { data } = await axios.put(url, body, {headers});
            console.log(data);
        };

        //이미지정보 가져오기
        const handleImg = async() => {
            const url =`/fligent/api/customer/selectmemberimglist.json`;
            const headers = {
                "Content-Type":"application/json",
                "token" : state.token
            };
            const {data} = await axios.get(url, {headers});
            if(data.status === 200) {
                state.imageurl = data.result;
                const tmp1 = state.imageurl.split('?')[1];
                const tmp2 = tmp1.split('=')[1];
                state.mimageno = tmp2;
            }
        };

        const handleFile = (e) =>{
            // const reader = new FileReader();
            state.file = null;
            if(e.target.files.length > 0) {
                state.file = e.target.files[0];
                if(e.target.files && e.target.files[0]){
                    if(e.target.files && e.target.files[0]){
                        state.imageurl = URL.createObjectURL(e.target.files[0])  
                    }
                }
            }
        };
        //회원이미지 수정
        const handleIMGUpdate = async() => {
            const url = `/fligent/api/customer/selectmemberimgupdate.json`;
            const headers ={
                "Content-Type":"multipart/form-data",
                "token" : state.token
            };

            let body = new FormData();
                body.append("mimageno", state.mimageno);
                body.append("file", state.file);

            const { data }  = await axios.put(url, body, {headers});
            console.log(data);
        };

        //주문목록 받아오기
        const handleList = async() => {
            const url =`/fligent/api/order/selectorderlist.json?start=${state.page}`;
            const headers = {
                "Content-Type":"application/json",
                "TOKEN" : state.token
            };
            const {data} = await axios.get(url, {headers});
            // console.log(data);
            if(data.status === 200) {
                state.row = data.orderlist;
                state.count = data.count;
                state.pages = Math.floor(data.count/10+1);
                for(const item of state.row){
                    item.price = item.price.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
                }
            }
        };

        const handlePage = (tmp) =>{
            state.page = tmp;
            handleList();
        };

        //주문취소
        const handleCancle = async(ono) => {
            if(confirm('주문 취소할까요?')){
                const url= `/fligent/api/order/deleteorder.json?ono=${ono}`;
                const headers = {
                    "Content-Type":"application/json",
                    "token" : state.token
                };
                const body = {
                    
                }
                const {data} = await axios.post(url, body, {headers});
                // console.log(data);
                if(data.status === 200){
                    alert('주문 취소되었습니다.\n환불은 7~10일 소요됩니다.\n자세한 내용은 결제사에 문의바랍니다^^');
                    handleList();
                }
                else{
                    alert('주문 취소 실패했습니다.');
                }
            }
        };

        //작성글 조회
        const handleBoard = async() => {
            const url =`/fligent/api/customer/mypage/boardlist.json?start=${state.page1}`;
            const headers = {
                "Content-Type":"application/json",
                "TOKEN" : state.token
            };
            const {data} = await axios.get(url, {headers});
            // console.log(data);
            if(data.status === 200) {
                state.blist = data.result;
                for(const tmp of state.blist){
                    tmp.content = tmp.content.toString().substr(0, 30) + "...";
                }
                state.count1 = data.count;
                state.pages1 = Math.floor(data.count/10+1);
            }
        };

        const handlebPage = (tmp) =>{
            state.page1 = tmp;
            handleBoard();
        };

        const handleContent = (bno) => {
            router.push({path:'boardselectone', query:{bno:bno}});
        }

        //작성글 삭제
        const handleDeleteboard = async(bno) => {
            if(confirm('삭제할까요?')){
                const url = `/fligent/api/board/deleteoneboard.json?bno=${bno}`;
                const headers = {
                    "Content-Type":"application/json", 
                    "TOKEN" : state.token
                };
                const body = {
    
                }
                const {data} = await axios.post(url, body, {headers});
                // console.log(data);
                if(data.status === 200){
                    alert('삭제되었습니다.')
                    handleBoard();
                }
                else{
                    alert('삭제 실패했습니다.');
                }
            }
        };

        //댓글 조회
        const handleReply = async() => {
            const url =`/fligent/api/customer/mypage/boardreplylist.json?start=${state.page2}`;
            const headers = {
                "Content-Type":"application/json",
                "TOKEN" : state.token
            };
            const {data} = await axios.get(url, {headers});
            // console.log(data);
            if(data.status === 200) {
                state.rlist = data.result;
                state.count2 = data.count;
                state.pages2 = Math.floor(data.count/10+1);
            }
        };

        const handlerPage = (tmp) =>{
            state.page2 = tmp;
            handleReply();
        };

        //댓글 삭제
        const handleDeletereply = async(rno) => {
            if(confirm('삭제할까요?')){
                const url = `/fligent/api/board/deletereply.json?rno=${rno}`;
                const headers = {
                    "Content-Type":"application/json", 
                    "TOKEN" : state.token
                };
                const body = {
    
                }
                const {data} = await axios.post(url, body, {headers});
                // console.log(data);
                if(data.status === 200){
                    alert('삭제되었습니다.')
                    handleReply();
                }
                else{
                    alert('삭제 실패했습니다.');
                }
            }
        };

        //좋아요한 글 조회
        const handleLike = async() => {
            const url =`/fligent/api/customer/mypage/boardlikelist.json?start=${state.page3}`;
            const headers = {
                "Content-Type":"application/json",
                "TOKEN" : state.token
            };
            const {data} = await axios.get(url, {headers});
            // console.log(data);
            if(data.status === 200) {
                state.llist = data.result;
                for(const tmp of state.llist){
                    tmp.content = tmp.content.toString().substr(0, 30) + "...";
                }
                state.count3 = data.count;
                state.pages3 = Math.floor(data.count/10+1);
            }
        };

        //좋아요 해제
        const handleUnlike = async(bno) => {
            if(confirm('해제할까요?')){
                const url = `/fligent/api/customer/mypage/deletelike.json?bno=${bno}`;
                const headers = {
                    "Content-Type":"application/json", 
                    "TOKEN" : state.token
                };
                const body = {

                }
                // console.log(body)
                const {data} = await axios.post(url, body, {headers});
                // console.log(data);
                if(data.status === 200){
                    alert('해제되었습니다.')
                    handleLike();
                }
                else{
                    alert('해제 실패했습니다.');
                }
            }
        };


            
        

        const handlelPage = (tmp) =>{
            state.page3 = tmp;
            handleLike();
        };

        onMounted(()=>{
            handleData();
            handleList();
            handleImg();
            handleBoard();
            handleReply();
            handleLike();


            state.type = Number(route.query.type);
            // console.log(state.type);
            if( state.type === 1){
              state.activeName = "orderlist";
            }
            else if( state.type === 2){
                state.activeName = "userInfoChange";
            }
        });

        const handleUpdatePw = async() => {
            if(state.userpw.length <= 0){
                alert('암호를 입력하세요');
                return false;
            }
            if(state.userpw1.length <= 0){
                alert('변경할 암호를 입력하세요');
                return false;
            }
            if(state.userpw === state.userpw1){
                alert('이전 암호와 같습니다.');
                return false;
            }
            if(state.userpw1 !== state.userpw2){
                alert('변경할 암호가 일치하지 않습니다.');
                return false;
            }
            const url = `/fligent/api/customer/mypage/mypageupdatepassword.json`;
            const headers = {
                "Content-Type":"application/json",
                "token" : state.token
            };
            const body = {
                userpw   : state.userpw,     // 기존암호
                userpw1  : state.userpw1     // 새로운 암호
            }
            const { data } = await axios.put(url, body, {headers});
            // console.log(data);
            if(data.status === 200){
                alert('암호가 변경되었습니다.');
                router.go();
            }
            else {
                alert('암호가 일치하지 않습니다.');
            }
        };

        const handleDelete = async() => {
            if(state.userpw === ''){
                alert("현재 암호를 입력하세요")
                form.value[5].focus();
                return false;
            }
            if(state.userpw3 === ''){
                alert("암호 확인을 입력하세요")
                form.value[6].focus();
                return false;
            }
            if(state.userpw !== state.userpw3){
                alert('암호가 일치하지 않습니다.');
                return false;
            }
            if(confirm('탈퇴할까요?')){
                const url= `/fligent/api/customer/mypage/delete.json`;
                const headers = {
                    "Content-Type":"application/json",
                    "token" : state.token
                };
                const body = {
                    userpw : state.userpw,
                }       
                const {data} = await axios.post(url, body, {headers});                
                // console.log(data);
                // console.log("--------delete test 여기까지 옴 ---------");
                if(data.status === 200){
                    alert('탈퇴되었습니다.');
                    router.push({path:'/memberlogout'});
                }
                else{
                    alert('다시 시도해 주세요.');
                }
            }
        };

        return {
            state, 
            form,
            ...toRefs(state),
            UploadFilled,
            handleUpdate, 
            handleUpdatePw, 
            handleDelete,
            handleFile,
            handleCancle,
            handlePage,
            handleDeleteboard,
            handleDeletereply,
            handleUnlike,
            handlebPage,
            handlerPage,
            handlelPage,
            handleContent,
            handleNotUpdate
        }
    }
}
</script>

<style lang="css" scoped>
.container {
    width   : 100%;
    padding : 100px;
    /* margin : auto; */
    text-align:center;
    /* border:1px solid #cccccc; */
}
.info{
    /* border: 1px solid #cccccc; */
    width: 50%;
    margin: auto;
}
.change{
    /* border: 1px solid #cccccc; */
    width: 100%;
}
.el-tab-pane h3{
    font-family: 'MapoFlowerIsland';
    font-size: 30px;
    margin-bottom: 20px;
    font-weight: 600;
}
/* @font-face {
  font-family: 'MapoFlowerIsland';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/MapoFlowerIslandA.woff') format('woff');
  font-weight: normal;
  font-style: normal;
} */
</style>
