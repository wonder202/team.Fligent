<template>
    <div class="container">
        <h3>비밀번호 찾기</h3>
        <div class="info">
            <div style="width: 85%; margin: auto; padding: 20px;">
                <h6 style="color: #cccccc;">회원가입시 입력한 정보를 기재해주세요.</h6>
                <el-form label-width="120px">
                    <el-form-item label="아이디">
                        <el-input style="width:300px" v-model="state.userid" :ref = "el => {form[0] = el}"/>
                    </el-form-item>
                    <el-form-item label="생년월일">
                        <div class="demo-date-picker">
                            <el-date-picker type="date" v-model="state.birth" 
                                placeholder="생년월일을 고르세요"
                                style="width:300px; float: right;"
                                format="YYYY/MM/DD"
                                value-format="YYYYMMDD"
                                :ref = "el => {form[1] = el}" />
                        </div>
                    </el-form-item>
                    <el-form-item label="연락처">
                      <el-input style="width:90px" v-model="state.phone" :ref = "el => {form[2] = el}"/>
                      <label>-</label>
                      <el-input style="width:100px" v-model="state.phone1" :ref = "el => {form[3] = el}"/>
                      <label>-</label>
                      <el-input style="width:100px" v-model="state.phone2" :ref = "el => {form[4] = el}"/>
                    </el-form-item>
                </el-form>
                <el-button round @click="handleSendMail()" style="float: right; margin-right: 110px; font-size: 15px; font-weight: 600;">인증코드 발송</el-button>
            </div>
            <br />
            <div v-if="state.sendresult == 1" style="width: 75%; margin: auto; padding: 20px;">
                <hr />
                <el-form-item>
                    <h6 style="color: #cccccc;">이메일로 발송된 인증코드를 입력하세요.</h6>
                    <el-input style="width:300px" v-model="state.userinputcode" :ref = "el => {form[5] = el}"/>
                    <el-button round @click="handleCodeCheck()" style="margin-left: 20px; font-size: 15px; font-weight: 600;" >인증코드 확인</el-button>
                </el-form-item>
                <hr />
            </div>
            <div v-if="state.codeckeckret == 1" style="margin: auto; width: 75%;">
                <el-form :model="form" label-width="120px">
                    <el-form-item label="*변경할 암호">
                        <el-input type="password" style="width:200px" v-model="state.newpw" :ref = "el => {form[6] = el}"/><br />
                    </el-form-item>
                        
                    <el-form-item label="*변경할 암호 확인">
                        <el-input type="password" style="width:200px" v-model="state.newpw1" :ref = "el => {form[7] = el}"/>
                        <el-button round @click="handleSetPw()" style="margin-left: 20px; font-size: 15px; font-weight: 600;" >암호 변경하기</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script>
import { reactive, ref } from 'vue';
import axios from 'axios';
import { useRouter } from 'vue-router';
export default {
    setup () {
        const router = useRouter();
        const form = ref([]);
        const state = reactive({
            userid : '',
            birth  : '',
            phone  : '',
            phone1 : '',
            phone2 : '',
            sendresult : 0, // 인증번호입력 input 태그 v-if를 위한 변수
            mailcode : '', // 이메일로 받은 인증코드
            userinputcode : '', // 사용자가 입력한 인증코드
            codeckeck : ' 인증코드입력 ', // 아이디 중복확인처럼 변경하고싶어요
            codeckeckret : 0, // 인증코드 비교결과 (일치시 1로 변경)
            newpw : '', // 변경할 암호
            newpw1 : '' // 변경할 암호 확인용
        });

        // 사용자에게 인증 이메일 발송
        const handleSendMail = async() => {
            if(state.userid ===''){
                alert('아이디를 입력하세요')
                form.value[0].focus();
                return false;
            }
            if(state.birth ===''){
                alert('생년월일을 입력하세요')
                form.value[1].focus();
                return false;
            }
            if(state.phone ===''){
                alert('연락처를 입력하세요')
                form.value[2].focus();
                return false;
            }
            if(state.phone1 ===''){
                alert('연락처를 입력하세요')
                form.value[3].focus();
                return false;
            }
            if(state.phone2 ===''){
                alert('연락처를 입력하세요')
                form.value[4].focus();
                return false;
            }
            const url = `/fligent/api/member/sendmail.json`;
            const headers = {"Content-Type":"application/json"};
            const body = {
                userid  : state.userid,
                birth   : state.birth,
                phone   : `${state.phone}-${state.phone1}-${state.phone2}`
            }
            const {data} = await axios.post(url, body, {headers});
            // console.log(data);
            if(data.status === 200){
                alert('인증코드가 [ '+ state.userid + ' ] 로 발송되었습니다!\n전송받은 인증코드를 입력해주세요');
                state.sendresult = 1;

                // 이메일로 발송된 인증코드 변수에 저장
                state.mailcode = data.code;
                // console.log("메일로 발송된 코드!!!!!!!!!!! data => ", data.code)
                // console.log("이메일로 발송된 인증코드 => ",state.mailcode);
            } else {
                alert('사용자 정보가 일치하지 않습니다')
            }
        }   

        // 메일로 발송된 인증코드와 사용자가 입력한 인증코드가 일치하는지 확인
        const handleCodeCheck = () => {
            if(state.userinputcode ===''){
                alert('인증코드를 입력하세요')
                form.value[5].focus();
                return false;
            }
            if(state.mailcode == state.userinputcode){ // 이메일로 발송된 인증코드와 사용자가 입력한 인증코드가 일치하는 경우
                alert('인증코드가 일치합니다!\n새 비밀번호를 설정해주세요')
                state.codeckeckret = 1;
            } else {
                alert('인증코드가 일치하지 않습니다\n다시 확인하여 입력해주세요')
            }
        }

        // 암호 변경하기
        const handleSetPw = async() => {
            //  유효성 검사
            // newpw 와 newpw1 비어있는지 확인
            // newpw 와 newpw1 일치 확인
            if(state.newpw ===''){
                alert('변경할 암호를 입력하세요')
                form.value[6].focus();
                return false;
            }
            if(state.newpw1 ===''){
                alert('변경할 암호 확인값을 입력하세요')
                form.value[7].focus();
                return false;
            }
            if(state.newpw != state.newpw1){
                alert('입력한 암호와 확인값이 일치하지 않습니다')
                form.value[6].focus();
                return false;
            }
            
            const url = `/fligent/api/member/setnewpw.json`;
            const headers = {"Content-Type":"application/json"};
            const body = {
                userid  : state.userid,
                userpw  : state.newpw
            }
            const { data } = await axios.put(url, body, {headers});
            // console.log("@@@@@@@@@@2 암호변경 완료!!!!!!!!!!! data => ", data );
            if(data.status == 200){
                alert('암호변경이 완료 되었습니다!\n로그인 페이지로 이동합니다')
                router.push({path:'/memberlogin'});
            } else {
                alert('오류가 발생했습니다! 다시 시도해주세요')
                router.go(0);
            }
        }

        return {
            state,
            form,
            handleSendMail,
            handleCodeCheck,
            handleSetPw
        }
    }
}
</script>

<style lang="css" scoped>
.container{
    width: 50%;
    height: 500px;
    /* border:1px solid #cccccc; */
    text-align: center;
}
.container h3 {
    margin-top: 100px;
    margin-bottom: 50px;
    margin-left: 50px;
    font-family: 'MapoFlowerIsland';
    font-size: 40px;
    font-weight: 600;
}
.info{
    margin: auto;
    /* border:1px solid #cccccc; */
    width: 80%;
    text-align: center;
}
/* @font-face {
    font-family: 'MapoFlowerIsland';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/MapoFlowerIslandA.woff') format('woff');
    font-weight: normal;
    font-style: normal;
} */
</style>