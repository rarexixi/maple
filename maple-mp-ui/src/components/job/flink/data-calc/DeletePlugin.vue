<script setup lang="ts">
import { h } from 'vue'
import { MinusOutlined } from '@ant-design/icons-vue'

const emit = defineEmits<{
  (e: 'delete'): void
}>()
</script>

<template>
  <a-popconfirm @confirm="() => emit('delete')" ok-text="确定" cancel-text="取消" placement="right" :show-cancel="true">
    <template #title>确定删除吗？</template>
    <a-tooltip title="删除">
      <a-button type="link" :icon="h(MinusOutlined)" danger />
    </a-tooltip>
  </a-popconfirm>
</template>