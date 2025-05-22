<script setup lang="ts">
import { UpOutlined, DownOutlined } from '@ant-design/icons-vue'

import DeletePlugin from "@/components/job/spark/data-calc/DeletePlugin.vue"

const {expand, index} = defineProps<{
  expand: boolean,
  index: number
}>()

const emit = defineEmits<{
  (e: 'update:expand', data: any): void
  (e: 'delete'): void
}>()

const triggerChange = () => {
  emit('update:expand', !expand)
}
</script>

<template>
  <a-button type="link" @click="triggerChange">
    {{ index + 1 }}
    <UpOutlined v-if="expand" />
    <DownOutlined v-else />
  </a-button>
  <DeletePlugin @delete="() => emit('delete')" />
</template>