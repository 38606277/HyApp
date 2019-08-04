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
      <div class="recommend-title">功能</div>
      <van-grid :column-num="3">
        <van-grid-item key="1" icon="photo-o" to="/AssetEdit" text="个人资产展示" />
        <van-grid-item key="2" icon="cart-o" to="AssetView" text="资产信息修改" />
        <van-grid-item key="3" icon="like-o" text="部门内资产责任人变更" />
        <van-grid-item key="4" icon="home-o" text="名下资产" />
        <van-grid-item key="5" icon="star-o" text="部门内资产地点调拨" />
        <van-grid-item key="6" icon="chat-o" text="部门间资产调拨" />
        <van-grid-item key="7" icon="photo-o" text="个人资产展示" />
        <van-grid-item key="8" icon="aim" text="资产信息修改" />
        <van-grid-item key="9" icon="more-o" text="部门内资产责任人变更" />
      </van-grid>
    </div>
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
    hotSale()
      .then(result => {
        this.hotGoods = result.data;
      })
      .catch(error => {
        console.log(error);
      });
    saleGroup()
      .then(result => {
        this.saleGroupGoods = result.data;
      })
      .catch(error => {
        console.log(error);
      });
    discover()
      .then(result => {
        this.discoverGoods = result.data;
      })
      .catch(error => {
        console.log(error);
      });
  },
  components: {
    goodItem,
    scrollX,
    backgroundImg,
    tabItem
  },
  computed: {
    goodItems() {
      return {
        推荐: this.hotGoods,
        拼团: this.saleGroupGoods,
        低价: this.discoverGoods,
        发现: this.hotGoods,
        火爆: this.saleGroupGoods
      };
    }
  },
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

.recommend-title {
  display: flex;
  justify-content: left ;
  align-items: center ;
  text-align: center ;
  font-size: 14px;
  height: 30px;

  .border {
    width: 12px;
    height: 1px;
    margin: 0 8px;
    background-color: #000;
  }
}
</style>
