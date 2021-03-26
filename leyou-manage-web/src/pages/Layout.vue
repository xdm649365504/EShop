<template>
  <v-app v-bind:dark="dark">
    <v-navigation-drawer
      v-bind:dark="dark"
      persistent
      v-bind:mini-variant="miniVariant"
      v-model="drawer"
      enable-resize-watcher
      fixed
      app
    >
      <v-toolbar flat class="transparent">
        <v-list class="pa-0">
          <v-list-tile avatar>
            <v-list-tile-avatar>
              <img src="../assets/2.jpeg">
            </v-list-tile-avatar>
            <v-list-tile-content>
              <v-list-tile-title>谢多谋</v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
        </v-list>
      </v-toolbar>
      <v-divider/>

      <v-list class="pt-0" dense>
        <v-list-group
          v-model="item.active"
          v-for="item in items"
          :key="item.title"
          :prepend-icon="item.action"
          no-action
        >
          <!--一级 -->
          <v-list-tile slot="activator">
            <v-list-tile-content>
              <v-list-tile-title>{{ item.title }}</v-list-tile-title>
            </v-list-tile-content>
          </v-list-tile>
          <!-- 二级 -->
          <v-list-tile v-for="subItem in item.items" :key="subItem.title" :to="item.path + subItem.path">
            <v-list-tile-content>
              <v-list-tile-title>{{ subItem.title }}</v-list-tile-title>
            </v-list-tile-content>
            <v-list-tile-action>
              <v-icon>{{ subItem.action }}</v-icon>
            </v-list-tile-action>
          </v-list-tile>
        </v-list-group>
      </v-list>
    </v-navigation-drawer>
    <!--工具条 -->
    <v-toolbar  app dark :color="dark ? 'secondary' : 'primary'"  >
      <v-toolbar-side-icon @click.stop="drawer = !drawer"/>
 
      <v-btn icon @click.stop="miniVariant = !miniVariant">
        <v-icon v-html="miniVariant ? 'chevron_right' : 'chevron_left'"/>
      </v-btn>

      <v-flex xs3></v-flex><!--标题和前面的空白-->
      <v-toolbar-title v-text="title"/>
      <v-spacer/><!--自动填充-->
    </v-toolbar>
    <v-content>
      <v-breadcrumbs>
        <v-icon slot="divider">chevron_right</v-icon>
        <v-breadcrumbs-item>{{item1}}</v-breadcrumbs-item>
        <v-breadcrumbs-item>{{item2}}</v-breadcrumbs-item>
      </v-breadcrumbs>
      <div>
        <router-view/>
      </div>
    </v-content>
  </v-app>
</template>

<script>
  import menus from "../menu";

  export default {
    data() {
      return {
        dark: false,// 是否暗黑主题
        drawer: true,// 左侧导航是否隐藏
        miniVariant: false,// 左侧导航是否收起
        title: '乐优商城后台管理',// 顶部导航条名称,
        menuMap: {}
      }
    },
    computed: {
      items() {
        return menus;
      },

    },
    name: 'App',


  }
</script>
<style scoped>
  .box {
    width: 90%;
  }
</style>


