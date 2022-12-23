<template>
  <div>
    <div class="container">
      <div class="header">
        <video muted autoplay>
          <source src="../assets/air2.mp4" type="video/mp4">
        </video>

        <div class="text">
          <p class="animate__animated animate__fadeInDown">Fligent</p>
        </div>

        <div class="textt">
          <p class="animate__animated animate__fadeInUp">당신을 위한 맞춤 공항정보 |플라이전트|</p>
        </div>
      </div>
      <div class="boarding">
        <div class="row">
          <div class="col-md-8" style="height: 30%;">
            <select class="form-select" v-model="state.cate" 
                style="width: 180px; margin-left: 20px; font-size: 20px; font-weight: 600; height: 40px;" 
                @change="onChange($event)">
              <option value="GMP" label="김포공항"></option>
              <option value="ICN" label="인천공항"></option>
              <option value="PUS" label="김해공항"></option>
              <option value="CJU" label="제주공항"></option>
            </select>
            <h2 style="margin-top: 100px; margin-left: 100px; text-align: center;">탑승수속 예상 소요시간 <i class="bi bi-hourglass-split"></i></h2>
            <h6 style="text-align: center; margin-left: 100px; color: #cccccc;">*인천공항과 김해공항에서는 제공되지 않는 정보입니다.</h6>
          </div>

          <div class="col-md-4">
            <div class="card" style="width: 65%; border: 1px solid #ffffff; background-color: #F2E9E9;">
              <div class="card-body">
                <h3 class="card-title">오늘의 날씨<i class="bi bi-cloud-sun"></i></h3>
              </div>
                <h6 class="card-subtitle mb-2 text-muted">현재 공항</h6>
                <table class="table table-borderless" style="text-align: center;">
                  <thead style="font-size: 18px;">
                    <tr>
                      <th scope="col">날씨</th>
                      <th scope="col">기온</th>
                      <th scope="col">비소식</th>
                      <th scope="col">습도</th>
                    </tr>
                  </thead>
                  <tbody style="font-size: 16px; font-weight: 500;">
                    <tr v-for="obj of state.weather" :key="obj">
                      <td>{{obj.skyvalue}}</td>
                      <td>{{obj.tmpvalue}}℃</td>
                      <td>{{obj.ptyvalue}}</td>
                      <td>{{obj.rehvalue}}%</td>
                    </tr>
                  </tbody>
                </table>
            </div> 
          </div>
        </div>

          <div class="getwait">
            <table class="table" style="width:95%; margin-left: 30px;">
              <thead>
                <tr style="font-size: 17px;">
                  <th scope="col">조회 시간</th>
                  <th scope="col">자동발권/수하물위탁</th>
                  <img src="../assets/free-animated-icon-fast-forward-7740748.gif" style="width:40px; margin-top: 40px;" />
                  <th scope="col">신분확인</th>
                  <img src="../assets/free-animated-icon-fast-forward-7740748.gif" style="width:40px; margin-top: 40px;" />
                  <th scope="col">보안검색</th>
                  <img src="../assets/free-animated-icon-fast-forward-7740748.gif" style="width:40px; margin-top: 40px;" />
                  <th scope="col">탑승 ➡ 출발</th>
                  <img src="../assets/free-animated-icon-airplane-8112715.gif" style="width:70px; margin-top: 10px;" />
                  <th scope="col">일반발권 ➡ 출발</th>
                </tr>
              </thead>
              <tbody v-if="state.getwaittimedata != null" class="table-group-divider" style="text-align: center;">
                <tr style="font-size: 18px; background-color: #ffffff;">
                  <td>{{state.getwaittimedata.prc_HR}}</td>
                  <td style="width:170px;">{{state.getwaittimedata.sty_TCT_AVG_A}}분</td>
                  <td style="width:5px;"></td>
                  <td>{{state.getwaittimedata.sty_TCT_AVG_B}}분</td>
                  <td style="width:5px;"></td>
                  <td>{{state.getwaittimedata.sty_TCT_AVG_C}}분</td>
                  <td style="width:5px;"></td>
                  <td>{{state.getwaittimedata.sty_TCT_AVG_D}}분</td>
                  <td style="width:5px;"></td>
                  <td>{{state.getwaittimedata.sty_TCT_AVG_ALL}}분</td>
                </tr>
              </tbody>
            </table>
          </div>

          <div class="img">
            <el-carousel direction="vertical" :interval="3000">
              <el-carousel-item>
                <img src="../assets/1.jpg" />
              </el-carousel-item>
              <el-carousel-item>
                <img src="../assets/2.jpg" />
              </el-carousel-item>
              <el-carousel-item>
                <img src="../assets/3.jpg" />
              </el-carousel-item>
              <el-carousel-item>
                <img src="../assets/4.jpg" />
              </el-carousel-item>
            </el-carousel>
          </div>
        
      </div>

      <div class="info">
        <div class="row" style="margin-top: 50px;">
          <div class="col-md-6">
            <h2>현재 공항 주변 교통 혼잡도</h2>
            <div class="map" style="height: 60%;">
              <div id="map" style="margin-top: 30px; border-radius: 25px; border: 1px solid; width:80%; height: 590px; margin-left:100px; margin-bottom: 100px;"></div>
            </div>
          </div>

          <div class="col-md-6" style="width: 45%;">
            <h2>현재 공항 운항 일정</h2>
            <h6 style="text-align: center; color: #cccccc;">*인천공항에서는 제공되지 않는 정보입니다.</h6>
            <select class="form-select" v-model="state.type"
              style="float: right; width: 150px; margin-top: 10px; height: 40px;" 
              @change="onChangeType($event)">
              <option value="I" label="도착정보"></option>
              <option value="O" label="출발정보"></option>
            </select>
            <select class="form-select"
                  style="float: left; width: 200px; margin-top: 10px; margin-right: 10px; margin-bottom: 10px; height: 40px;" 
                  @change="onChangeTime($event)">
              <option value="0000~2400" label="시간대를 선택해주세요"></option>
              <option value="0000~0600" label="00:00~06:00"></option>
              <option value="0600~0800" label="06:00~08:00"></option>
              <option value="0800~1000" label="08:00~10:00"></option>
              <option value="1000~1200" label="10:00~12:00"></option>
              <option value="1200~1400" label="12:00~14:00"></option>
              <option value="1400~1600" label="14:00~16:00"></option>
              <option value="1600~1800" label="16:00~18:00"></option>
              <option value="1800~2000" label="18:00~20:00"></option>
              <option value="2000~2200" label="20:00~22:00"></option>
              <option value="2200~2400" label="22:00~24:00"></option>
            </select>
            
            <table class="table" style="width:100%; text-align: center; font-size: 15px; margin-bottom: 5px;">
              <thead style="background-color: #F2D1C9;">
                <tr style="font-size: 18px;">
                  <th scope="col">항공편명</th>
                  <th scope="col">항공사</th>
                  <th scope="col">출발공항</th>
                  <th scope="col">도착공항</th>
                  <th scope="col">예정시간 > 변경시간</th>
                  </tr>
              </thead>
              <tbody>
                <tr v-for="obj of state.flightstatuslistdatadata" :key="obj" style="font-size: 16px;">
                  <td>{{obj.airFln}}</td>
                  <td>{{obj.airlineKorean}}</td>
                  <td>{{obj.boardingKor}}</td>
                  <td>{{obj.arrivedKor}}</td>
                  <td>{{obj.std}} > {{obj.etd}}</td>
                </tr>
              </tbody>
            </table>
            <div class ="paginateion">
              <el-button style="width: 40px; height: 30px;" v-for="tmp in state.flightstatuslistpageNo" :key="tmp" @click="flightstatuslistPage(tmp)">
                {{tmp}}
              </el-button>
            </div>
          </div>

        </div>
      </div>

      <div class="hotboard">
        <div>
          <el-button color="#0339A6" round style="margin-left: 40px; height: 40px; font-size: 20px; font-weight:600;">#조회수 많은</el-button><r />
          <el-button link class="button1" @click="handleBoard()" style="float: right; font-size: 18px; margin-top: 8px; margin-right: 40px; font-weight: 600;"> <i class="bi bi-blockquote-left fs-4"></i>더 많은 글 ></el-button>
        </div>
        <el-row>
          <div class="card" v-for="obj of state.hit" :key="obj">
            <img style="height: 170px;" :src="obj.imgurl" class="card-img-top"/>
            <div class="card-body">
              <h5 class="card-title">{{obj.title}}</h5>
            </div>
            <div class="row">
              <div class="col-7">
                <p class="card-text" style="font-size: 12px;">{{obj.regdate}}</p>
                <p class="card-text"><span style="color:#cccccc;">by. </span><span style="font-weight: 600;">{{obj.nickname}}</span></p>
              </div>
              <div class="col-5">
                <img src="../assets/free-animated-icon-view-8121254.gif" style="width: 30px;" />{{obj.hit}}
                <i class="bi bi-chat-left-dots" style="margin-left: 10px;"></i> {{obj.recnt}}
              </div>
            </div>
            <div class="bottom" style="margin-bottom: 10px; margin-right: 10px;">
                <el-button class="button" round style="float: right; font-weight: 600; color: black; border: 2px solid #cccccc;" @click="boardSelectOne(obj.bno)"> 보러가기 ></el-button>
            </div>
          </div>
        </el-row>

        <br />
        <div style="margin-top: 10px">
          <el-button color="#F2D1C9" round style="margin-left: 40px; height: 40px; font-size: 20px; font-weight:600;">#인기 많은</el-button><r />
        </div>

        <el-row>
          <div class="card" v-for="obj of state.like" :key="obj">
            <img style="height: 170px;" :src="obj.imgurl" class="card-img-top"/>
            <div class="card-body">
              <h5 class="card-title">{{obj.title}}</h5>
            </div>
            <div class="row">
              <div class="col-7">
                <p class="card-text" style="font-size: 12px;">{{obj.regdate}}</p>
                <p class="card-text"><span style="color:#cccccc;">by. </span><span style="font-weight: 600;">{{obj.nickname}}</span></p>
              </div>
              <div class="col-5">
                <img src="../assets/free-animated-icon-heart-8121302.gif" style="width: 30px;" />{{obj.lcnt}}
                <i class="bi bi-chat-left-dots" style=""></i> {{obj.recnt}}
              </div>
            </div>
            <div class="bottom" style="margin-bottom: 10px; margin-right: 10px;">
                <el-button round class="button" style="float: right; font-weight: 600; color: black; border: 2px solid #cccccc;" @click="boardSelectOne(obj.bno)"> 보러가기 ></el-button>
            </div>
          </div>
        </el-row>
      </div>

      <br />

      <div class="youtube">
        <h2 style="font-size: 40px;">오늘의 추천 영상</h2>
        <div class="row">
          <div class="col-7">
            <div class="video" style="float: left; width: 100%;">
              <div style="text-align:center; margin-top: 5%; margin-left: 7%;">
                <div v-if="state.youtubeurl">
                  <iframe style="border-radius: 25px;" width="80%" height="400px" :src="state.youtubeurl[0].youtubeUrl"
                      frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                    <div style="font-weight: 600; font-size: 35px;">
                      <br />
                      <p>오늘의 추천 vlog!</p>
                      <p style="font-weight: 500; font-size: 20px;">*자세한 정보는 위의 동영상에서</p> 
                    </div>
                </div>
                <br />
                <br />
              </div>
            </div>
          </div>
          <div class="col-5">
            <div class="video1" style="width: 90%;">
              <div style="text-align:center; margin-top: 2%; margin-right: 6%;">
                  <div v-if="state.youtubeurl">
                    <iframe style="margin-bottom: 3%; border-radius: 25px;" width="90%" height="280px" :src="state.youtubeurl[1].youtubeUrl"
                    frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                  </div>
                  <div v-if="state.youtubeurl">
                    <iframe style="border-radius: 25px;" width="90%" height="280px" :src="state.youtubeurl[2].youtubeUrl"
                    frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>
                  </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <router-view></router-view>
      <div class="footer">
        <img src="../assets/footer3.png"/>
      </div>
    </div>
  </div>
</template>
<script>
import { reactive } from '@vue/reactivity'
import axios from 'axios';
import { onMounted } from '@vue/runtime-core';
import { ref } from 'vue'
import { useRouter } from 'vue-router';
export default {
  setup () {
    const router = useRouter();
    const state = reactive({
      flightstatuslistdatadata: [],
      flightstatuslistpageNo  : 1,
      pages                   : 0,
      getwaittimedata         : [],
      getwaittimepageNo       : 1,
      weather                 : [],
      token                   : sessionStorage.getItem("token"),
      cate                    :'GMP',
      category                : ['GMP', 'ICN', 'PUS', 'CJU'],
      lat                     : 37.55897,
      lng                     : 126.80332,
      map                     : '',
      type                    : 'I',
      schStTime               : '1200',
      schEdTime               : '1400',
      selectvalue             : '',
      like                    : [],
      hit                     : [],
      youtubeurl              : '',
      position :{
        "GMP":{lat:37.55897,lng:126.80332},
        "ICN":{lat:37.45076,lng:126.47288},
        "PUS":{lat:35.16974,lng:128.95402},
        "CJU":{lat:33.50628,lng:126.49231}
      },
    });

    var now = new Date();
    var hours = now.getHours();	// 시간
      
      const handleUser = async() => {
        if(state.token == null){return;}
          const url =`/fligent/api/customer/mypage/update.json`;
          const headers = {
              "Content-Type":"application/json",
              "TOKEN" : state.token
          };
          const {data} = await axios.get(url, {headers});
          // console.log(data);
          if(data.status === 200) {
              state.cate = data.result.airportname;
          }
      };
      
      const handleData = async() => {
        // 한국공항공사_항공기 운항정보
        const url = `/fligent/api/flightstatuslist.json?pageNo=${state.flightstatuslistpageNo}&schAirCode=${state.cate}&type=${state.type}&schStTime=${state.schStTime}&schEdTime=${state.schEdTime}`;
        const headers = {"Content-Type" : "application/json"};
        const {data} = await axios.get(url, {headers});
        // console.log('항공기 운항정보 data =====> ', data);
        if(data.totalCount != 0){
        state.flightstatuslistdatadata = data.items.item;
        for(const time of state.flightstatuslistdatadata){
          let bbb = ""; let ccc = "";
          if((time.std%100).toString().length == 1){
            bbb = "0"+time.std%100;
          }
          else{
            bbb = time.std%100;
          }
          if((time.etd%100).toString().length == 1){
            ccc = "0"+time.etd%100;
          }
          else{
            ccc = time.etd%100;
          }
          time.std = Math.floor(time.std/100)+":"+bbb;
          time.etd = Math.floor(time.etd/100)+":"+ccc;
        }
        state.flightstatuslistpageNo = parseInt(data.totalCount/10+1);
      } 
      else { // 데이터가 없는경우
        state.flightstatuslistdatadata = [];
      }

        // 한국공항공사_공항 소요시간 정보
        const url1 = `/fligent/api/getwaittime.json?cate=${state.cate}`;
        const headers1 = {"Content-Type" : "application/json"};
        const response = await axios.get(url1, {headers1});
        if(response.data != null){
          state.getwaittimedata = response.data;
        } 
      };

      // 유튜브 url
      const youtubeUrlDATA = async() => {
        const url = `/fligent/api/airportyoutube/selectyoutubeurl.json?airportCode=${state.cate}`;
        const headers = {"Content-Type" : "application/json"};
        const {data} = await axios.get(url,{headers});
        if(data.list !=null){
          
          state.youtubeurl = data.list;
        }
      }

      //기상예보
      const wetherDATA = async() => {
      const url = `/fligent/api/weatherinfo.json?schAirCode=${state.cate}`;
      const headers = {"Content-Type" : "application/json"};
      const data = await axios.get(url, {headers});
        if(data.data !=null){
          state.weather = data.data;
        }
      };

      //게시글 조회순 4개
      const handleBoardHit = async() => {
        const url = `/fligent/api/board/boardHitselect.json`;
        const headers = {"Content-Type" : "application/json"};
        const { data } = await axios.get(url, {headers});
          if(data.status === 200){
            state.hit = data.result
          }
      };
      //게시글 좋아요순 4개
      const handleBoardLike = async() => {
        const url = `/fligent/api/board/boardLikeselect.json`;
        const headers = {"Content-Type" : "application/json"};
        const { data } = await axios.get(url, {headers});
          if(data.status === 200){
            state.like = data.result
            // console.log(data.result)
          }
      };
      //해당 게시글로 이동
      const boardSelectOne = async(bno) => {
        const url = `/fligent/api/board/updatehit.json?bno=${bno}`;
        const headers = {"Content-Type":"application/json"};
        const { data } = await axios.put(url, {headers});
        
        if(data.status == 200){
            router.push({path:'boardselectone', query:{bno:bno}});
        } 
        else {
            alert('오류가 발생했습니다!다시 시도해주세요')
        }
      };

      const flightstatuslistPage = async(tmp) => {
        state.flightstatuslistpageNo = tmp;
        handleData();
      };

      const onChange = async(event) => {
        state.cate = event.target.value;
        state.flightstatuslistpageNo = 1;
        handleData();
        youtubeUrlDATA();
        wetherDATA();
        initMap();
      };
      
      const onChangeType = async(event) => {
        state.type = event.target.value;
        state.flightstatuslistpageNo = 1;
        handleData();
      };

      const onChangeTime = async(event) => {
        const selectvalue = event.target.value.split("~")
        state.schStTime = selectvalue[0];
        state.schEdTime = selectvalue[1];
        state.flightstatuslistpageNo = 1;
        handleData();
      };

      const handleBoard = () => {
            router.push({path:'/board'});
      };

      onMounted(async() => {
        await handleUser();
        handleBoardHit();
        handleBoardLike();
        if(0<=hours&&hours<6){state.schStTime = '0000'; state.schEdTime = '0600';}
        else if(6<=hours&&hours<8){state.schStTime = '0600'; state.schEdTime = '0800';}
        else if(8<=hours&&hours<10){state.schStTime = '0800'; state.schEdTime = '1000';}
        else if(10<=hours&&hours<12){state.schStTime = '1000'; state.schEdTime = '1200';}
        else if(12<=hours&&hours<14){state.schStTime = '1200'; state.schEdTime = '1400';}
        else if(14<=hours&&hours<16){state.schStTime = '1400'; state.schEdTime = '1600';}
        else if(16<=hours&&hours<18){state.schStTime = '1600'; state.schEdTime = '1800';}
        else if(18<=hours&&hours<20){state.schStTime = '1800'; state.schEdTime = '2000';}
        else if(20<=hours&&hours<22){state.schStTime = '2000'; state.schEdTime = '2200';}
        else if(22<=hours&&hours<24){state.schStTime = '2200'; state.schEdTime = '2400';}
        handleData();
        wetherDATA();
        youtubeUrlDATA();

        let script = document.createElement('script');
        script.setAttribute('src', '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=98e6a59045810a89b4b63df18610ec36&libraries=clusterer,drawing,services')
        document.head.appendChild(script)
        script.onload = () => window.kakao.maps.load( initMap );
        // console.log(window);
      });

      const initMap = () => {
        const container = document.getElementById("map");
        const position = new window.kakao.maps.LatLng(state.position[state.cate].lat, state.position[state.cate].lng);
        const options = {
            center: position,
            level: 6,
        };
        
        state.map = new window.kakao.maps.Map(container, options);
        state.map.addOverlayMapTypeId(window.kakao.maps.MapTypeId.TRAFFIC)
        //kakao오류나면 window 붙여주기
        
        new window.kakao.maps.Marker({
          map: state.map,
          position:position,
        });
      };

      return {
        state, 
        ref,
        flightstatuslistPage, 
        onChange,
        onChangeType,
        onChangeTime,
        handleBoard,
        boardSelectOne,
      }
    }
}
</script>

<style lang="css" scoped>
.container{
  /* border:3px solid #000000; */
  max-width:100%;
  height: 100%;
  margin:0; 
  padding:0;
  overflow-x: hidden;
  /* overflow-y: auto; */
}
* {
  margin: 0;
  padding: 0;
}

.boarding {
  margin-top: 30px;
  margin-bottom: 50px;
  height: 100vh;
  width:100vw;
  /* border:5px solid #cccccc; */
}
.getwait{
  /* border:1px solid #cccccc; */
  height: 20vh;
}
.img{
  /* border: 5px solid #cccccc; */
  height: 50vh;
}
.img img{
  margin-top: 50px;
  margin-left: 2%;
  object-fit: fill;
  width: 95vw;
  height: 100%;
  opacity: 0.85;
}
.info{
  border: 1px solid #ffffff;
  width: 100vw;
  height: 100vh;
  background-color: #f3eded;
}
.header {
  width: 100%;
  height: 90vh;
  overflow: hidden;
  margin: 0px auto;
  position: relative;
  /* border:2px solid #cccccc; */
}
.header video {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.text {
  position: absolute;
  width: 100%;
  top: 60%;
  left: 50%;
  font-family: 'MapoFlowerIsland';
  transform: translate(-50%,-50%);
}
.text p {
  text-align: center;
  font-size: 160px;
  margin-bottom: 120px;
  color: #ffffff;
  font-weight: 600;
}
.textt {
  position: absolute;
  font-family: 'MapoFlowerIsland';
  width: 100%;
  top: 60%;
  left: 50%;
  transform: translate(-50%,-50%);
}
.textt p {
  margin-top: 185px;
  margin-bottom: 80px;
  text-align: center;
  font-size: 25px;
  color: #ffffff;
}
.texttt{
  font-family: 'S-CoreDream-3Light';
}
.bi-cloud-sun{
  font-size: 50px;
  line-height: 22px;
  margin-left: 10px;
  color: #032859;
}
.card{
  width: 270px;
  margin: 40px;
  font-weight: 600;
  /* border:1px solid #ffffff;  */
}
th, td {
  border-bottom: 1px solid #444444;
  padding: 10px;
  text-align: center;
}
tbody tr:nth-child(2n+1) {
  background-color: #f7e9e9;
}

.container h2{
  font-weight: 600; 
  font-family: 'MapoFlowerIsland'; 
  text-align: center; 
  margin-bottom: 10px;
  margin-top: 10px;
}

.card-title{
  font-weight: 600;
  text-align: center;
  margin-top: 15px;
  margin-bottom: 25px;
  /* font-size: 27px; */
  font-family: 'MapoFlowerIsland';
}
.card-subtitle{
  text-align: center;
}
.button1:hover{
  opacity: 1.5;
	animation: post-ani 0.8s linear;
}
.card:hover {
	opacity: 1.0;
	animation: post-ani 0.8s linear;
}
@keyframes post-ani {
  25% {
    transform: scale(1.03);
  }
}
.card-text{
  font-weight: 500;
  margin-left: 20px;
}
*{
      transition-duration: 0.8s;
    }
.paginateion{
  /* margin: auto; */
  margin-top: 50px;
  text-align: center;
  /* border:1px solid #cccccc; */
}
.text-box h4 {
  font-size: 1.8em;
  text-align: center;
  color: #171716;
  text-shadow:-1px 0px #DCE5EE,1px 0px #DCE5EE,0px -1px #DCE5EE,0px 1px #DCE5EE;
  color:#032859;
  margin: 0;
  margin-top: 50px; 
  font-weight: 600; 
  font-family: 'MapoFlowerIsland';
}
.hotboard {
  width: 85vw;
  /* height: 100vh; */
  padding: 10px;
  margin: 100px auto;
  /* border:1px solid #cccccc; */
}
.youtube { 
  margin-top: 100px; 
  grid-area: youtube; 
  /* border:1px solid #cccccc; */
  width: 100vw;
}
/* .map{
  border:1px solid #cccccc;
} */
.footer { 
  grid-area: footer; 
  /* border:1px solid #cccccc; */
  width: 100vw;
}
.footer img {
  object-fit: fill;
  /* border:5px solid #cccccc; */
  width: 100%;
  height: 250px;
}
@media(max-width:664px) {
  .header {
    height: auto;
  }
  .video {
    height: auto;
  }
  .text p {
    font-size: 24px;
  }
  .textt p {
    font-size: 24px;
  }
}
/* @font-face {
  font-family: 'IBMPlexSansKR-Regular';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_20-07@1.0/IBMPlexSansKR-Regular.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

@font-face {
  font-family: 'MapoFlowerIsland';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2001@1.1/MapoFlowerIslandA.woff') format('woff');
  font-weight: normal;
  font-style: normal;
} */
</style>