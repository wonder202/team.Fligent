<template>
    <div class="container">
        <h3>게시글 수정</h3>
        <div class="img">
            <el-form-item label="첨부이미지">
                <div v-for="(obj, idx) of state.row.imgurllist" :key="obj" style="margin: 10px;">
                    <img :src="obj" style="width:300px; height: 200px; margin-bottom: 30px;"/>
                    <el-button link @click="handleImgDelete(state.imgno[idx])"><i class="bi bi-x-square fs-5"></i></el-button>
                </div>
            </el-form-item>
            <el-form-item label="이미지 추가">
                <img v-for="tmp in state.fileurl" :key="tmp" style="width:300px; height: 200px;" :src="tmp" />
            </el-form-item>
                <input type="file" name="filename" accept="image/gif, image/jpeg, image/png" multiple @change="handleFile($event)" style="margin-left: 70px;" />
        </div>
        <hr />
        <div class="info" v-if="state.row">
            <el-form-item label="작성자">
                <span style="font-size: 20px;">{{state.row.nickname}}</span>
            </el-form-item>
            <el-form-item label="제목">
                <el-input type="text" v-model="state.title" style="width:450px;" :ref = "el => {form[0] = el}" /><br />
            </el-form-item>
            <el-form-item label="내용">
                <el-input v-model="state.content" type="textarea" style="width:450px;" rows="10" :ref = "el => {form[1] = el}" /><br />
            </el-form-item>
            <el-form-item label="해시태그">
                {{state.hno1[idx]}}
                <div v-for="(obj, idx) of state.hashtag" :key="obj" style="margin: 10px;">
                    <el-button>#{{obj.name}}</el-button>
                    <el-button link @click="handleHashDelete(state.hno1[idx])"><i class="bi bi-x-square fs-5"></i></el-button>
                    <!-- <label style="margin:10px;"></label> -->
                </div>
            </el-form-item>
            <el-form-item label="해시태그 추가">
                <div>
                    <el-button v-for="(obj) in state.hashtag1" :key="obj" :color="obj.hcolor" @click="handlePush(obj.hno)" style="margin:10px;">#{{obj.name}}</el-button>
                    <label style="margin:10px;"></label>
                </div>
            </el-form-item>
            <div style="margin-top: 20px; width: 100%;">
                <el-button round @click="handleUpdate(state.row.bno)" type="primary" style="height: 35px; margin-right: 50px; font-size: 20px; font-weight: 600;">수정하기</el-button>
                <el-button round @click="handleList(state.row.bno)" type="info" plain style="height: 35px; font-size: 20px; font-weight: 600;">취소</el-button>
            </div>
        </div>
    </div>
</template>

<script>
import { reactive, ref} from '@vue/reactivity';
import { useRoute, useRouter } from 'vue-router'
import { onMounted, watch } from '@vue/runtime-core';
import axios from 'axios';

export default {

    setup() {
        const route = useRoute(); // 이전 페이지에 넘어온 값 받기
        const router = useRouter(); // 다른 페이지로 이동하기 위해 받음
        const form = ref([]);
        const state = reactive({
            bno         : Number(route.query.bno),
            row         : '',
            title       : '',
            content     : '',
            file        : [],
            fileurl     : [],
            imgurllist  : [],
            imgno       : [],
            hashtag     : [],
            hashtag1    : [],
            hno         : [],
            hno1        : [],
            selecthashtag : [], // 선택한 여러개의 해시태그 번호를 받을 배열
            hname       : [ '김포공항', '인천공항', '김해공항', '제주공항', '먹거리', '놀거리', 
                                '행사', '명소', '가족', '친구', '연인', '신혼' ], // 버튼 생성을 위한 해시태그의 이름
            hcolor      : ['#0339A6','#F2D1C9', '#0339A6','#F2D1C9','#0339A6','#F2D1C9','#0339A6','#F2D1C9','#0339A6','#F2D1C9', '#0339A6','#F2D1C9'], // 해시태그 버튼 색(2가지 반복)
            token       : sessionStorage.getItem("token"),
        });

        // 특정 변수의 값이 변경 되면 실행
        watch(state, () => {
            // console.log('watch =>', state);
        }, {
            immediate: true, // 즉시
            deep: true, // state 내부의 객체가 바뀌면
        });

        // 원래 글 정보 받아오기
        const handleData = async () => {
            const url = `/fligent/api/board/boardselectone.json?bno=${state.bno}`;
            const headers = { "Content-Type": "application/json" };
            const { data } = await axios.get(url, { headers });
            // console.log('가져온 이미지', state.imgno);
            // console.log('저장된 해시', data.hashtag);
            if (data.status === 200) {
                state.row = data.result;
                state.title = data.result.title
                state.content = data.result.content
                state.imgurllist = data.result.imgurllist;
                state.hashtag = data.hashtag;
                for(let i=0; i<state.imgurllist.length; i++){
                    state.imgno[i] = state.imgurllist[i].split('=')[1];
                    // console.log(state.imgno[i])
                }
                for(let i=0; i<state.hashtag.length; i++){
                    state.hno1[i] = state.hashtag[i].hno;
                    // console.log(state.hno[i])
                }

            }
        };

        const handleImgDelete = async(imgno) => {
            const url = `/fligent/api/board/deleteimage.json?bno=${state.bno}&bimageno=${imgno}`;
            const headers = {"Content-Type":"application/json"};
            const body = {

            }
            const {data} = await axios.post(url, body, {headers});
            if(data.status === 200){
                alert('삭제되었습니다')
                handleData();
            }
            else{
                alert('삭제 실패하였습니다')
            }
        };

        const handleFile = (e) =>{
            if(e.target.files.length == 0 ) { // 첨부한 파일이 존재하지 않는 경우
                state.fileurl = []; // 초기화
                // console.log("******** state.file => " , state.file)
            }
            else { // 첨부한 파일이 존재하는 경우
                for(let i=0; i<e.target.files.length; i++){
                    var pathpoint = e.target.files[i].name.lastIndexOf('.');
                    var filepoint = e.target.files[i].name.substring(pathpoint+1,e.target.files[i].name.length);
                    var filetype = filepoint.toLowerCase();
                    if(filetype=='jpg' || filetype=='gif' || filetype=='png' || filetype=='jpeg' || filetype=='bmp') {
                        // 정상적인 이미지 확장자 파일일 경우 ...
                        state.file[i] = e.target.files[i];
                        if(e.target.files && e.target.files[i]){
                            state.fileurl[i] = URL.createObjectURL(e.target.files[i])
                        }
                    } else {
                        alert('이미지 파일만 선택할 수 있습니다.');
                        return false;
                    }
                }
            }
        };
        
        // 이미지 재등록
        const handleIMGInsert = async(bno) => {
            const url = `/fligent/api/board/boardinsertbatchimage.json`;
            const headers ={
                "Content-Type":"multipart/form-data"
            };
            // 이미지가 있는 경우  body 정보 담기
            let body = new FormData();
            // console.log(state.file.length)
            for(let i=0; i<state.file.length; i++){
                body.append("file", state.file[i]);
                body.append("bno", bno);
            }
            const { data }  = await axios.post(url, body, headers);
            if(data.status !== 200){
                alert('수정 실패하였습니다.')
            }
        };

        //해시태그 삭제
        const handleHashDelete = async(hno1) => {
            const url = `/fligent/api/board/hashtagmappingdelete.json`;
            const headers = {
                "Content-Type":"application/json",
                "token": state.token
            };
            const body = [{
                bno : state.bno,
                hno : hno1
            }]
            // console.log(body)
            const {data} = await axios.post(url, body, {headers});
            if (data.status === 200) {
                handleData();
            }
        };

        // 해시태그 DB 정보 가져오기
        var clonecolor = [];
        const hasgtagData = async() => {
            const url = `/fligent/api/board/hashtagdataget.json`
            const headers = {"Content-Type" : "application/json"}
            const {data} = await axios.get(url, headers)
            // console.log('추가 해시',data.list)
            if(data.status == 200){
                state.hashtag1 = data.list;
                clonecolor = JSON.parse(JSON.stringify(data.list));
            }
        };
        // 해시태그 배열에 담기
        const handlePush = (hno) => {
            var chk = 0;
            for(var j = 0; j<state.selecthashtag.length; j++){
                if(state.selecthashtag[j] == hno){
                    chk = 1;
                    state.selecthashtag.splice(j,1);
                    break;
                }
                else{
                    chk = 0;
                }
            }
            if(chk == 0){
                state.selecthashtag.push(hno);
                state.hashtag1[hno-1].hcolor = '#cccccc';
                // console.log(state.selecthashtag.length)
            }
            else if(chk == 1){
                state.hashtag1[hno-1].hcolor = clonecolor[hno-1].hcolor;
            }
        };

        const handleInsertHashtag = async(bno) => {
            const body = [];
            for(let i=0; i<state.selecthashtag.length; i++){
                body.push({"bno":bno, "hno":state.selecthashtag[i]});
            }
            const url = `/fligent/api/board/hashtagmapping.json`;
            const headers = {"Content-Type":"application/json"};
            // console.log("body",body[0]);
            const ret = await axios.post(url, body, {headers});
            if(ret.status !== 200){
                
                alert('수정 실패하였습니다.')
            }
        };

        // 게시글 수정하기
        const handleUpdate = async (bno) => {
            if(state.title ===''){
                alert('제목을 입력하세요.');
                form.value[0].focus();
                return false;
            }
            
            if(state.content ===''){
                alert('내용을 입력하세요.');
                form.value[1].focus();
                return false;
            }
            // /fligent/api/board/updateoneboard.json
            const url = `/fligent/api/board/updateoneboard.json`;
            const headers = { 
                "Content-Type": "application/json", 
                "token": state.token 
            };
            const body = {
                bno: state.bno,
                title: state.row.title,
                content: state.row.content
            };

            const response = await axios.put(url, body, { headers });
            if (response.data.status === 200) {
                handleIMGInsert(state.bno);
                handleInsertHashtag(state.bno);
                alert('수정되었습니다.')
                router.push({path:'boardselectone', query:{bno:bno}});
            }
        };

        const handleList = (bno) => {
            router.push({path:'boardselectone', query:{bno:bno}});
        };

        onMounted(() => {
            handleData();
            hasgtagData();
        });

        return {
            state,
            form,
            handleUpdate,
            handleFile,
            handleList,
            handleImgDelete,
            handleHashDelete,
            handlePush,
            handleInsertHashtag
        }
    }
}
</script>

<style lang="css" scoped>

.container{
    /* border: 1px solid #cccccc; */
    text-align: center;
    padding: 30px;
}
.container h3 {
    margin-bottom: 70px;
    font-family: 'MapoFlowerIsland';
    font-size: 40px;
    text-align: center;
    /* padding: 35px; */
    font-weight: 600;
}
.img{
    width: 65%;
    /* border: 1px solid #cccccc; */
    margin: auto;
}
.info{
    width: 40%;
    /* border: 1px solid #cccccc; */
    margin: auto;
}
.scrollbar-flex-content {
    display: flex;
}

.scrollbar-demo-item {
    flex-shrink: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    width: 160px;
    height: 160px;
    margin: 1%;
    text-align: center;
    border-radius: 4px;
    background: var(--el-color-danger-light-9);
    color: var(--el-color-danger);
}
</style>