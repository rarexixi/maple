<script setup lang="ts">
import { RouterView, RouterLink } from 'vue-router'
import { h, ref } from "vue"
import { useBreadcrumbStore } from "@/stores/breadcrumbs"
import zhCN from "ant-design-vue/es/locale/zh_CN"
import type { MenuProps } from "ant-design-vue"
import * as AntdIcons from '@ant-design/icons-vue'

const breadcrumbsStore = useBreadcrumbStore()
const collapsed = ref<boolean>(false)
const selectedKeys = ref<string[]>(['1'])
const selectedKeys2 = ref<string[]>(['1'])
const navMenuItems = ref<MenuProps['items']>([
  {
    key: 'sysConf',
    icon: () => h(AntdIcons['ControlOutlined']),
    label: h(RouterLink, {to: {path:'/sys-conf'}}, () => '系统配置'),
    title: 'sysConf',
  },
  {
    key: 'cluster',
    icon: () => h(AntdIcons['ClusterOutlined']),
    label: h(RouterLink, {to: {path:'/cluster'}}, () => '集群'),
    title: 'cluster',
  },
  {
    key: 'clusterEngine',
    icon: () => h(AntdIcons['CalculatorOutlined']),
    label: h(RouterLink, {to: {path:'/cluster-engine'}}, () => '计算引擎'),
    title: 'clusterEngine',
  },
  {
    key: 'datasourceType',
    icon: () => h(AntdIcons['AppstoreOutlined']),
    label: h(RouterLink, {to: {path:'/datasource-type'}}, () => '数据源类型'),
    title: 'datasourceType',
  },
  {
    key: 'datasource',
    icon: () => h(AntdIcons['DatabaseOutlined']),
    label: h(RouterLink, {to: {path:'/datasource'}}, () => '数据源'),
    title: 'datasource',
  },
  {
    key: 'job',
    icon: () => h(AntdIcons['PlaySquareOutlined']),
    label: h(RouterLink, {to: {path:'/job'}}, () => '作业'),
    title: 'job',
  },
  {
    key: 'dataCalc',
    icon: () => h(AntdIcons['CalculatorOutlined']),
    label: '数据计算-数组',
    children: [
      {
        key: 'sparkDataCalcArray',
        label: h(RouterLink, {to: {path:'/spark-data-calc-array'}}, () => '数组方式'),
        title: 'sparkDataCalcArray',
      },
      {
        key: 'sparkDataCalcGroup',
        label: h(RouterLink, {to: {path:'/spark-data-calc-group'}}, () => '分组方式'),
        title: 'sparkDataCalcGroup',
      },
    ]
  },
])

const headerNavMenuItems = ref<MenuProps['items']>([
  {
    key: 'user',
    icon: () => h(AntdIcons['UserOutlined']),
    label: 'rarexixi',
    title: 'user',
    children: [
      {
        key: 'profile',
        label: '个人信息',
        title: 'profile',
      },
      {
        key: 'logout',
        label: '退出',
        title: 'logout',
      }
    ]
  }
])

</script>

<template>
  <a-config-provider :locale="zhCN" :component-size="'small'">
    <a-layout>
      <a-layout-sider width="250" v-model:collapsed="collapsed">
        <div class="logo">
          <HomeOutlined />
          <span class="logo-text">MAPLE</span>
        </div>
        <a-menu v-model:selectedKeys="selectedKeys" :items="navMenuItems" theme="dark" />
      </a-layout-sider>
      <a-layout>
        <a-layout-header style="background: #ffffff; box-shadow: 0 3px 3px rgba(0,0,0,0.05); padding: 0; z-index: 2;">
          <a-flex :style="{ width: '100%', height: '100%', padding: '0 12px' }" :align="'center'">
            <a-button type="text" @click="() => (collapsed = !collapsed)"
                      style="font-size: 1.5rem; display: inline-block; height: 64px; margin-right: 20px;">
              <MenuUnfoldOutlined v-if="collapsed" />
              <MenuFoldOutlined v-else />
            </a-button>
            <a-breadcrumb style="line-height: 64px; width: 50%; font-size: 1rem">
              <template v-for="item in breadcrumbsStore.breadcrumbs">
                <a-breadcrumb-item :href="item.path">{{ item.text }}</a-breadcrumb-item>
              </template>
            </a-breadcrumb>
            <a-flex :style="{ width: '100%', height: '100%' }" :justify="'flex-end'" :align="'center'">
              <a-menu mode="horizontal" v-model:selectedKeys="selectedKeys2" :items="headerNavMenuItems" />
            </a-flex>
          </a-flex>
        </a-layout-header>
        <a-layout-content
            :style="{ background: '#efefef', padding: 0, margin: 0, height: 'calc(100vh - 96px)', overflow: 'auto' }">
          <RouterView />
        </a-layout-content>
        <a-layout-footer style="text-align: center; height: 32px; line-height: 32px; padding: 0">
          Ant Design ©2018 Created by Ant UED
        </a-layout-footer>
      </a-layout>
    </a-layout>
  </a-config-provider>
</template>

<style scoped>
.logo {
  float: left;
  font-size: 32px;
  text-align: left;
  font-weight: bold;
  line-height: 64px;
  height: 64px;
  width: 100%;
  color: #ffffff;
  padding: 0 24px;
  overflow: hidden;
}

.logo .logo-text {
  margin-left: 1rem;
}

:deep .ant-layout-sider-collapsed .logo > .logo-text {
  display: none;
}
</style>
