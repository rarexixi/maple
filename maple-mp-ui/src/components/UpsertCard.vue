<script lang="ts" setup>
const props = defineProps<{
  title?: string;
  backUrl: string;
}>();
</script>

<template>
  <a-card class="upsert-card">
    <template #title>
      <a-space align="center">
        <a :href="backUrl">
          <LeftOutlined />
          返回列表</a>
        <a-divider type="vertical" />
        <span v-if="!!props.title">{{title}}</span>
        <slot name="title" v-else></slot>
      </a-space>
    </template>
    <slot></slot>
  </a-card>
</template>

<style scoped>
.upsert-card {
  margin: 10px
}
</style>