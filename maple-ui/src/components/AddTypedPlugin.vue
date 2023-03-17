<script lang="ts">
import { defineComponent, reactive, ref } from "vue"


export default defineComponent({
  emits: ['add'],
  setup(props, { emit }) {
    const plugins = reactive({
      sources: ['jdbc', 'managed_jdbc', 'file'],
      transformations: ['sql'],
      sinks: ['hive', 'jdbc', 'managed_jdbc', 'file'],
    })

    const plugin = ref<string>('')

    const confirm = () => {
      let arr = plugin.value.split('##')
      if (arr.length == 2) {
        emit('add', arr[0], arr[1])
      }
    }

    return {
      plugin,
      plugins,
      confirm,
    }
  },
})
</script>

<template>
  <a-popconfirm @confirm="confirm" ok-text="确定" placement="right" :show-cancel="false">
    <template #title>
      <a-typography-title :level="5">请选择插件</a-typography-title>
      <a-radio-group v-model:value="plugin">
        <label>source：</label>
        <template v-for="pluginName in plugins.sources" :key="`source-${pluginName}`">
          <a-radio :value="`source##${pluginName}`">{{ pluginName }}</a-radio>
          </template>
        <br>
        <label>transformation：</label>
        <template v-for="pluginName in plugins.transformations" :key="`transformation-${pluginName}`">
          <a-radio :value="`transformation##${pluginName}`">{{ pluginName }}</a-radio>
          </template>
        <br>
        <label>sink：</label>
        <template v-for="pluginName in plugins.sinks" :key="`sink-${pluginName}`">
          <a-radio :value="`sink##${pluginName}`">{{ pluginName }}</a-radio>
          </template>
      </a-radio-group>
    </template>
    <a-tooltip title="添加">
      <a-button type="link">
        <template #icon><plus-outlined /></template>
      </a-button>
    </a-tooltip>
  </a-popconfirm>
</template>