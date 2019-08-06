<template>
  <div>
    <van-nav-bar
      title="资产管理"
      left-text="返回"
      left-arrow
      @click-left="goHome"
      @click-right="onClickRight"
    />

    <van-dropdown-menu>
      <van-dropdown-item v-model="value1" :options="option1" />
      <van-dropdown-item v-model="value2" :options="option2" />
      <van-dropdown-item v-model="value3" :options="option3" />
    </van-dropdown-menu>

    <van-card num="2" price="5000" desc="thinkPad T480" title="笔记本电脑" :thumb="imageURL5">
      <!-- <div slot="footer">
        <van-button size="mini">转出</van-button>
        <van-button size="mini">详细</van-button>
      </div> -->
    </van-card>
    <van-card num="2" price="2.00" desc="描述信息" title="办公桌" :thumb="imageURL1" origin-price="10.00" />
    <van-card
      num="2"
      tag="标签"
      price="2.00"
      desc="描述信息"
      title="华为路由器"
     :thumb="imageURL2"
      origin-price="10.00"
    />
    <van-card
      num="2"
      tag="标签"
      price="2.00"
      desc="描述信息"
      title="投影仪"
     :thumb="imageURL3"
      origin-price="10.00"
    />
    <van-card
      num="2"
      price="2.00"
      desc="描述信息"
      title="商品标题"
     :thumb="imageURL4"
      origin-price="10.00"
    />
  </div>
</template>

<script>
import Vue from 'vue';

import { Toast } from 'vant';
import { DropdownMenu, DropdownItem, Card } from 'vant';
// Vue.use(DropdownMenu).use(DropdownItem);

export default {
  name: 'User',
  data() {
    return {
      imageURL1: require('../../images/good/pic-1.jpg'),
      imageURL2: require('../../images/good/pic-2.jpg'),
      imageURL3: require('../../images/good/pic-3.jpg'),
      imageURL4: require('../../images/good/pic-4.jpg'),
      imageURL5: require('../../images/good/pic-5.jpg'),

      value1: 0,
      value2: 0,
      value3: 'a',
      option1: [{ text: '全部资产', value: 0 }, { text: '新款商品', value: 1 }, { text: '活动商品', value: 2 }],
      option2: [{ text: '管理类', value: 0 }, { text: '新款商品', value: 1 }, { text: '活动商品', value: 2 }],
      option3: [{ text: '默认排序', value: 'a' }, { text: '好评排序', value: 'b' }, { text: '销量排序', value: 'c' }]
    };
  },

  methods: {
    goHome() {
      this.$router.push('/Home');
    },

    onClickRight() {
      Toast('按钮');
    }
  }
};
</script>

<style lang="stylus" scoped>
.user-poster {
  width: 100%;
  height: auto;
  display: block;
}

.user-link {
  text-align: center;
  font-size: 12px;
  padding: 15px 0;
  background-color: #fff;

  .van-icon {
    display: block;
    margin-bottom: 4px;
    font-size: 24px;
  }
}

.user-group {
  margin-bottom: 0.3rem;
}
</style>
