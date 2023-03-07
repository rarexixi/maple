<script lang="ts">
import {defineComponent} from 'vue'
import zhCN from 'ant-design-vue/es/locale/zh_CN';
import NavMenu from '@/components/NavMenu.vue';

const menus = [
    {name: '数据源类型', path: '/datasource-type', type: 'menu-item'},
    {name: '数据源', path: '/datasource', type: 'menu-item'},
    {name: '数据计算配置', path: '/', type: 'menu-item'},
]

export default defineComponent({
    components: {NavMenu},
    setup() {
        return {
            zhCN,
            menus
        }
    },
})
</script>

<template>
    <a-config-provider :locale="zhCN" :component-size="'small'">
        <a-layout :style="{ height: '100vh', overflow: 'auto' }" class="layout">
            <a-layout-header>
                <div class="logo"/>
                <nav-menu :menus="menus"></nav-menu>
            </a-layout-header>
            <a-layout-content :style="{ overflow: 'auto' }">
                <router-view/>
            </a-layout-content>
        </a-layout>
    </a-config-provider>
</template>

<style scoped>
</style>



