<template>
    <div class="container">
        <h2 style="margin-top:200px;">주문 완료 되었습니다.</h2>
        <div class="button">
            <el-button round type="info" plain @click="handleCart()" style="font-size: 25px; font-weight: 600; height: 40px;">장바구니로 돌아가기</el-button>
            <el-button round type="primary" @click="handleOList()" style="font-size: 25px; margin-left: 250px; font-weight: 600; height: 40px;">주문목록</el-button>
        </div>
    </div>
</template>

<script>
import { useRouter } from 'vue-router';
export default {
    setup () {
        const router = useRouter();
        
        const handleCart = () => {
            router.push({path:'/cart'});
        };

        const handleOList = () => {
            router.push({path:'/customermypage', 
            query: {type: 1}}
            );
        };

        return {
            handleCart,
            handleOList,
        }
    }
}
</script>

<style lang="css" scoped>
.container{
    text-align: center;
    /* border:1px solid #cccccc; */
}
.button{
    /* border:1px solid #cccccc; */
    margin-top: 200px;
}
.container h2{
    font-family: 'MapoFlowerIsland';
    font-weight: 600;
}
/* @font-face {
  font-family: 'MapoFlowerIsland';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/MapoFlowerIslandA.woff') format('woff');
  font-weight: normal;
  font-style: normal;
} */
</style>