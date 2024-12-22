<script setup lang="ts">
import { ref } from "vue"

import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"

const value = defineModel<any>("value")
const showOptions = ref<boolean>(false)

const confirm = () => {
};
</script>

<template>
  <a-popconfirm placement="top" @confirm="confirm">
    <template #title>
      <a-input-string-map v-model:value="value" style="width: 640px" />
    </template>
    <a-button>设置参数</a-button>
  </a-popconfirm>
  <a-divider type="vertical" />
  <a-switch v-model:checked="showOptions" />
  展示参数
  <pre v-if="showOptions">{{ JSON.stringify(value, null, 4) }}</pre>
</template>

<style scoped>

</style>