<script setup lang="ts">
import { PlusOutlined, CheckOutlined, StopOutlined, DeleteOutlined, } from '@ant-design/icons-vue';
import { h } from "vue"

const {
  selected = false,
  canAdd = true,
  canEnable = true,
  canDisable = true,
  canDel = true,
} = defineProps<{
  selected: boolean
  canAdd?: boolean
  canEnable?: boolean
  canDisable?: boolean
  canDel?: boolean
}>()

const emit = defineEmits<{
  (e: 'add'): void
  (e: 'enable'): void
  (e: 'disable'): void
  (e: 'edit'): void
  (e: 'del'): void
}>()
</script>

<template>
  <div class="operation-btns">
    <slot name="before" />
    <a-button v-if="canAdd" @click="() => emit('add')" :icon="h(PlusOutlined)" class="btn-primary">添加</a-button>
    <template v-if="selected">
      <a-button v-if="canEnable" @click="() => emit('enable')" :icon="h(CheckOutlined)" class="btn-success">启用</a-button>
      <a-button v-if="canDisable" @click="() => emit('disable')" :icon="h(StopOutlined)" class="btn-warning">禁用</a-button>
      <a-button v-if="canDel" @click="() => emit('del')" :icon="h(DeleteOutlined)" class="btn-danger">删除</a-button>
    </template>
    <slot name="after" />
  </div>
</template>

<style scoped>

</style>