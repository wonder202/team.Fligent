<template>
    <div class="container">
        <div class="item">
            <!-- {{state.row}} -->
            <div class="row">
                <div class="col-7">
                    <el-carousel indicator-position="outside">
                        <el-carousel-item v-for="obj in state.imageurl" :key="obj">
                            <img :src="obj" style="width: 55%; height: 100%;"/>
                        </el-carousel-item>
                    </el-carousel>
                </div>
                <div class="col-5">
                    <el-form-item>
                        <label style="font-size: 30px; font-weight: 600;">{{state.row.name}}</label>
                    </el-form-item>
                    <hr />

                    <el-form>
                        <el-form-item label="개당">
                            <label v-html="state.row.price" style="font-size:20px; font-weight: 600; color:#A90F30;"></label>
                            <label>원</label>
                        </el-form-item>
                        
                        <el-form-item label="이 상품은?">
                            <label v-html="state.row.content" style="width: 300px; font-size:18px; font-weight: 600;"></label>
                        </el-form-item>
                        <br />
                        <hr />
                        <br />
                        <el-form-item label="주문수량을 입력하세요.">
                            <el-input-number v-model="state.cnt" style="width: 130px;"/>
                        </el-form-item>
                        <el-button round type="info" plain style="font-size: 20px; font-weight: 600; margin-top: 50px; margin-right: 100px;" @click="handleList">목록으로</el-button>
                        <el-button round type="primary" style="font-size: 20px; font-weight: 600; margin-top: 50px;" @click="handleCart">장바구니 담기</el-button>
                    </el-form>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { reactive } from '@vue/reactivity'
import { useRoute, useRouter} from 'vue-router'
import { onMounted } from '@vue/runtime-core';
import axios from 'axios';
export default {
    setup () {
        const route = useRoute();
        const router = useRouter();

        const state = reactive({
            ino         : Number(route.query.ino),
            row         : '', //상세 내용           
            imageurl    : [],
            cnt         : 1,
            token       : sessionStorage.getItem("token"),
        });

        const handleData = async() => {
            const url = `/fligent/api/item/selectoneitem.json?ino=${state.ino}`;
            const headers = {"Content-Type":"application/json"};
            const { data } = await axios.get(url, {headers});
            // console.log(data);
            if(data.status === 200){
                state.row = data.item;
                state.imageurl = data.item.imageurl;
            }
        };

        const handleCart = async() => {
            const url = `/fligent/api/cart/insertincart.json`;
            const headers = {"Content-Type":"application/json", "token" : state.token};
            if(state.token === null){
                alert('로그인 후 이용해주세요');
                router.push({path:'/memberlogin'});
            }
            const body = {
                cnt : state.cnt,
                ino : Number(route.query.ino),
            }
            const { data } = await axios.post(url, body, {headers});
            // console.log(data);
            if(data.status === 200){
                // alert('장바구니에 담겼습니다.')
                // if(confirm('장바구니로 이동할까요?')){
                //     router.push({path:'/cart'});
                // }
                
                if(confirm('상품이 장바구니에 담겼습니다!\n장바구니로 이동할까요?')){
                    router.push({path:'/cart'});
                }

            }
        };

        const handleList= () => {
            router.push({path:'/customeritem'});
        };

        onMounted(() => {
            handleData();
        });

        return {
            state,
            handleCart,
            handleList,
        }
    }
}
</script>

<style lang="css" scoped>
.container{
    text-align: center;
    width: 100%;
    padding: 20px;
    /* border:1px solid #cccccc; */

}
.item{
    margin-top: 100px;
    height: 600px;
    /* border:1px solid #cccccc; */
}
</style>


