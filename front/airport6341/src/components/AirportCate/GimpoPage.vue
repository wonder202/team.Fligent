<template>
  <div class="container">
    <div class="header">
      <div class="tracking-in-expand">
        <h1>김 포 공 항</h1>
      </div>
    </div>
    <div class="traffic">
      <div class="row">
        <div class="col-6">
          <h2>현재 공항 주변 교통 혼잡도</h2>
          <div id="map" style="border-radius: 25px; border: 2px solid; width:600px; height: 500px; margin-left: 100px;"></div>
        </div>
        <div class="col-6">
          <div class="text-box">
            <h2>공항 주차장 혼잡도</h2>
          </div>
          <el-descriptions direction="vertical" v-for="obj of state.parkinginfodata" :key="obj" border="1" style="width: 85%; margin: auto; height: 85px; margin-right: 100px;" >
              <el-descriptions-item label="주차장">{{obj.parkingAirportCodeName}}</el-descriptions-item>
              <el-descriptions-item label="전체 주차면 수" >{{obj.parkingFullSpace}}</el-descriptions-item>
              <el-descriptions-item label="입고 수/출고 수" >{{obj.parkingIincnt}}/{{obj.parkingIoutcnt}}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div> 
    </div>
    <div class="bording">
      <div class="flightlist">
        <h2>현재 공항 운항 일정</h2>
        <select class="form-select" v-model="state.type"
            style="float: right; width: 150px; margin-top: 10px; font-size: 17px; margin-bottom: 10px;" 
            @change="onChangeType($event)">
          <option value="I">도착정보</option>
          <option value="O">출발정보</option>
        </select>
        <select class="form-select"
            style="float: left; width: 220px; margin-right: 10px; font-size: 17px; margin-top: 10px;" 
            @change="onChangeTime($event)">
          <option value="0000~2400">시간대를 선택해주세요</option>
          <option value="0000~0600">00:00~06:00</option>
          <option value="0600~0800">06:00~08:00</option>
          <option value="0800~1000">08:00~10:00</option>
          <option value="1000~1200">10:00~12:00</option>
          <option value="1200~1400">12:00~14:00</option>
          <option value="1400~1600">14:00~16:00</option>
          <option value="1600~1800">16:00~18:00</option>
          <option value="1800~2000">18:00~20:00</option>
          <option value="2000~2200">20:00~22:00</option>
          <option value="2200~2400">22:00~24:00</option>
        </select>
        <table class="table table-hover" style="width:100%; text-align: center; font-size: 15px;">
          <thead style="background-color: #F2D1C9;">
            <tr style="font-size: 20px;">
              <th scope="col">운항날짜</th>
              <th scope="col">항공편명</th>
              <th scope="col">항공사</th>
              <th scope="col">출발공항</th>
              <th scope="col">도착공항</th>
              <th scope="col">예정시간</th>
              <th scope="col">변경시간</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="obj of state.flightstatuslistdatadata" :key="obj" style="font-size: 17px;">
              <td>{{obj.flightDate}}</td>
              <td>{{obj.airFln}}</td>
              <td>{{obj.airlineKorean}}</td>
              <td>{{obj.boardingKor}}</td>
              <td>{{obj.arrivedKor}}</td>
              <td>{{obj.std}}</td> 
              <td>{{obj.etd}}</td>
            </tr>
          </tbody>
        </table>
        <ul class ="paginateion" style="float: right;">
          <el-button v-for="tmp in state.flightstatuslistpageNo" :key="tmp" @click="flightstatuslistPage(tmp)">
              {{tmp}}
          </el-button>
        </ul>
      </div>

      <div class="img">
        <el-carousel direction="vertical">
          <el-carousel-item>
            <img src="../../../src/assets/1.jpg" />
          </el-carousel-item>
        </el-carousel>
      </div>
      <div class="waittime">
        <h2 style="margin-top: 50px;">탑승수속 예상 소요시간 <i class="bi bi-hourglass-split"></i></h2>
        <table class="table" style="width:100%;" >
          <thead>
            <tr style="font-size: 20px;">
              <!-- <th scope="col">공항</th> -->
              <th scope="col">업데이트</th>
              <th scope="col">자동발권 - 수하물위탁</th>
              <th><img src="../../../src/assets/free-animated-icon-fast-forward-7740748.gif" style="width:30px; margin-top: 40px;" /></th>
              <th scope="col">신분확인</th>
              <th><img src="../../../src/assets/free-animated-icon-fast-forward-7740748.gif" style="width:30px; margin-top: 40px;" /></th>
              <th scope="col">보안검색</th>
              <th><img src="../../../src/assets/free-animated-icon-fast-forward-7740748.gif" style="width:30px; margin-top: 40px;" /></th>
              <th scope="col">탑승 -> 출발</th>
              <th><img src="../../../src/assets/free-animated-icon-airplane-8112715.gif" style="width:60px; margin-top: 40px;" /></th>
              <th scope="col">일반발권 -> 출발</th>
            </tr>
          </thead>
          <tbody class="table-group-divider" style="text-align: center;">
            <tr style="font-size: 17px; background-color: #ffffff;">
              <!-- <td>{{state.getwaittimedata.iata_APCD}}</td> -->
              <td>{{state.getwaittimedata.prc_HR}}</td>
              <td style="width:210px;">{{state.getwaittimedata.sty_TCT_AVG_A}}분</td>
              <td style="width:10px;"></td>
              <td style="width:200px;">{{state.getwaittimedata.sty_TCT_AVG_B}}분</td>
              <td style="width:10px;"></td>
              <td>{{state.getwaittimedata.sty_TCT_AVG_C}}분</td>
              <td style="width:10px;"></td>
              <td>{{state.getwaittimedata.sty_TCT_AVG_D}}분</td>
              <td style="width:10px;"></td>
              <td>{{state.getwaittimedata.sty_TCT_AVG_ALL}}분</td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <div class="container1">
        <div class="item">
          <div class="row">
            <div class="col-8" style="margin-left: 50px;">
              <h2><i class="bi bi-shop"></i> 공항 내 편의시설</h2>
              <select class="form-select" v-model="state.typeofbusiness" 
                  style="width: 150px; margin-left: 80%; font-size: 17px;"
                  @change="onChange($event)">
                <option selected value="식음료업">식음료업</option>
                <option value="면세점업">면세점업</option>
                <option value="판매업">판매업</option>
                <option value="기타서비스업">기타서비스업</option>
                <option value="안내업">안내업</option>
                <!-- <option value="금융업">금융업</option> -->
              </select>

              <div class="itemcard">
                <div class="card" style="width: 30%; border:1px solid #ffffff; margin:10px; float: left;" v-for="obj of state.list" :key="obj">
                  <img style="height:20%; border: 1px solid #cccccc;" :src="obj.imageurl" class="card-img-top"/>
                  <div class="card-body">
                    <h5 class="card-title">{{obj.storeName}}</h5><hr />
                    <p class="card-text"><i class="bi bi-geo-alt"></i> {{obj.leaseLocation}}</p>
                    <p class="card-text"><i class="bi bi-shop"></i> {{obj.typeOfBusiness}}</p>
                  </div>
                </div>
              </div>
            </div>

            <div class="col" style="margin-right: 20px;">
              <i class="bi bi-clock-history" style="font-size: 100px;"></i>
              <h2>탑승시간이 많이 남았다면,</h2>
              <h2>어떠세요?</h2>
              <!-- {{state.hashtag -->
              <div class="hash">
                <el-button v-for="(obj) in state.hashtag.slice(4, 12)" :key="obj" :color="obj.hcolor" @click="handleSearch(obj.hno)"
                    style="font-size: 17px; font-weight: 600; margin: 10px;">#{{obj.name}}</el-button>
              </div>
              <h5 style="color:#cccccc; font-weight: 600;">*누르면 게시판으로 이동합니다.</h5>
            </div>
          </div>
        </div>
        <div class="page">
          <el-button v-for="tmp in state.pages" :key="tmp" @click="airstorePages(tmp)">
              {{tmp}}
          </el-button>
        </div>
      </div>
    </div>
    <div class="footer">
      <img src="../../assets/footer3.png"/>
    </div>
  </div>
</template>

<script>
import { reactive } from '@vue/reactivity'
import axios from 'axios';
import { onMounted } from '@vue/runtime-core';
import { useRouter } from 'vue-router';
export default {
    setup () {
      const marginMap = {
          default: '28px',
      }
      const router = useRouter();
      const state = reactive({
        hashtag       : [],
        flightstatuslistdatadata:[],
        flightstatuslistpageNo  :1,
        getwaittimedata         :[],
        parkinginfodata         :[],
        cate          :'GMP',
        lat           :37.55897,
        lng           :126.80332,
        map           : '',
        type          : 'I',
        schStTime     : '1200',
        schEdTime     : '1400',
        selectvalue   : '',
        list          : [],
        // hno           : '',
        page          : 1,
        pages         : 0,
        total         : 0,
        typeofbusiness: '식음료업'
      });

      var now = new Date();
      var hours = now.getHours();	// 시간

      // 한국공항공사_항공기 운항정보
      const handleData = async() => {
        const url = `/fligent/api/flightstatuslist.json?pageNo=${state.flightstatuslistpageNo}&schAirCode=${state.cate}&type=${state.type}&schStTime=${state.schStTime}&schEdTime=${state.schEdTime}`;
        const headers = {"Content-Type" : "application/json"};
        const {data} = await axios.get(url, {headers});
        // console.log(data);
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

        // 한국공항공사_공항 소요시간 정보
        const url1 = `/fligent/api/getwaittime.json?cate=${state.cate}`;
        const headers1 = {"Content-Type" : "application/json"};
        const response = await axios.get(url1, {headers1});
        state.getwaittimedata = response.data;

        // 한국공항공사_전국공항 실시간 주차정보
        const url3 = `/fligent/api/parkinginfo.json?cate=${state.cate}`;
        const headers3 = {"Content-Type" : "application/json"};
        const response3 = await axios.get(url3, {headers3});
        state.parkinginfodata = response3.data.items.item;  
      };

      // var clonecolor = [];
      const hasgtagData = async() => {
          const url = `/fligent/api/board/hashtagdataget.json`
          const headers = {"Content-Type" : "application/json"}
          const {data} = await axios.get(url, headers)
          if(data.status == 200){
              state.hashtag = data.list;
              // state.hno = data.list.hno;
              // clonecolor = JSON.parse(JSON.stringify(data.list));
          }
      };

      const handleSearch = async(hno) => {
        router.push({path:'/board', query: { hno : [1,hno]}});
      };

      const flightstatuslistPage = async(tmp) => {
        state.flightstatuslistpageNo = tmp;
        handleData();
      };

      const onChange = async(event) => {
        state.typeofbusiness = event.target.value;
        handleData1();
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

      const handleData1 = async() => {
          const url = `/fligent/api/airportstore/selecttypeofbussiness.json?airportCode=GMP&typeOfBusiness=${state.typeofbusiness}&start=${state.page}`;
          const headers = {"Content-Type":"application/json"};
          const {data} = await axios.get(url, {headers});
          // console.log(data);
          if(data.status === 200){
              state.list = data.catelist;
              state.total = data.count;
              state.pages = Math.floor(data.count/8+1);
          }
      };

      const airstorePages = (tmp) =>{
          state.page = tmp;
          handleData1();
      };

      onMounted(() =>{
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
        handleData1();
        hasgtagData();

        let script = document.createElement('script');
        script.setAttribute('src', '//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=98e6a59045810a89b4b63df18610ec36&libraries=clusterer,drawing,services')
        document.head.appendChild(script)
        script.onload = () => window.kakao.maps.load( initMap );
        // console.log(window);
      });

      const initMap = () => {
        const container = document.getElementById("map");
        const position = new window.kakao.maps.LatLng(state.lat, state.lng);
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
        flightstatuslistPage, 
        onChange, 
        marginMap,
        onChangeType,
        onChangeTime,
        airstorePages,
        handleSearch
      }
    }
}
</script>

<style lang="css" scoped>
.container {
  max-width: 100vw;
  height: 100%;
  text-align: center;
  overflow-x: hidden;
  /* border:1px solid #cccccc; */
}

.traffic{
  /* border:1px solid #cccccc; */
  width: 100vw;
  height: 100vh;
}
.container1 {
  width: 100vw;
  /* grid-area: container1; */
  /* border: 3px solid #cccccc; */
}
.header { 
  border:1px solid #ffffff;
  grid-area: header;
  height: 200px;
  margin-bottom: 30px;
  background-color:#f5dcdc;
  text-align: center;
  background-image: url(../../assets/gimpo.jpg);
  object-fit: fill;
}
.footer { 
  grid-area: footer;
  /* width: 100vw; */
}
.footer img {
  object-fit: fill;
  /* border:5px solid #cccccc; */
  width: 100%;
  height: 250px;
}
 .bording {
  grid-area: bording;
  /* border:5px solid #cccccc; */
  /* margin-top: 50px; */
}
.flightlist{
  /* border:5px solid #d60202; */
  width: 90vw;
  height: 100vh;
  margin: auto;
}
.img{
  /* border:5px solid #cccccc; */
  width: 95vw;
  margin: auto;
  /* height: 30vh; */
}
.img img{
  margin-top: 50px;
  object-fit: fill;
  width: 95vw;
  height: 100%;
  /* opacity: 0.85; */
}
.waittime{
  /* border:5px solid #cccccc; */
  width: 95vw;
  height: 70vh;
  margin: auto;
}
.hash{
    /* border:1px solid #cccccc; */
    width: 70%;
    margin:auto;
}
.page {
    /* border:1px solid #cccccc; */
    /* margin: auto; */
    width: 65%;
}
.card-title{
    font-weight: 600;
}
.header h1{
  margin-top: 60px;
  color: rgb(250, 241, 246);
  font-weight: 600; 
  font-family: 'MapoFlowerIsland'; 
  text-shadow: 2px 2px 2px rgb(197, 123, 123);
  font-size: 55px;
}
.el-button{
  margin-bottom: 10%; 
  font-size: 18px; 
  font-weight:600;
  margin-left: 10px;
  height: 40px;
}
.el-button:hover {
	opacity: 1.0;
	animation: post-ani 0.8s linear;
}
@keyframes post-ani {
  25% {
    transform: scale(1.03);
    }
}
.container h2{
  font-weight: 600; 
  font-family: 'MapoFlowerIsland'; 
  text-align: center; 
  margin-bottom: 20px;
}
th, td {
  border-bottom: 1px solid #444444;
  padding: 10px;
  text-align: center;
}
tbody tr:nth-child(2n+1) {
  background-color: #f7e9e9;
}
.container h3{
    font-family: 'IBMPlexSansKR-Regular';
    text-align: center; 
    margin-bottom: 20px;
}

@font-face {
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
}
.tracking-in-expand {
	-webkit-animation: tracking-in-expand 0.7s cubic-bezier(0.215, 0.610, 0.355, 1.000) both;
	animation: tracking-in-expand 0.7s cubic-bezier(0.215, 0.610, 0.355, 1.000) both;
}
@-webkit-keyframes tracking-in-expand {
  0% {
    letter-spacing: -1em;
    opacity: 0;
  }
  40% {
    opacity: 0.6;
  }
  100% {
    opacity: 1;
  }
}
@keyframes tracking-in-expand {
  0% {
    letter-spacing: -1em;
    opacity: 0;
  }
  40% {
    opacity: 0.6;
  }
  100% {
    opacity: 1;
  }
}
</style>