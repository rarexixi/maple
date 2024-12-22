<script setup lang="ts">
import { h, reactive } from "vue"
import { PlusOutlined } from "@ant-design/icons-vue"

const emit = defineEmits<{
  (e: 'add', pluginType: string, pluginName: string): void
}>()

const plugins = reactive({
  source: ['jdbc', 'kafka', 'upsert-kafka', 'doris', 'starrocks', "mysql-cdc", "postgres-cdc", "oracle-cdc", "sqlserver-cdc", "db2-cdc", "oceanbase-cdc", "tidb-cdc"],
  transformation: ['sql'],
  sink: ['jdbc', 'doris', 'starrocks', 'kafka', 'upsert-kafka'],
})

const add = (pluginType: any) => {
  emit('add', pluginType.keyPath[0], pluginType.keyPath[1])
}
</script>

<template>
  <a-dropdown :trigger="['click']">
    <template #overlay>
      <a-menu @click="add">
        <a-sub-menu v-for="(value, key) in plugins" :key="key" :title="key">
          <a-menu-item v-for="v in value" :key="v">{{ v }}</a-menu-item>
        </a-sub-menu>
      </a-menu>
    </template>
    <a-tooltip title="添加">
      <a-button type="link" :icon="h(PlusOutlined)" />
    </a-tooltip>
  </a-dropdown>
</template>