<template>
    <div class="container">
        <h3>게시글 등록</h3>
        <div class="board">
            <el-form :model="form" label-width="120px">
                <el-form-item label="제목">
                    <el-input v-model="state.title" style="width:80%;" :ref = "el => {form[0] = el}" />
                </el-form-item>
                <el-form-item label="내용">
                    <el-input v-model="state.content" type="textarea" style="width:80%;" rows="10" :ref = "el => {form[1] = el}" />
                </el-form-item>
                <el-form-item label="해시태그">
                    <div class="hash">
                        <el-button v-for="(obj) in state.hashtag" :key="obj" :color="obj.hcolor" @click="handlePush(obj.hno)" style="margin:10px;">#{{obj.name}}</el-button>
                        <label style="margin:10px;"></label>
                    </div>
                </el-form-item>
                <el-form-item label="첨부이미지">
                    <!-- {{state.fileurl}} -->
                    <img v-for="obj in state.fileurl" :key="obj" style="width:300px; height: 200px;" :src="obj" />
                    <input type="file" name="filename" accept="image/gif, image/jpeg, image/png, image/jfif" multiple @change="handleFile($event)" style="margin-left: 70px;" />
                </el-form-item>
                <div class="btn">
                    <el-button round type="primary" @click="handleInsert()" style="height: 35px; font-size: 20px; margin-right: 50px; font-weight: 600; ">등록하기</el-button>
                    <el-button round plain type="info" @click="handleList()" style="height: 35px; font-size: 20px; font-weight: 600;">취소</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>
<script>
import { reactive, ref  } from '@vue/reactivity'
import axios from 'axios';
import { useRouter } from 'vue-router';
import { onMounted } from '@vue/runtime-core';
export default {
    setup () {
        const router = useRouter();
        const form = ref([]);

        const state = reactive({
            file     : [],
            fileurl  : [],
            image    : null,
            title    : '',
            content  : '',
            token    : sessionStorage.getItem("token"),
            userid   : '',
            hashtag : [], // 해시태그의 번호를 받을 배열 선언
            selecthashtag : [], // 선택한 여러개의 해시태그 번호를 받을 배열
            hname : [ '김포공항', '인천공항', '김해공항', '제주공항', '먹거리', '놀거리', 
                        '행사', '명소', '가족', '친구', '연인', '신혼' ], // 버튼 생성을 위한 해시태그의 이름
            hcolor : ['#0339A6','#F2D1C9', '#0339A6','#F2D1C9','#0339A6','#F2D1C9','#0339A6','#F2D1C9','#0339A6','#F2D1C9', '#0339A6','#F2D1C9'], // 해시태그 버튼 색(2가지 반복)
        });
        
        //state.file의 변수에 수동으로 첨부 이미지 정보를 넣기
        // var fileaaa = '';
        const handleFile = (e) =>{
            if(e.target.files.length == 0 ) { // 첨부한 파일이 존재하지 않는 경우
                state.fileurl = []; // 초기화
            }
            else { // 첨부한 파일이 존재하는 경우
                for(let i=0; i<e.target.files.length; i++){
                    var pathpoint = e.target.files[i].name.lastIndexOf('.');
                    var filepoint = e.target.files[i].name.substring(pathpoint+1,e.target.files[i].name.length);
                    var filetype = filepoint.toLowerCase();
                    if(filetype=='jpg' || filetype=='gif' || filetype=='png' || filetype=='jpeg' || filetype=='bmp' || filetype=='jfif') {
                        // 정상적인 이미지 확장자 파일일 경우 ...
                        state.file[i] = e.target.files[i];
                        if(e.target.files && e.target.files[i]){
                            state.fileurl[i] = URL.createObjectURL(e.target.files[i])
                            // console.log(state.fileurl)
                        }
                    }
                    else {
                        alert('이미지 파일만 선택할 수 있습니다.');
                        return false;
                    }
                }
            }
        };

        // 게시글 등록(글정보만)
        const handleInsert = async () => {
            if(state.title===''){
                alert('제목을 입력하세요.');
                form.value[0].focus();
                return false;
            }
            
            if(state.content===''){
                alert('내용을 입력하세요.');
                form.value[1].focus();
                return false;
            }

            if(state.selecthashtag.length == 0){
                alert('해쉬태그를 최소 1개 선택해주세요.');
                return false;
            }

            if(state.file.length === 0){
                alert('이미지는 반드시 한장이상 첨부되어야 합니다!');
                return false;
            }

            const url = `/fligent/api/board/insertboardone.json`;
            const headers = {
                "Content-Type":"application/json",
                "TOKEN" : state.token
            }
            const body = {   //userid 가져와서 닉네임 받아야함
                title    : state.title,
                content : state.content, // 이미지를 표시하는 태그만 포함
                userid   : state.userid
            }

            const {data} = await axios.post(url, body, {headers});
            if(data.status === 200) {
                handleIMGInsert(data.bno);
                handleInsertHashtag(data.bno);
            }
            else{
                alert('등록에 실패하였습니다.');
                router.push({path:'/boardwrite'});
            }   
        };


        // 이미지 등록 
        const handleIMGInsert = async(bno) => {
            const url = `/fligent/api/board/boardinsertbatchimage.json`;
            const headers ={
                "Content-Type":"multipart/form-data"
            };
            // 이미지가 있는 경우  body 정보 담기
            let body = new FormData();
            for(let i=0; i<state.file.length; i++){
                body.append("file", state.file[i]);
                body.append("bno", bno);
            }
            const { data }  = await axios.post(url, body, headers);
            if(data.status == 200){
                alert('게시글이 등록되었습니다!')
                router.push({path:'/board'});
            } 
            else{
                alert('게시글 등록에 실패하였습니다.\n다시 시도해주세요');
            }
        };
        // 해시태그 DB 정보 가져오기
        var clonecolor = [];
        const hasgtagData = async() => {
            const url = `/fligent/api/board/hashtagdataget.json`
            const headers = {"Content-Type" : "application/json"}
            const {data} = await axios.get(url, headers)
            if(data.status == 200){
                state.hashtag = data.list;
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
                state.hashtag[hno-1].hcolor = '#cccccc';
            }
            else if(chk == 1){
                state.hashtag[hno-1].hcolor = clonecolor[hno-1].hcolor;
            }
        }

        // 해시태그 등록
        const handleInsertHashtag = async(bno) => {
            const body = [];
            for(let i=0; i<state.selecthashtag.length; i++){
                body.push({"bno":bno, "hno":state.selecthashtag[i]});
            }
            const url = `/fligent/api/board/hashtagmapping.json`;
            const headers = {"Content-Type":"application/json"};
            // console.log("body",body[0]);
            const ret = await axios.post(url, body, {headers});
            console.log("ret",ret);
        }

        onMounted(()=>{
            hasgtagData();
        });

        // 글목록으로 돌아가기
        const handleList = () => {
            router.push({path:'/board'});
        };

        onMounted(()=>{
            // 해시태그 정보 가져오기
            hasgtagData();
        });

        return {
            state, 
            form,
            handleInsert, 
            handleList,
            handleFile,
            handlePush,
            handleInsertHashtag
        };
    }
}
</script>

<style lang="css" scoped>
.container{
    width: 100%;
    padding: 30px;
    margin: 10px auto 0px auto;
    /* border: 1px solid #cccccc; */
}
.board{
    /* border: 1px solid #cccccc; */
    width: 60%;
    margin: auto;
}
.hash{
    /* border: 1px solid #cccccc; */
    width: 80%;
}
.container h3 {
    margin-bottom: 70px;
    font-family: 'MapoFlowerIsland';
    font-size: 40px;
    text-align: center;
    /* padding: 35px; */
    font-weight: 600;
}
.btn{
    margin-top:30px; 
    width: 100%;
    /* border: 1px solid #cccccc; */
}
</style>