(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["AssetView"],{2098:function(e,t,a){},8977:function(e,t,a){"use strict";a.r(t);var n=function(){var e=this,t=e.$createElement,a=e._self._c||t;return a("div",[a("van-nav-bar",{attrs:{title:"资产卡片","left-text":"返回","left-arrow":""},on:{"click-left":e.goHome,"click-right":e.onClickRight}}),a("van-cell-group",[a("van-field",{attrs:{required:"",clearable:"",label:"资产标签号","right-icon":"scan",placeholder:"扫描输入"},on:{"click-right-icon":function(t){return e.scanQrCode()}},model:{value:e.tagCode,callback:function(t){e.tagCode=t},expression:"tagCode"}}),a("van-field",{attrs:{required:"",clearable:"",label:"资产编号"},on:{"click-right-icon":function(t){return e.$toast("question")}},model:{value:e.username,callback:function(t){e.username=t},expression:"username"}}),a("van-field",{attrs:{required:"",clearable:"",label:"资产名称"},on:{"click-right-icon":function(t){return e.$toast("question")}},model:{value:e.username,callback:function(t){e.username=t},expression:"username"}}),a("van-field",{attrs:{clearable:"",label:"资产型号"},on:{"click-right-icon":function(t){return e.$toast("question")}},model:{value:e.username,callback:function(t){e.username=t},expression:"username"}}),a("van-cell",{attrs:{title:"所属部门","is-link":""}}),a("van-uploader",{attrs:{multiple:""},model:{value:e.fileList,callback:function(t){e.fileList=t},expression:"fileList"}}),a("van-field",{attrs:{clearable:"",label:"资产原值"},on:{"click-right-icon":function(t){return e.$toast("question")}},model:{value:e.username,callback:function(t){e.username=t},expression:"username"}}),a("van-field",{attrs:{clearable:"",label:"累计折旧"},on:{"click-right-icon":function(t){return e.$toast("question")}},model:{value:e.username,callback:function(t){e.username=t},expression:"username"}}),a("van-button",{attrs:{type:"primary",size:"large"},on:{click:function(t){return e.saveClick()}}},[e._v("保存")])],1)],1)},l=[],i=a("2620"),c={name:"User",data:function(){return{fileList:[{url:"https://img.yzcdn.cn/vant/cat.jpeg"}],tagCode:"aaa",imageURL:"../../images/good/pic-7.jpg",value1:0,value2:0,value3:"a",option1:[{text:"全部资产",value:0},{text:"新款商品",value:1},{text:"活动商品",value:2}],option2:[{text:"管理类",value:0},{text:"新款商品",value:1},{text:"活动商品",value:2}],option3:[{text:"默认排序",value:"a"},{text:"好评排序",value:"b"},{text:"销量排序",value:"c"}]}},methods:{goHome:function(){this.$router.push("/")},onClickRight:function(){Object(i["a"])("按钮")},scanQrCode:function(){Object(i["a"])("二维码");var e=this;dsBridge.call("cameraApi.scanQrCode","二维码扫描",function(t){alert(t),e.tagCode=t})},saveClick:function(){this.tagCode="bbb"}}},o=c,r=(a("baef"),a("6691")),u=Object(r["a"])(o,n,l,!1,null,"7bffcdca",null);t["default"]=u.exports},baef:function(e,t,a){"use strict";var n=a("2098"),l=a.n(n);l.a}}]);
//# sourceMappingURL=AssetView.ca1d89f6.js.map