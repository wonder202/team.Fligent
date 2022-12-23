<template>
    <div class="container">
        <h3>장바구니</h3>
        <el-table :data="state.list" class="table" style="width: 90%; text-align: center; font-size: 17px;">
            <el-table-column label="체크" width="100">
                <template #default="scope">
                    <!-- {{scope.row.cno}} -->
                    <input type="checkbox" v-model="state.chk" :value="scope.row.cno" />
                </template>
            </el-table-column>
            <el-table-column prop="name" label="상품명" width="180" />
            <el-table-column prop="price" label="상품가격/개당" width="180" />
            <el-table-column prop="cnt" label="주문수량" />
            <el-table-column prop="tot" label="총 상품가격" />
            <el-table-column label="주문관리" width="200">
                <template #default="scope">
                    <el-button text @click="handleDelete(scope.row.cno)" style="font-weight: 600; color: red;">삭제</el-button>
                </template>
            </el-table-column>
        </el-table>
        <ul class ="paginateion">
            <el-button v-for="tmp in state.pages" :key="tmp" @click="itemPages(tmp)">
                {{tmp}}
            </el-button>
        </ul>
        <div class="butt">
            <el-button round @click="handleOrder()" type="primary" style="font-weight: 600; font-size: 20px; float: right;">주문하기</el-button>
            <el-button link @click="handleDeletebatch()" type="danger" style="font-weight: 600; font-size: 18px; float: right; margin-right: 20px;">선택 삭제</el-button>
            <el-button round @click="handleList()" type="info" plain style="font-size: 20px; float: left; font-weight: 600;">계속 쇼핑하기</el-button>
        </div>
    </div>
</template>

<script>
import { onMounted, reactive } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
export default {
    setup () {
        const router = useRouter();
        const state = reactive({
            chk   : [],
            list  : [],
            token : sessionStorage.getItem("token"),
            cnt   : '',
            page  : 1,
            pages : 0,
            total : 0,
        });

        const itemPages = (tmp) =>{
            state.page = tmp;
            handleData();
        }; 

        const handleData = async() => {
            const url = `/fligent/api/cart/selectcartlist.json`;
            const headers = {
                "Content-Type":"application/json",
                "token" : state.token
            };
            const { data } = await axios.get(url, {headers});
            console.log(data);
            if(data.status === 200){
                state.list = data.list;
                state.total = data.count;
                for(const item of state.list){
                    item.price = item.price.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
                    item.tot = item.tot.toString().replace(/\B(?<!\.\d*)(?=(\d{3})+(?!\d))/g, ",");
                }
            }
        };

        onMounted(() => {
            handleData();
        });

        const handleDelete = async(cno) => {
            if(confirm('삭제할까요?')){
                const url = `/fligent/api/cart/deletecart.json?cno=${cno}`;
                const headers = {
                    "Content-Type":"application/json", 
                    "token" : state.token
                };
                const body = {
                    
                }
                const {data} = await axios.post(url, body, {headers});
                // console.log(data);
                if(data.status === 200){
                    alert('삭제 되었습니다.');
                    handleData();
                }
            }
        };
        const handleDeletebatch = async() => {
            if(confirm('선택한 상품을 삭제할까요?')){
                const url = `/fligent/api/cart/deletebatchcart.json`;
                // console.log(url)
                const headers = {
                    "Content-Type":"multipart/form-data",
                    "token" : state.token
                };
                // console.log(headers);
                // console.log(state.chk.length);

                let body = new FormData();
                for(let i=0; i<state.chk.length; i++){
                    // console.log(state.chk[i])
                    body.append("cno", state.chk[i])
                }
                // console.log(body)
                const {data} = await axios.post(url, body, {headers});
                // console.log(data);
                if(data.status === 200){
                    alert('삭제 되었습니다.');
                    handleData();
                }
            }
        };
    
        const handleOrder = async() => {
            // console.log(state.chk.length)
            if(state.chk.length >= 1){
                router.push({path:'/order', query:{cno : state.chk}});
            }
            else if(state.chk.length === 0){
                alert('주문할 상품을 체크해주세요.')
            }
        };

        const handleList = () => {
            router.push({path:'/customeritem'});
        };

        return {
            state,
            handleOrder,
            handleList,
            handleDelete,
            itemPages,
            handleDeletebatch
        }
    }
}
</script>

<style lang="css" scoped>
.container{
    text-align: center;
    /* border:1px solid #cccccc; */
    width: 70%;
}
.container h3{
    font-family: 'MapoFlowerIsland';
    font-weight: 600;
    font-size: 50px;
    margin-bottom: 50px;
    margin-top: 50px;
}
.butt{
    width: 90%;
    margin: 0 auto;
}
.el-button{
    height: 40px;
}
.table{
    /* border:1px solid #cccccc; */
    margin: 0 auto;
    margin-bottom: 50px;
}
/* @font-face {
    font-family: 'MapoFlowerIsland';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/MapoFlowerIslandA.woff') format('woff');
    font-weight: normal;
    font-style: normal;
} */
</style>