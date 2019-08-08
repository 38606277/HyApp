<template>
  <div>
    <van-nav-bar
      title="资产查找"
      left-text="返回"
      left-arrow
      @click-left="goHome"
      @click-right="onClickRight"
    />
     <van-search v-model="searchValue" placeholder="请输入搜索关键词" show-action @search="onSearch">
      <div slot="action" @click="onSearch">搜索</div>
    </van-search>
  </div>
</template>

<script>
import { Toast } from 'vant';


export default {
  name: 'User',
  data() {
    return {
      fileList: [{ url: 'https://img.yzcdn.cn/vant/cat.jpeg' }],
      tagCode: 'aaa',
      imageURL: require('../../images/icon/plus.png'),
      imageURL1:require('../../images/icon/plus.png'),
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
    },

    scanQrCode() {
      var self = this;
      dsBridge.call('cameraApi.scanQrCode', '二维码扫描', function(value) {
        self.tagCode = value;
      });
    },
    takePhoto() {
      var self = this;
      alert('takePhoto');
      dsBridge.call('cameraApi.takePhoto', '拍照', function(value) {
        alert(value);
        self.imageURL='file://' + JSON.parse(value)[0];
        // document.getElementById('imageView').src = 'file://' + JSON.parse(value)[0];
      });
    },
    selectPhoto(){
        var self = this;
          alert('selectPhoto');
          dsBridge.call("cameraApi.selectPhoto","选择图片",function (value) {
           alert(value);
            self.imageURL1='file://' + JSON.parse(value)[0];
            //  document.getElementById("selectPhotoBtn").innerHTML = (value);

        })
    },

    saveClick() {
      this.tagCode = 'bbb';
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

.selectimg{
  padding :15px;
}
</style>
