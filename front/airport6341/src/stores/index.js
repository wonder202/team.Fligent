import { createStore } from 'vuex';
import axios from 'axios';

export default createStore({
    state() {
        let tmp = false;
        if(sessionStorage.getItem("token") !== null){
            tmp = true;
        }
        return {
            logged   : tmp,
            counter  : 1,
            imageurl : ''
        }
    },

    getters : { // App.vue에서 값을 가져 감
        getLogged : state => {
            return state.logged;
            
        },
        getCounter : state => {
            return state.counter;
        },
        getImageurl : state => {
            return state.imageurl;
        }
    },

    mutations:{ // LoginPage.vue, LogoutPage.vue
        setLogged(state, value){
            state.logged = value;
        },
        setCounter(state) {
            state.counter++;
        },
        setImageurl(state, value) {
            state.imageurl = value;
        }
    },

    actions:{
        async handleImg(context) {
            if(sessionStorage.getItem("token") == null){return;}
            const url =`/fligent/api/customer/selectmemberimglist.json`;
            const headers = {
                "Content-Type":"application/json",
                "token" : sessionStorage.getItem("token")
            };
            // console.log(headers)
            const {data} = await axios.get(url, {headers});
            // console.log('11111111111111111',data.result)
            if(data.status === 200) {
                context.commit('setImageurl', data.result);
            }
        }
    }
});

