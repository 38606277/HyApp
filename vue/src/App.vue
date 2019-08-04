<template>
  <div id="app">
    <keep-alive include="Home,Cart,User">
      <router-view/>
    </keep-alive>
    <Tab v-show="!$route.meta.fullScreen" />
  </div>
</template>

<script>
import Tab from '@/views/Tab/Tab';
export default {
  components: {
    Tab
  }
};
</script>
<style lang="stylus">
html, body, #app
  width 100%
  height 100%
#github
  position fixed
  top 0
  right 60px
  z-index 999
</style>
