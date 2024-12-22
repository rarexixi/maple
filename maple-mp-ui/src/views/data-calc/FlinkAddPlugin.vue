<script setup lang="ts">
import { computed, h, reactive } from "vue"
import { PlusOutlined } from "@ant-design/icons-vue"

const {pluginType = 'sources'} = defineProps<{ pluginType: string }>()
const pluginGroup = reactive({
  sources: ['jdbc', 'kafka', 'upsert-kafka', 'doris', 'starrocks'],
  transformations: ['sql'],
  sinks: ['jdbc', 'kafka', 'upsert-kafka', 'doris', 'starrocks'],
})

const plugins = computed(() => {
  switch (pluginType) {
    case 'source':
      return pluginGroup.sources
    case 'transformation':
      return pluginGroup.transformations
    case 'sink':
      return pluginGroup.sinks
    default:
      return ['']
  }
})

const emit = defineEmits<{
  (e: 'add', pluginName: string): void
}>()

const add = (pluginName: any) => {
  emit('add', pluginName.key)
}
</script>

<template>
  <a-dropdown :trigger="['click']">
    <template #overlay>
      <a-menu @click="add">
        <a-menu-item v-for="name in plugins" :key="name">{{ name }}</a-menu-item>
      </a-menu>
    </template>
    <a-button type="link" :icon="h(PlusOutlined)" />
  </a-dropdown>
</template>