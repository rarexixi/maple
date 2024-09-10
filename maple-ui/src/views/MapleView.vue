<script lang="ts">
import { defineComponent, ref } from "vue"
import MapleArrayView from "./MapleArrayView.vue"
import MapleGroupView from "./MapleGroupView.vue"


export default defineComponent({
  components: {
    MapleArrayView,
    MapleGroupView,
  },
  setup() {
    const mode = ref('array')
    return {
      mode
    }
  }
})

</script>

<template>
  <a-breadcrumb separator="/">
    <a-breadcrumb-item>数据计算配置</a-breadcrumb-item>
    <a-breadcrumb-item>
      <a-radio-group v-model:value="mode">
        <a-radio value="array">数组</a-radio>
        <a-radio value="group">分组</a-radio>
      </a-radio-group>
    </a-breadcrumb-item>
  </a-breadcrumb>
  <maple-array-view v-if="mode == 'array'"/>
  <maple-group-view v-if="mode == 'group'"/>
</template>

<style lang="less" scoped>
</style>