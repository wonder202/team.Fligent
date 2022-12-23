<template>
    <div class="container">
        <h3>상품등록</h3>
        <div class="insert">
            <el-form :model="form" label-width="120px">
                <el-form-item label="상품명">
                    <el-input type="text" v-model="state.name" style="width:400px;" :ref = "el => {form[0] = el}"/>
                </el-form-item>
                <el-form-item label="상품설명">
                    <el-input v-model="state.content" type="textarea" style="width:400px;" rows="10" :ref = "el => {form[1] = el}" />
                </el-form-item>
                <el-form-item label="카테고리">
                    <select class="form-select" v-model="state.icateno" aria-label="Default select example" style="width:200px;">
                        <option selected value="1">인기아이템</option>
                        <option value="2">여행물품</option>
                        <option value="3">반려동물용품</option>
                        <option value="4">기내용물품</option>
                        <option value="5">여행생활용품</option>
                    </select>
                </el-form-item>
                <el-form-item label="가격/개당">
                    <el-input type="text" v-model="state.price" style="width:200px;" :ref = "el => {form[2] = el}" />
                    <label style="margin:10px;">원</label>
                </el-form-item>
                <el-form-item label="수량">
                    <el-input-number v-model="state.quantity" :min="1" style="width:200px;" />
                </el-form-item>
                <el-form-item label="상품 이미지">
                    <input type="file" multiple @change="handleFile($event)" style="margin-left: 70px;" />
                </el-form-item>
                <div style="margin-top:100px;">
                    <el-button round @click="handleInsert()" type="primary" style="height: 35px; margin-right: 100px; font-size: 20px; font-weight: 600;">등록하기</el-button>
                    <el-button round @click="handleList()" type="info" plain style="height: 35px; font-size: 20px; font-weight: 600;">취소</el-button>
                </div>
            </el-form>
        </div>
    </div>
</template>

<script>
import { reactive, ref } from '@vue/reactivity';
import axios from 'axios';
import { useRouter } from 'vue-router';
export default {
    setup () {
        const router = useRouter();
        const form = ref([]);
        const state = reactive({
            file     : [],
            image    : null,
            name : '',
            content : '',
            price : 0,
            quantity : 0,
            icateno : 1,
            userid : 'admin',
            token: sessionStorage.getItem("token"),
        });
        const handleInsert = async() => {
            if(state.name === ''){
                alert('상품명을 입력하세요.');
                form.value[0].focus();
                console('aaa')
                return false;
            }
            
            if(state.content === ''){
                alert('상품설명을 입력하세요.');
                form.value[1].focus();
                return false;
            }

            if(state.price === 0){
                alert('가격을 입력하세요.');
                form.value[2].focus();
                return false;
            }

            if(state.file.length === 0){
                alert('이미지는 반드시 한장이상 첨부되어야 합니다!');
                return false;
            }

            const url = `/fligent/api/admin/item/insert.json`;
            const headers = {"Content-Type":"application/json", "TOKEN" : state.token};
            
            const body = {
                name :state.name,
                content :state.content,
                price :state.price,
                quantity :state.quantity,
                itemcate:{icateno:state.icateno}, //조인으로 하위 만들때 쓰는 로직
                userid :state.userid,
            }
            const { data } = await axios.post(url, body, {headers});
            // console.log(data);
            if(data.status === 200){
                //ino가 시퀀스로 형성되어 저장되면 ino를 handleIMGInsert에 불러와 저장시킴
                handleIMGInsert(data.result.ino);
                alert('상품등록완료');
                router.push({path:'/adminmypage'});
            }
        };

        const handleFile = (e) =>{
            if(e.target.files.length > 0) {
                for(let i=0; i<e.target.files.length; i++){
                    state.file[i] = e.target.files[i];
                }
            }
        };
        
        const handleIMGInsert = async(ino) => {
            const url = `/fligent/api/admin/item/image/insertbatch.json`;
            const headers ={"Content-Type":"multipart/form-data", "TOKEN" : state.token};
            let body = new FormData();
            for(let i=0; i<state.file.length; i++){
                body.append("file", state.file[i]);
                body.append(`list[${i}].item.ino`, ino);
            }
            const { data }  = await axios.post(url, body, {headers});
            console.log(data);
        };
        const handleList = () => {
            router.push({path:'/adminmypage'});
        };
        return {
            state,
            form,
            handleInsert,
            handleList,
            handleFile,
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
.insert{
    width: 50%;
    margin:0 auto;
    /* border:1px solid #cccccc; */
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