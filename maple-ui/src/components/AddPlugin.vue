<script lang="ts">
import { computed, defineComponent, reactive, ref } from "vue"

export default defineComponent({
  props: {
    pluginType: {
      type: String,
      isRequired: true,
      default: () => 'sources'
    }
  },
  emits: ['add'],
  setup(props, { emit }) {

    const pluginGroup = reactive({
      sources: ['jdbc', 'managed_jdbc', 'file'],
      transformations: ['sql'],
      sinks: ['hive', 'jdbc', 'managed_jdbc', 'file'],
    })

    const plugins = computed(() => {
      switch (props.pluginType) {
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

    const pluginName = ref(plugins.value[0]);


    const confirm = () => {
      emit('add', pluginName.value);
    }

    return {
      plugins,
      pluginName,
      confirm,
    }
  }
})
</script>

<template>
  <a-popconfirm @confirm="confirm" ok-text="确定" placement="right" :show-cancel="false">
    <template #title>
      <a-typography-title :level="5">请选择类型</a-typography-title>
      <a-radio-group v-model:value="pluginName">
        <template v-for="name in plugins" :key="type">
          <a-radio :value="name">{{ name }}</a-radio>
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