<template>
    <div class="container">
        <div style="text-align:center">
            <h3> </h3>
        </div>
    </div>
</template>

<script>
import { reactive } from '@vue/reactivity'
import { onMounted } from '@vue/runtime-core'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router';
import axios from 'axios';
export default {
    setup () {
        const store  = useStore();
        const router = useRouter();

        const state = reactive({
            token    : sessionStorage.getItem("token"),
        });
        const handleData = async() => {
            if(confirm('로그아웃 할까요?')){       
                const url =`/fligent/api/member/logout.json`;
                const headers = {
                    "Content-Type":"application/json",
                    "TOKEN" : state.token
                };
                const body = {};
                const {data} = await axios.post(url, body, {headers});
                // console.log(data);     
                if(data.status === 200){
                        alert('로그아웃 되었습니다.');
                    sessionStorage.removeItem("token");
                    store.commit('setLogged', false);  
                    router.push({path:'/'});                       
                }
                else{
                    alert('로그아웃 실패');
                    router.push({path:'/'});    
                }            
            }    
            else{
                alert('로그아웃 취소 되었습니다. 홈으로 이동합니다.');
                router.push({path:'/'});  
            }                      
        };   

        onMounted(()=>{
            handleData();
        })

        return {}
    }
}
</script>

<style lang="scss" scoped>

</style>

