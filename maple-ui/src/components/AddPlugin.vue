<script lang="ts">
import { defineComponent, ref } from "vue"

export default defineComponent({
  props: {
    types: {
      type: Array<String>,
      isRequired: true
    }
  },
  emits: ['add'],
  setup(props, { emit }) {

    const type = ref(props.types?.[0]);

    const confirm = () => {
      emit('add', type.value);
    }
    return {
      type,
      confirm,
    }
  },
})
</script>

<template>
  <a-popconfirm @confirm="confirm" ok-text="确定" placement="right" :show-cancel="false">
    <template #title>
      <a-typography-title :level="5">请选择类型</a-typography-title>
      <a-radio-group v-model:value="type">
        <template v-for="typeName in types" :key="type">
          <a-radio :value="typeName">{{ typeName }}</a-radio>
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