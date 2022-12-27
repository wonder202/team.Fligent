<template>
    <div class="container">
        <h3>상품수정</h3>
        <div class="update">
            <div class="img">
                <el-form-item label="첨부이미지">
                    <div v-for="(obj, idx) of state.imgurl" :key="obj" style="margin: 10px;">
                        <img :src="obj" style="width:200px; height: 200px; margin-bottom: 30px;"/>
                        <el-button link @click="handleImgDelete(state.imgno[idx])"><i class="bi bi-x-square fs-5"></i></el-button>
                    </div>
                </el-form-item>
                <el-form-item label="이미지 추가">
                    <img v-for="tmp in state.fileurl" :key="tmp" style="width:200px; height: 200px;" :src="tmp" />
                    <input type="file" name="filename" accept="image/gif, image/jpeg, image/png" multiple @change="handleFile($event)" style="margin-left: 70px;" />
                </el-form-item>
            </div>
            <el-form label-width="150px">
                <el-form-item label="상품번호">
                    <el-input v-model="state.ino" style="width:400px" readonly />
                </el-form-item>
                <el-form-item label="상품명">
                    <el-input v-model="state.name" style="width:400px"/>
                </el-form-item>
    
                <el-form-item label="상품설명">
                    <el-input v-model="state.content" type="textarea" style="width:400px;" rows="10" />
                </el-form-item>       
    
                <el-form-item label="가격" >
                    <el-input v-model="state.price" style="width:400px"/>
                </el-form-item>
    
                <el-form-item label="재고수량" >
                    <el-input v-model="state.quantity" style="width:400px"/>
                </el-form-item>
    
                <el-form-item>
                    <el-button round @click="handleUpdate()" type="primary" style="height: 35px; margin-left: 15px; margin-right: 190px; font-size: 20px; font-weight: 600;">수정하기</el-button>
                    <el-button round @click="handleList()" type="info" plain style="height: 35px; font-size: 20px; font-weight: 600;">취소</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
import { onMounted} from '@vue/runtime-core'
import { reactive } from '@vue/reactivity';
import { useRoute, useRouter } from 'vue-router'
export default {
    setup () {
        const route = useRoute();
        const router = useRouter();

        const state = reactive({
            token: sessionStorage.getItem("token"),
            row     : '',
            name    : '',
            content : '',
            price   : '',
            quantity: '',
            ino     : Number(route.query.ino),
            file    : [],
            fileurl : [],
            imgurl  : [],
            imgno   : []
        });

        const handleData = async() => {
            const url =`/fligent/api/admin/item/selectoneitem.json?ino=${state.ino}`;
            const headers = {
                "Content-Type":"application/json",
                "TOKEN" : state.token
            };
            const { data } = await axios.get(url, {headers});

            if(data.status === 200) {
                state.name = data.item.name;
                state.content = data.item.content;
                state.price = data.item.price;
                state.quantity = data.item.quantity;
                if(data.item.imageurl !== null ){
                    state.imgurl = data.item.imageurl;
                    for(let i=0; i<state.imgurl.length; i++){
                        state.imgno[i] = state.imgurl[i].split('=')[1];
                    }
                }
            }
        };

        const handleImgDelete = async(imgno) => {
            const url = `/fligent/api/admin/item/image/deleteimage.json?ino=${state.ino}&iimageno=${imgno}`;
            const headers = {"Content-Type":"application/json", "TOKEN" : state.token};
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
        const handleIMGInsert = async(ino) => {
            const url = `/fligent/api/admin/item/image/iteminsertbatchimage.json`;
            const headers ={
                "Content-Type":"multipart/form-data",
                "TOKEN" : state.token
            };
           // 이미지가 있는 경우  body 정보 담기
           let body = new FormData();
            // console.log(state.file.length)

            for(let i=0; i<state.file.length; i++){
                body.append("file", state.file[i]);
                body.append("ino", ino);
            }
            
            const { data }  = await axios.post(url, body,{headers});
            if (data !== null) {
                alert('이미지가 등록 되었습니다.');
            }
        };
        const handleUpdate = async() => {
            const url =`/fligent/api/admin/item/updatepost.json`;
            const headers = {
                "Content-Type":"application/json",
                "token" : state.token
            };
            const body = {
                ino :state.ino,
                name :state.name,
                content : state.content,
                price : state.price,
                quantity : state.quantity
            };

            const { data } = await axios.post(url, body, {headers});
            // console.log(data);
            if(data.status === 200){
                handleIMGInsert(state.ino);
                alert('정보가 변경되었습니다.');
                router.push({path:'/adminmypage'});
            }
            else{
                alert('다시 시도해 주세요.');     
            }
        };

        const handleList = () => {
            router.push({path:'/adminmypage'});
        }

        onMounted(()=>{
            handleData();
        });

        return {
            state,
            handleUpdate,
            handleList,
            handleFile,
            handleImgDelete,
            handleIMGInsert
        }
    }
}
</script>

<style lang="css" scoped>
.container{
    width: 80%;
    padding:50px;
    text-align:center;
    /* border:1px solid #cccccc; */
}
.update{
    width: 50%;
    margin:0 auto;
    /* border:1px solid #cccccc; */
}
.img{
    margin-bottom: 30px;
}
.container h3{
    font-family: 'MapoFlowerIsland';
    font-weight: 600;
    font-size: 35px;
    margin-bottom: 50px;
}
/* @font-face {
    font-family: 'MapoFlowerIsland';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/MapoFlowerIslandA.woff') format('woff');
    font-weight: normal;
    font-style: normal;
} */
</style>