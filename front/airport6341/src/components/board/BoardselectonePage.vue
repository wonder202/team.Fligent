<template>
    <div class="container">
        <div class="item">
            <div class="row">
                <!-- {{state.row}} -->
                <div class="col-7">
                    <el-carousel indicator-position="outside" style="margin-top: 30px;">
                        <el-carousel-item v-for="obj in state.row.imgurllist" :key="obj">
                            <img :src="obj" style="width: 60%; height: 100%;" />
                        </el-carousel-item>
                    </el-carousel>
                    <h4 style="font-weight: 600;">{{state.row.title}}</h4><br />
                    
                    <div style="width: 100%; margin-bottom: 20px;">
                        <div style="float: right; margin-right: 100px; margin-left: 10px;">조회 {{ state.row.hit }}회</div>
                        <div style="float: right; margin-left: 200px;">좋아요 {{ state.lcnt }}개</div>
                        <span style="color:#cccccc;">by. </span>{{ state.row.nickname}}
                    </div>
                    <div style="width: 80%; margin: auto; font-size:18px; font-weight: 600;">
                        {{state.row.content}}<br />
                    </div><br />
                    <div style="width: 70%; margin: auto;">
                        <span v-for="(obj) in state.hashtag" :key="obj" style="margin:10px; font-weight: 600; color: #ee9c9c;">#{{obj.name}}</span>
                    </div>
                    <span style="margin-bottom: 10px; font-size: 12px;"><i class="bi bi-vector-pen fs-6"></i> {{ state.row.regdate }}</span><br />
                    
                    <div v-if="state.row.userid == state.loginUserId" scope="col" style="width: 100%;">
                        <el-button link type="success" style="font-size: 17px; font-weight: 600; margin-right: 30px;"
                            @click="handleBoardUpdate(state.bno)">
                            수정
                        </el-button>
                        <el-button link type="danger" style="font-size: 17px; font-weight: 600;"
                            @click="handleBoardDelete()">
                            삭제
                        </el-button>
                    </div>
                    <div class="butt" style="margin-top: 30px; width: 100%;">
                        <el-button link style="font-size: 17px; font-weight: 600; color: black; margin-right: 20px;" @click="handleBoardPage()">목록으로</el-button>
                        <el-button link @click="handleNext()"><i class="bi bi-chevron-double-left fs-3" style="color:mediumblue;"></i></el-button>
                        <el-button link :color="state.likecolor" @click="handleLikePost()"><i class="bi bi-suit-heart fs-3" style="color:crimson;"></i></el-button>
                        <el-button link @click="handlePrev()"><i class="bi bi-chevron-double-right fs-3" style="color:mediumblue;"></i></el-button>
                    </div>
                </div>

                <div class="col-5" style=" margin-top: 30px;">
                    <el-input type="text" style="width:80%; margin-right: 10px;" v-model="state.replyContent" placeholder="댓글을 남겨보세요"/>
                    <el-button link @click="handleInsertReply()"><i class="bi bi-pencil-square fs-3"></i></el-button>
                    <div style="margin-top: 10px; text-align: left;">
                        <i class="bi bi-person-lines-fill fs-3"></i>
                        <table class="table" style="width:100%;">
                            <thead>
                                <tr style="text-align: center;">
                                    <th scope="col" style="width: 120px;"></th>
                                    <th scope="col"></th>
                                    <th scope="col" style="width: 120px;"></th>
                                    <th scope="col"></th>
                                </tr>
                            </thead>
                            <tbody v-for="(obj) of state.result" :key="obj">
                                <td style="font-weight: 600;">{{ obj.nickname}}<span style="font-weight: 500;">님</span></td>
                                <td>"{{ obj.content }}"</td>
                                <td>{{ obj.regdate }}</td>
                                <!-- 사용자 본인의 댓글인 경우 삭제/수정버튼 보임 -->
                                <td v-if="obj.userid == state.loginUserId" scope="col" style="width: 20%;">
                                    <el-button link type="success" plain style="font-size: 13px;"
                                        @click="handleReplyClick(obj.rno)"  
                                        data-toggle="modal" data-target="#myModal">
                                        수정
                                    </el-button>
                                    <el-button link type="danger" plain style="font-size: 13px;"
                                        @click="handleReplyDelete(obj.rno)">
                                        삭제
                                    </el-button>
                                </td>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <!-- 댓글 수정을 위한 모달창 생성 -->
            <div class="modal hide" id="myModal" >
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h4 class="modal-title">댓글 수정하기</h4>
                            <button type="button" class="close" @click="handlemodalclose()" data-dismiss="modal">×</button>
                        </div>

                        <div class="modal-body">
                            <p>댓글 수정 후 수정버튼을 눌러주세요</p>
                            <el-input v-model="state.replyUpdate" style="width:60%"/>
                        </div>

                        <div class="modal-footer">
                            <button type="button" class="btn btn-default" @click="handleReplyUpdate()" >수정</button>
                            <button type="button" class="btn btn-default" @click="handlemodalclose()" data-dismiss="modal">취소</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
<script>
import { reactive } from '@vue/reactivity'
import { useRoute, useRouter } from 'vue-router'
import { onMounted } from '@vue/runtime-core';
import axios from 'axios';
export default {
    setup() {
        const route = useRoute();
        const router = useRouter();

        const state = reactive({
            bno         : Number(route.query.bno),
            rno         : '', //댓글번호
            row         : '', //상세 내용
            Prevno      : 0, //이전글번호
            Nextno      : 0, //다음글번호
            hashtag     : [], //해시태그 배열
            replypage   : 1, //댓글 페이지네이션
            result      : '',//댓글 담기
            token       : sessionStorage.getItem("token"),
            likestatus  : '', // 좋아요 상태 확인 =>  A = 좋아요 등록됨 , B = 좋아요 해제됨
            replyContent: '', // 댓글 등록을 위한 변수
            replyUpdate : '', // 댓글 수정을 위한 변수
            loginUserId : '', //로그인한 사용자 아이디
            selectReply : null, //댓글수정을 위한 1개 조회
            likecolor   : '',
            lcnt        : '',
        });

        const handleData = async () => {
            // 1. 게시글 1개 정보 가져오기
            const url = `/fligent/api/board/boardselectone.json?bno=${state.bno}`;
            const headers = { "Content-Type": "application/json" };
            const { data } = await axios.get(url, { headers });
            
            if (data.status === 200) {
                state.row = data.result;
                state.Prevno = data.Prevno;
                state.Nextno = data.Nextno;
                state.hashtag = data.hashtag;
                if(data.result.lcnt == null){
                    state.lcnt = 0;
                }
                else{
                    state.lcnt = data.result.lcnt;
                }

                // 로그인한 사용자 정보 조회
                handlecompareId();
            }
        };
        
        // 2. 댓글 가져오기
        const handleData1 = async () => {
            const url1 = `/fligent/api/board/boardselectonereply.json?bno=${state.bno}&page=${state.replypage}`;
            const headers1 = { "Content-Type": "application/json" };
            const response = await axios.get(url1, { headers1 });
            if (response.status == 200) {
                state.result = response.data.result;
            }
        };

        // 좋아요 버튼클릭시 좋아요수 1 증가/해제
        const handleLikePost = async () => {
            if(state.token != null){
            const url = `/fligent/api/board/postlike.json`;
            const headers = {
                "Content-Type": "application/json",
                "token": state.token
            };
            const body = { board: { bno: state.bno } }
            const { data } = await axios.post(url, body, { headers });
            // console.log('좋아요 버튼 클릭 결과 => ',data.likestatus)
            // A이면 좋아요 등록 B이면 좋아요 해제
            if (data.status == 200) {
                if (data.likestatus == 'A') {
                    alert('좋아요 등록됨')
                    state.likecolor = '#ffc0cb';
                } else {
                    alert('좋아요 해제됨')
                    state.likecolor = '';
                }
                handleData();
            } else {
                alert('오류발생\n다시 시도해주세요')
            }
            }
            else {
                alert('로그인 후 이용가능합니다')
            }
        };

        // 로그인한 사용자 정보 조회
        const handlecompareId = async () => {
            if (state.token == null) {
                state.loginUserId = null;
            } else {
                const url = `/fligent/api/board/compareid.json`;
                const headers = {
                    "Content-Type": "application/json",
                    "token": state.token
                };
                const { data } = await axios.get(url, { headers });
                state.loginUserId = data.userid;
            }
        };

        // 댓글 등록하기
        const handleInsertReply = async () => {
            if(state.token == null){
                alert('로그인 후 작성 가능합니다!')
                if(confirm('로그인 페이지로 이동하시겠습니까?')){
                    router.push({path:'/memberlogin'});
                }
                return;
            } 
            else {
                const url = `/fligent/api/board/insertreply.json`;
                const headers = {
                    "Content-Type": "application/json",
                    "token": state.token
                }
                const body = {
                    content: state.replyContent,
                    board: { bno: state.bno }
                }
                const { data } = await axios.post(url, body, { headers });
                // console.log('댓글 등록 결과 => ' , data)
                if (data.status == 200) {
                    alert('댓글이 등록되었습니다');
                    handleData1();
                }
            }
        }

        // 게시글 수정
        const handleBoardUpdate = (bno) => {
            router.push({path:'boardupdate', query:{bno:bno}});
        };

        // 게시글 삭제
        const handleBoardDelete = async() => {
            if(confirm('삭제하시겠습니까?')){
                const url =`/fligent/api/board/deleteoneboard.json?bno=${state.bno}`;
                const headers = { "Content-Type": "application/json", "token": state.token };
                const body = {};
                const { data } = await axios.post(url, body, { headers });
                if (data.status === 200) {
                    router.push({path:'/board'});
                    alert('삭제되었습니다.')
                }
            }
        };

        // 댓글 수정 모달창(기존정보 불러오기)
        const handleReplyClick = async (rno) => {
            state.rno = rno;

            const url = `/fligent/api/board/selectreply.json?rno=${state.rno}`;
            const headers = { "Content-Type": "application/json" };
            const { data } = await axios.get(url, { headers });

            if (data.status == 200) {
                let modal = document.getElementById("myModal");
                state.replyUpdate = data.result.content
                modal.style.display = 'block';
            }

        };

        // 댓글 수정
        const handleReplyUpdate = async () => {
            const url = `/fligent/api/board/updatereply.json`;
            const headers = {
                "Content-Type":"application/json",
                "TOKEN" : state.token
            }
            const body = { 
                content  : state.replyUpdate,
                board    : { bno: state.bno },
                rno   : state.rno
            }
            const {data} = await axios.post(url, body, {headers});
            if(data.status == 200){
                alert('댓글 수정이 완료되었습니다!')
                let modal = document.getElementById("myModal");
                modal.style.display = 'none';
                handleData1();
            }
        };

        // 댓글 삭제
        const handleReplyDelete = async(rno) => {
            if(confirm('삭제하시겠습니까?')){
                const url =`/fligent/api/board/deletereply.json?rno=${rno}`;
                const headers = { "Content-Type": "application/json", "token": state.token };
                const body = {};
                const { data } = await axios.post(url, body, { headers });
                if (data.status === 200) {
                    handleData1();
                    alert('삭제되었습니다.')
                }
            }
        };

        const handlemodalclose = () => {
            let modal = document.getElementById("myModal")
            modal.style.display = 'none'
        };

        // 이전글
        const handlePrev = async () => {
            if(state.Prevno == 0){
                alert('마지막 페이지 입니다.');
            }
            else{
                router.push({ path: "/boardselectone", query: { bno: state.Prevno } });
            }
            state.bno = state.Prevno
            handleData();
            handleData1();
        };

        // 다음글
        const handleNext = async () => {
            if(state.Nextno == 0){
                alert('마지막 페이지 입니다.');
            }
            else{
                router.push({ path: "/boardselectone", query: { bno: state.Nextno } });
            }

            state.bno = state.Nextno
            handleData();
            handleData1();
        };

        // 게시글 목록으로 이동
        const handleBoardPage = () => {
            router.push({path:'/board'});
        }

        onMounted(() => {
            handleData();
            handleData1();
        });

        return {
            state,
            handleLikePost,
            handleInsertReply,
            handlePrev,
            handleNext,
            handlecompareId,
            handleReplyClick,
            handleReplyUpdate,
            handleBoardPage,
            handleBoardUpdate,
            handleBoardDelete,
            handlemodalclose,
            handleReplyDelete
        }
    }
}
</script>

<style lang="css" scoped>
.container {
    text-align: center;
    width: 100%;
    padding: 50px;
    /* border:1px solid #cccccc; */
}
.item{
    margin: auto;
    /* border:1px solid #cccccc; */
}
.butt button:hover {
    opacity: 1.0;
	animation: post-ani 0.8s linear;
}
@keyframes post-ani {
  25% {
    transform: scale(1.05);
  }
}
</style>