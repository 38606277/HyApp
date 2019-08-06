<template>
  <div>
    <van-tabbar v-model="active" class="tab" style="z-index:999">
      <van-tabbar-item icon="home-o" to="/Home">首页</van-tabbar-item>
      <van-tabbar-item icon="todo-list-o" to="/AssetFind">搜索</van-tabbar-item>
      <van-tabbar-item icon="label-o" to="/AssetView">待阅</van-tabbar-item>
      <van-tabbar-item icon="setting-o" to="/User">设置</van-tabbar-item>
    </van-tabbar>
  </div>
</template>

<script>
export default {
  data() {
    return {
      active: 0
    };
  },
  watch: {
    $route(to, from) {
      switch (to.fullPath) {
        case '/':
          this.active = 0;
          break;
        case '/Cart':
          this.active = 1;
          break;
        case '/User':
          this.active = 2;
          break;
        default:
          this.active = 0;
      }
    }
  }
};
</script>

<style lang="stylus" scoped>
</style>
