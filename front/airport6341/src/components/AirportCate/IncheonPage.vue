<template>
  <div class="container">
      <div class="header">
        <div class="tracking-in-expand">
          <h1>인 천 공 항</h1>
        </div>
      </div>
      <div class="traffic">
        <div class="row">
          <div class="col-6">
            <h2>현재 공항 주변 교통 혼잡도</h2>
              <div id="map" style="border-radius: 25px; border: 2px solid; width:600px; height: 500px; margin-left: 100px;"></div>
          </div>
          <div class="col-6">
            <h2>공항 주차장 혼잡도</h2>
            <el-descriptions direction="vertical" v-for="obj of state.parkinginfodata.slice(0, 6)" :key="obj" border="1" style="width: 85%; margin-top: 30px; height: 50px; margin-right: 100px;">
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
          <table style="width:100%; text-align: center; font-size: 15px;">
            <thead>
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
          </table>
          <hr />
          <h3 style="color: #cccccc;">*해당 공항에서 제공하지않는 정보입니다.</h3>
        </div>
        <div class="img">
          <el-carousel direction="vertical">
            <el-carousel-item>
              <img src="../../../src/assets/1.jpg" />
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="waittime">
          <h2 style="margin-top: 30px;">탑승수속 예상 소요시간 <i class="bi bi-hourglass-split"></i></h2>
          <table class="table" style="width:100%; margin-top: 10px;" >
            <thead>
              <tr style="font-size: 20px;">
                <th scope="col">업데이트</th>
                <th scope="col">자동발권 -> 수하물 위탁</th>
                <th scope="col">신분확인</th>
                <th scope="col">보안검색</th>
                <th scope="col">탑승 -> 출발</th>
                <th scope="col">일반발권 -> 출발</th>
              </tr>
            </thead>
          </table>
          <h3 style="color: #cccccc;">*해당 공항에서 제공하지않는 정보입니다.</h3>
        </div>
        <div class="container1">
          <div class="item">
            <div class="row">
              <div class="col-8" style="margin-left: 50px;">
                <h2><i class="bi bi-shop"></i> 공항 내 편의시설</h2>
                <select class="form-select" v-model="state.typeofbusiness" 
                    style="width: 150px; margin-left: 80%;"
                    @change="onChange($event)">
                  <option selected value="식음료업">식음료업</option>
                  <option value="면세점업">면세점업</option>
                  <option value="판매업">판매업</option>
                  <option value="기타서비스업">기타서비스업</option>
                  <option value="안내업">안내업</option>
                  <option value="금융업">금융업</option>
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
        };
        const router = useRouter();
        const state = reactive({
          hashtag  : [],
          flightstatuslistdatadata:[],
          flightstatuslistpageNo  :1,
          getwaittimedata         :[],
          parkinginfodata         :[],
          cate    :'ICN',
          lat     :37.47565,
          lng     :126.46853,
          map     : '',
          list    : [],
          page    : 1,
          pages   : 0,
          total   : 0,
          typeofbusiness : '식음료업'
        });
  
        // 한국공항공사_전국공항 실시간 주차정보
        const handleData = async() => {
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
              // clonecolor = JSON.parse(JSON.stringify(data.list));
          }
      };

      const handleSearch = async(hno) => {
        router.push({path:'/board', query: { hno : [2,hno]}});
      };
  
        const flightstatuslistPage = async(tmp) => {
          state.flightstatuslistpageNo = tmp;
          handleData();
        };
  
        const onChange = async(event) => {
          state.typeofbusiness = event.target.value;
          handleData1();
        };

        const handleData1 = async() => {
          const url = `/fligent/api/airportstore/selecttypeofbussiness.json?airportCode=ICN&typeOfBusiness=${state.typeofbusiness}&start=${state.page}`;
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
          // console.log('initMap')
          const container = document.getElementById("map");
          const position = new window.kakao.maps.LatLng(state.lat, state.lng);
          const options = {
              center: position,
              level: 7,
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
          airstorePages,
          handleSearch
        }
      }
  }
</script>
  
<style lang="css" scoped>
.container {
  text-align: center;
  max-width: 100vw;
  height: 100%;
  overflow-x: hidden;
  /* border:1px solid #cccccc; */
}
.container1 {
  width: 100vw;
  margin-top: 230px;
  /* border:1px solid #cccccc; */
}
.traffic{
  /* border:1px solid #cccccc; */
  width: 100vw;
  height: 100vh;
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
    /* border:1px solid #cccccc; */
}
.page {
  /* border:1px solid #cccccc; */
  /* margin: auto; */
  width: 65%;
}
.flightlist{
  /* border:5px solid #d60202; */
  width: 90vw;
  /* height: 40vh; */
  margin: auto;
}
.img{
  /* border:5px solid #cccccc; */
  width: 95vw;
  margin: auto;
  /* height: 30vh; */
}
.img img{
  margin-top: 20px;
  object-fit: fill;
  width: 95vw;
  height: 100%;
  /* opacity: 0.85; */
}
.waittime{
  /* border:5px solid #cccccc; */
  width: 95vw;
  /* height: 40vh; */
  margin: auto;
}
.hash{
  /* border:1px solid #cccccc; */
  width: 70%;
  margin:auto;
}
.card-title{
  font-weight: 600;
}
.header h1{
  margin-top: 60px;
  color: rgb(250, 241, 246);
  font-weight: 600; 
  font-family: 'MapoFlowerIsland'; 
  text-shadow: 2px 2px 2px rgb(197, 121, 121);
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
.container h3{
  font-family: 'IBMPlexSansKR-Regular';
  text-align: center; 
  margin-bottom: 20px;
}
.tracking-in-expand {
    -webkit-animation: tracking-in-expand 0.7s cubic-bezier(0.215, 0.610, 0.355, 1.000) both;
    animation: tracking-in-expand 0.7s cubic-bezier(0.215, 0.610, 0.355, 1.000) both;
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