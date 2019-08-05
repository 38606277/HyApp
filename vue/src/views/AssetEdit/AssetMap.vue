<template>
  <div class="mymapM">
    <!--捜索-->
    <div v-if="loading" class="loading">
      <van-loading type="spinner" />
    </div>
    <van-nav-bar
      title="资产地图"
      left-text="返回"
      left-arrow
      @click-left="goHome"
      @click-right="onClickRight"
    />
    <div class="search-box">
      <div class="search-postion">
        <input type="text" placeholder="输入关键字搜索" v-model="search_key" />
        <span class="clear" v-if="search_key" @click="search_key ='' ">
          <van-icon color="#8f8f8f" name="clear" />
        </span>
        <span class="buts border_but" @click="keySearch()">捜索</span>
      </div>
    </div>
    <div class="con-box con-map" v-if="!search_key">
      <!--地图-->
      <div class="mapbox">
        <div class="map" id="container"></div>
        <div class="sign"></div>
      </div>
    </div>
    <div class="con-box" v-if="!search_key">
      <!--地址列表-->
      <div class="Hlist-box">
        <ul>
          <li v-for="(item, index) in lists" :key="index" @click="onAddressLi(item)">
            <b>
              <van-icon color="#a6a6a6" name="clock" />
            </b>
            <div>
              <span>{{item.name}}</span>
              <p>{{item.address}}</p>
            </div>
          </li>
        </ul>
      </div>
    </div>
    <!--捜索列表-->
    <div class="search-list" v-if="search_key">
      <ul>
        <li v-for="(item, index) in search_list" :key="index" @click="onSearchLi(item.location)">
          <span>{{item.name}}</span>
          <p>{{item.address}}</p>
        </li>
        <li v-if="noSearchShow">
          <p>暂无搜索结果</p>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      center: [116.42792, 39.902896], //经度+纬度
      search_key: '', //搜索值
      lists: [], //地点列表
      search_list: [], //搜索结果列表
      marker: '',
      loading: false,
      noSearchShow: false //无搜索结果提示，无搜索结果时会显示暂无搜索结果
    };
  },
  mounted() {
    var self = this;

    dsBridge.call('locationApi.getLocationInfo', '获取定位信息', function(value) {
     alert(value); 
     self.center = value;
      setTimeout(() => {
      self.adMap();
    }, 1000);
    });
   
  },
  methods: {
    goHome() {
      this.$router.push('/');
    },
    adMap() {
      this.loading = true;
      //初始化地图
      var map = new AMap.Map('container', {
        zoom: 14, //缩放级别
        center: this.center //设置地图中心点
        //resizeEnable: true,  //地图初始化加载定位到当前城市
      });
      //获取初始中心点并赋值
      var currentCenter = map.getCenter(); //此方法是获取当前地图的中心点
      this.center = [currentCenter.lng, currentCenter.lat]; //将获取到的中心点的纬度经度赋值给data的center
      console.log(this.center);

      //创建标记
      this.marker = new AMap.Marker({
        position: new AMap.LngLat(currentCenter.lng, currentCenter.lat) // 经纬度对象，也可以是经纬度构成的一维数组[116.39, 39.9]
      });
      // 将创建的点标记添加到已有的地图实例：
      map.add(this.marker);

      //根据地图中心点查附近地点，此方法在下方
      this.centerSearch();
      //监听地图移动事件，并在移动结束后获取地图中心点并更新地点列表
      var moveendFun = e => {
        // 获取地图中心点
        currentCenter = map.getCenter();
        this.center = [currentCenter.lng, currentCenter.lat];
        this.marker.setPosition([currentCenter.lng, currentCenter.lat]); //更新标记的位置
        //根据地图中心点查附近地点
      };
      //更新数据
      var centerSearch = () => {
        this.loading = true;
        this.centerSearch();
      };

      // 绑定事件移动地图事件
      map.on('mapmove', moveendFun); //更新标记
      map.on('moveend', centerSearch); //更新数据
    },
    centerSearch() {
      AMap.service(['AMap.PlaceSearch'], () => {
        //构造地点查询类
        var placeSearch = new AMap.PlaceSearch({
          type:
            '汽车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|地名地址信息', // 兴趣点类别
          pageSize: 30, // 单页显示结果条数
          pageIndex: 1, // 页码
          city: '全国', // 兴趣点城市
          autoFitView: false // 是否自动调整地图视野使绘制的 Marker点都处于视口的可见范围
        });
        //根据地图中心点查附近地点
        placeSearch.searchNearBy('', [this.center[0], this.center[1]], 200, (status, result) => {
          if (status == 'complete') {
            this.lists = result.poiList.pois; //将查询到的地点赋值
            this.loading = false;
          }
        });
      });
    },
    keySearch() {
      this.loading = true;
      AMap.service(['AMap.PlaceSearch'], () => {
        //构造地点查询类
        var placeSearch = new AMap.PlaceSearch({
          type:
            '汽车服务|餐饮服务|购物服务|生活服务|体育休闲服务|医疗保健服务|住宿服务|风景名胜|商务住宅|政府机构及社会团体|科教文化服务|交通设施服务|金融保险服务|公司企业|地名地址信息', // 兴趣点类别
          pageSize: 30, // 单页显示结果条数
          pageIndex: 1, // 页码
          city: '全国', // 兴趣点城市
          citylimit: false, //是否强制限制在设置的城市内搜索
          autoFitView: false // 是否自动调整地图视野使绘制的 Marker点都处于视口的可见范围
        });
        //关键字查询
        placeSearch.search(this.search_key, (status, result) => {
          if (status == 'complete') {
            if (result.poiList.count === 0) {
              this.noSearchShow = true;
            } else {
              this.search_list = result.poiList.pois; //将查询到的地点赋值
              this.noSearchShow = false;
              this.loading = false;
            }
          } else {
            this.search_list = [];
            this.noSearchShow = true;
          }
        });
      });
    },
    onAddressLi(e) {
      console.log(e);
      this.marker.setPosition([e.location.lng, e.location.lat]); //更新标记的位置
    },
    onSearchLi(e) {
      console.log(e.lng + '-' + e.lat);
      this.center = [e.lng, e.lat];
      console.log(this.center);
      this.search_key = '';
      // this.loading=true;
      setTimeout(() => {
        this.adMap();
      }, 1000);
    }
  },
  watch: {
    search_key(newv, oldv) {
      if (newv == '') {
        this.search_list = [];
        this.noSearchShow = false;
        setTimeout(() => {
          this.adMap();
        }, 1000);
      }
    }
  }
};
</script>


<style lang="scss" scoped>
.con-map {
  height: 190px;
  width: 100%;
}
.con-box {
  .mapbox {
    margin-bottom: 10px;
    position: fixed;
    z-index: 111;
    width: 100%;
    height: 380px;
    padding: 10px 0;
    background: #eceeee;

    .map {
      width: 100%;
      height: 380px;
    }
  }

  .Hlist-box {
    width: 96%;
    margin: 0 auto;

    background: #fff;
    border-radius: 5px;
    li {
      height: 40px;
      padding: 14px 22px;
      border-bottom: 1px solid #d9d9d9;
      display: flex;
      b {
        display: inline-block;

        i {
          margin: 18px 18px 0 0;
        }
      }
      div {
        width: 100%;
      }
      span {
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        font-size: 15px;
        display: inline-block;
        width: 90%;
      }
      p {
        margin-top: 10px;
        color: #a6a6a6;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        font-size: 13px;
        width: 90%;
      }
    }
  }
}
.mymapM {
  .search-box {
    height: 48px;

    line-height: 48px;
    background: #fff;

    border-bottom: 1px solid #bfbec4;

    .search-postion {
      height: 48px;
      line-height: 48px;
      background: #fff;
      border-bottom: 1px solid #bfbec4;
      width: 100%;
      position: fixed;
      z-index: 99999;
      display: flex;
      input {
        flex: 4;
        height: 14px;
        padding: 16px 0;
        border: none;

        text-indent: 10px;
      }
      .clear {
        margin: 2px 6px;
      }
      .buts {
        width: 15%;
        text-align: center;
        display: inline-block;
        flex: 1;
      }
      .border_but {
        border-left: 1px solid #8f8f8f;
        height: 14px;
        line-height: 14px;
        margin: 17px 0;
      }
    }
  }
  .search-list {
    width: 96%;
    margin: 0 auto;
    margin-top: 10px;
    border-radius: 5px;
    background: #fff;
    li {
      height: 40px;
      padding: 14px 22px;
      border-bottom: 1px solid #d9d9d9;
      span {
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        font-size: 15px;
        display: inline-block;
        width: 90%;
      }
      p {
        margin-top: 10px;
        color: #a6a6a6;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        font-size: 13px;
        width: 90%;
      }
    }
  }
}
.loading {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 99999999;
}
</style>