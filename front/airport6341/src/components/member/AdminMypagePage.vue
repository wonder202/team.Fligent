<template>
    <div class="container">
        <el-tabs tab-position="left" class="demo-tabs">
            <el-tab-pane label="회원관리" style="margin-left: 50px;">
            <h3>회원목록</h3>
                <el-table :data="state.list" style="width: 90%; text-align: center;">
                    <el-table-column fixed prop="userid" label="아이디" width="250" />
                    <el-table-column prop="nickname" label="닉네임" width="200" />
                    <el-table-column prop="regdate" label="가입일자" />
                    <el-table-column fixed="right" label="회원관리" width="120">
                        <template #default="scope">
                            <!-- scope.row.userid와 state.list[scope.$index].userid는 같은 것 -->
                            <el-button link type="danger" v-if="state.list[scope.$index].block === 0" 
                                @click="handleBlock(scope.row.userid)" style="font-weight: 600;">차단</el-button>
                            <span link type="info" v-if="state.list[scope.$index].block === 1" 
                                style="font-weight: 600;">탈퇴</span>
                            <el-button link v-else-if="state.list[scope.$index].block === 2"
                                @click="handleBlock(state.list[scope.$index].userid)" style="font-weight: 600; color:deepskyblue;">해제</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-button v-for="tmp of state.pages" :key="tmp" @click="handlePage(tmp)">
                  {{tmp}}
                </el-button>
            </el-tab-pane>
            <el-tab-pane label="회원 게시물관리" style="margin-left: 70px;">
                <h3>회원 게시물목록</h3>
                <el-table :data="state.list1" style="width: 94%; text-align: center;">
                    <el-table-column fixed prop="memberUserid" label="회원아이디" />
                    <el-table-column prop="memberNickname" label="닉네임" />
                    <el-table-column prop="bno" label="번호" />
                    <el-table-column prop="hit" label="조회수" />
                    <el-table-column prop="content" label="내용"/>
                    <el-table-column fixed="right" label="게시글관리">
                        <template #default="scope">
                            <el-button link type="primary" style="font-weight: 600; color: red;"
                                    @click="handleDeleteboard(scope.row.bno)">삭제</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-button v-for="tmp of state.pages1" :key="tmp" @click="handlePage1(tmp)">
                  {{tmp}}
                </el-button>
            </el-tab-pane>
            <el-tab-pane label="상품목록" style="margin-left: 100px;">
                <h3>상품목록</h3>
                <div style="width: 80%;">
                    <select class="form-select" v-model="state.icateno" 
                            style="float: left; width: 180px; margin-bottom: 20px;" 
                            @change="onChange($event)">
                        <option selected value="1">인기아이템</option>
                        <option value="2">여행물품</option>
                        <option value="3">반려동물용품</option>
                        <option value="4">기내용물품</option>
                        <option value="5">여행생활용품</option>
                    </select>
                    <el-button round @click="handleInsert()" style="float: right; font-size: 20px; font-weight: 600;">물품등록</el-button>
                </div>
                <el-table :data="state.list2" style="width: 90%">
                    <el-table-column prop="ino" label="상품번호" />
                    <el-table-column prop="name" label="상품명" />
                    <el-table-column prop="price" label="가격" />
                    <el-table-column prop="quantity" label="재고수량" />
                    <el-table-column prop="regdate" label="등록일" width="120" />
                    <el-table-column fixed="right" label="상품관리">
                        <template #default="scope">
                            <el-button text class="button" type="danger" style="font-weight: 600; margin-left: 10px;" @click="handleDeleteitem(scope.row.ino)" >삭제</el-button>
                            <el-button text class="button" type="primary" style="font-weight: 600;" @click="handleUpdate(scope.row.ino)" >수정</el-button>
                        </template>
                    </el-table-column>
                    <el-table-column fixed="right" width="120">
                    </el-table-column>
                </el-table>
                <br />
                <el-button style="margin-bottom:50px;" v-for="tmp in state.pages2" :key="tmp" @click="handlePage2(tmp)">
                    {{tmp}}
                </el-button>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
import axios from 'axios';
import { onMounted} from '@vue/runtime-core';
import { reactive} from '@vue/reactivity';
import { useRouter } from 'vue-router';
export default {
    setup () {
        const router = useRouter();

        const state = reactive({
            list : [],
            tmp : '',
            list1 : [],
            list2 : [],
            page : 1,
            page1 : 1,
            page2 : 1,
            token: sessionStorage.getItem("token"),
            pages : 0,
            pages1 : 0,
            pages2 : 0,
            count : 0,
            count1 : 0,
            count2 : 0,
            icateno : '1'
        });

        const handleData = async() => {
            const url = `/fligent/api/admin/mypage/selectmember.json?page=${state.page}`;
            const headers = {"Content-Type":"application/json", "TOKEN" : state.token};
            const { data } = await axios.get(url, {headers});
            if(data.status === 200) {
                state.list = data.list;
                state.count = data.count;
                state.pages = Math.floor(data.count/10+1);
            }
        };
        const handleData1 = async() => {
            const url = `/fligent/api/admin/mypage/selectboard.json?page=${state.page1}`;
            const headers = {"Content-Type":"application/json", "TOKEN" : state.token};
            const { data } = await axios.get(url, {headers});
            console.log(data.list)
            // console.log(state.token)
            if(data.status === 200) {
                state.list1 = data.list;
                for(const tmp of state.list1){
                    tmp.content = tmp.content.toString().substr(0, 30) + "...";
                }
                state.count1 = data.count;
                state.pages1 = data.pages;
            }
        };
        const handleData2 = async() => {
            const url = `/fligent/api/admin/item/selectitem.json?page=${state.page2}&icateno=${state.icateno}`;
            const headers = {"Content-Type":"application/json", "TOKEN" : state.token};
            const { data } = await axios.get(url, {headers});
            if(data.status === 200) {
                state.list2 = data.itemlist;
                state.count2 = data.count;
                state.pages2 = Math.floor(data.count/10+1);
            }
        };

        const onChange = async(event) => {
            state.icateno = event.target.value;
            handleData2();
        };
            
        const handlePage = (tmp) =>{
            state.page = tmp;
            handleData();
        };

        const handlePage1 = (tmp) =>{
            state.page1 = tmp;
            handleData1();
        };
        const handlePage2 = (tmp) =>{
            state.page2 = tmp;
            handleData2();
        };

        const handleBlock = async(userid) => {
            const url = `/fligent/api/admin/mypage/blockmember.json?userid=${userid}`;
            const headers = {"Content-Type":"application/json", "TOKEN" : state.token};
            const body = {

            }
            const {data} = await axios.post(url, body, {headers});
            if(data.status === 200){
                alert('변경되었습니다.')
                handleData();
            }
            else{
                alert('변경 실패했습니다.');
            }
        };

        const handleDeleteboard = async(bno) => {
            if(confirm('삭제할까요?')){
                const url = `/fligent/api/admin/mypage/deleteboard.json?bno=${bno}`;
                const headers = {"Content-Type":"application/json", "TOKEN" : state.token};
                const body = {
    
                }
                const {data} = await axios.post(url, body, {headers});
                if(data.status === 200){
                    alert('삭제되었습니다.')
                    handleData1();
                }
                else{
                    alert('삭제 실패했습니다.');
                }
            }
        };

        const handleDeleteitem = async(ino) => {
            if(confirm('삭제할까요?')){
                const url = `/fligent/api/admin/item/delete.json?itemno=${ino}`;
                const headers = {"Content-Type":"application/json", "TOKEN" : state.token};
                const body = {

                }
                const {data} = await axios.post(url, body, {headers});
                if(data.status === 200){
                    alert('삭제 되었습니다.');
                    handleData2();
                }
                else{
                    alert('삭제 실패했습니다.');
                }
            }
        };
        const handleUpdate = (ino) => {
            router.push({path:'/adminitemupdate', query:{ino : ino}});
        };

        const handleInsert = () => {
            router.push({path:'/adminiteminsert'});
        };

        onMounted(() =>{
            handleData();
            handleData1();
            handleData2();
        });

        return {
            state,
            onChange,
            handleBlock,
            handlePage,
            handlePage1,
            handlePage2,
            handleDeleteboard,
            handleDeleteitem,
            handleInsert,
            handleUpdate,
        }
    }
}
</script>

<style lang="css" scoped>
.container{
    width   : 100%;
    padding : 50px;
    text-align: center;
}
.container h3{
    font-family: 'MapoFlowerIsland';
    font-weight: 600;
    font-size: 35px;
    margin-bottom: 50px;
    /* margin-top: 50px; */
}
/* @font-face {
    font-family: 'MapoFlowerIsland';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/MapoFlowerIslandA.woff') format('woff');
    font-weight: normal;
    font-style: normal;
} */
</style>