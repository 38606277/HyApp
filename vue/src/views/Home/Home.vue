<template>
  <div class="home">
    <van-search v-model="searchValue" placeholder="请输入搜索关键词" show-action @search="onSearch">
      <div slot="action" @click="onSearch">搜索</div>
    </van-search>
    <div class="home-swipe">
      <van-swipe :autoplay="3000" class="swipe" @change="changeSwipe">
        <van-swipe-item class="swipe-item">
          <img src="@/images/swipe/swipe-1.jpg" />
        </van-swipe-item>
        <van-swipe-item class="swipe-item">
          <img src="@/images/swipe/swipe-2.jpg" />
        </van-swipe-item>
        <van-swipe-item class="swipe-item">
          <img src="@/images/swipe/swipe-3.jpg" />
        </van-swipe-item>
        <van-swipe-item class="swipe-item">
          <img src="@/images/swipe/swipe-4.jpg" />
        </van-swipe-item>
      </van-swipe>
      <!-- <div class="recommend-title">功能</div> -->

      <van-grid :column-num="4">
        <van-grid-item key="1" to="/AssetList">
          <img class="function-item" src="@/images/icon/f3dd6c383aeb3b02.png" />
          <div class="van-grid-item__text">我的资产</div>
        </van-grid-item>
        <van-grid-item key="2" to="/AssetView">
          <img class="function-item" src="@/images/icon/13ceb38dcf262f02.png" />
          <div class="van-grid-item__text">资产卡片</div>
        </van-grid-item>
        <van-grid-item key="3" to="/AssetMap">
          <img class="function-item" src="@/images/icon/c70f1e85ae4a4f02.png" />
          <div class="van-grid-item__text">资产地图</div>
        </van-grid-item>
        <van-grid-item key="4" to="/AssetList">
          <img class="function-item" src="@/images/icon/6f7d6e44963c9302.png" />
          <div class="van-grid-item__text">盘点</div>
        </van-grid-item>
        <van-grid-item key="5" to="/AssetView">
          <img class="function-item" src="@/images/icon/fdf170ee89594b02.png" />
          <div class="van-grid-item__text">资产调拨</div>
        </van-grid-item>
        <van-grid-item key="6" to="/AssetMap">
          <img class="function-item" src="@/images/icon/26ffa31b56646402.png" />
          <div class="van-grid-item__text">资产分析</div>
        </van-grid-item>
        <van-grid-item key="5" to="/AssetView">
          <img class="function-item" src="@/images/icon/fdf170ee89594b02.png" />
          <div class="van-grid-item__text">资产调拨</div>
        </van-grid-item>
        <van-grid-item key="6" to="/AssetMap">
          <img class="function-item" src="@/images/icon/26ffa31b56646402.png" />
          <div class="van-grid-item__text">资产分析</div>
        </van-grid-item>
      </van-grid>
    </div>
    <good-item title="我的任务" describe moreRoute="/more/1" class="good"></good-item>
    <div id="echartContainer" style="width:100%; height:250px;" class="chart"></div>
  </div>
</template>

<script>
import goodItem from '@/components/goodItem/goodItem';
import scrollX from '@/components/scroll/scrollX';
import backgroundImg from '@/components/backgroundImg/backgroundImg';
import tabItem from '@/components/tabItem/tabItem';
import { hotSale, saleGroup, discover } from '@/api/api';
import { mapMutations } from 'vuex';
import { Grid, GridItem } from 'vant';
var echarts = require('echarts');
export default {
  name: 'Home',
  data() {
    return {
      searchValue: '',
      indexPage: 1,
      hotGoods: [],
      saleGroupGoods: [],
      discoverGoods: []
    };
  },
  mounted() {
    // 基于准备好的dom，初始化echarts实例
    var myChart = echarts.init(document.getElementById('echartContainer'));

    // 绘制图表
    myChart.setOption({
      backgroundColor:'white',
      xAxis: {
        type: 'category',
        data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [820, 932, 901, 934, 1290, 1330, 1320],
          type: 'line'
        }
      ]
    });
  },
  components: {
    goodItem,
    scrollX,
    backgroundImg,
    tabItem
  },
  computed: {},
  methods: {
    onSearch() {
      console.log('onSearch');
    },
    changeSwipe(index) {
      this.indexPage = index;
    },
    showGood(item) {
      this.setGood(item);
      this.$router.push('/Good');
    },
    ...mapMutations({
      setGood: 'SET_GOOD_MUTATION'
    })
  }
};
</script>

<style lang="stylus" scoped>
.home {
  background-color: #eee;
  margin-bottom: 50px;
}

.home-swipe {
  box-sizing: border-box;
  padding: 4px 6px;
  background-color: #fff;
  border-bottom: 1px solid #dedede;

  .home-swipe-head {
    padding: 4px 0;

    .recommend {
      font-weight: 700;
    }

    .tips {
      font-size: 12px;
      margin-left: 6px;
      color: #8f8f8f;
    }

    .swipe-num {
      float: right;

      .indexPage {
        font-weight: 700;
      }

      .pageNum {
        font-size: 12px;
        color: #8f8f8f;
      }
    }
  }

  .swipe {
    img {
      width: 100%;
    }
  }
}

.scroll-hot {
  width: 100%;
  overflow: hidden;

  .scroll-item {
    display: inline-block;
  }
}

.sale-ul {
  display: flex;

  .sale-item {
    flex-grow: 1;
    padding: 2px 4px;

    img {
      width: 100%;
    }

    .sale-title {
      text-align: center;
      font-size: 14px;

      .sale-price {
        color: #ff4c0a;
        font-size: 16px;
      }
    }
  }
}

.discover-ul {
  display: flex;
  height: 100px;
  margin-bottom: 4px;

  .discover-img {
    width: 100px;
  }

  .discover-li {
    flex-grow: 1;
    margin-right: 4px;

    &:last-child {
      margin-right: 0;
    }
  }
}

.function-item {
  height: 48px;
  width: 48px;
}

.function-name {
  font-size: 12px;
}

.recommend-title {
  display: flex;
  justify-content: left;
  align-items: center;
  text-align: center;
  font-size: 14px;
  height: 30px;

  .border {
    width: 12px;
    height: 1px;
    margin: 0 8px;
    background-color: #000;
  }

  .chart {
    background-color: #fff;
  }

  .good {
    font-size: 18px;
  }
}
</style>
