<script lang="ts">
import { defineComponent, ref } from "vue"
import zhCN from "ant-design-vue/es/locale/zh_CN"
import NavMenu from "@/components/NavMenu.vue"
import type {MenuItem} from "@/components/NavMenu.vue"

const menus = [
  { name: "数据源类型", path: "/datasource-type", type: "menu-item", icon: "appstore-outlined", children: [] } as MenuItem,
  { name: "数据源", path: "/datasource", type: "menu-item", icon: "database-outlined", children: [] } as MenuItem,
  { name: "数据计算配置", path: "/", type: "menu-item", icon: "calculator-outlined", children: [] } as MenuItem,
]

export default defineComponent({
  components: { NavMenu },
  setup() {
    const collapsed = ref(true);
    return {
      zhCN,
      collapsed,
      menus,
    }
  },
})
</script>

<template>
  <a-config-provider :locale="zhCN" :component-size="'small'">
    <a-layout :style="{ height: '100vh', overflow: 'auto' }" class="layout">
      <a-layout-sider v-model:collapsed="collapsed" collapsible>
        <div class="logo">Maple</div>
        <nav-menu :menus="menus"></nav-menu>
      </a-layout-sider>
      <a-layout-content :style="{ overflow: 'auto' }">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-config-provider>
</template>

<style scoped>
.logo {
  color: #ffffff;
  text-align: center;
  font-size: 24px;
  line-height: 32px;
  font-weight: bold;
  height: 32px;
  margin: 16px 5px;
  background: rgba(255, 255, 255, 0.3);
}
</style>



