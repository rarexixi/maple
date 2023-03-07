<script type="ts">
import {defineComponent, ref} from "vue"
import {PlusOutlined} from "@ant-design/icons-vue";

export default defineComponent({
  components: {
    PlusOutlined
  },
  props: {
    types: {
      type: Array,
      isRequired: true
    }
  },
  emits: ['add'],
  setup(props, {emit}) {

    const type = ref(props.types[0]);

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
  <a-popconfirm @confirm="confirm" :icon="null" ok-text="确定" placement="right" :show-cancel="false">
    <template #title>
      <a-typography-title :level="5">请选择类型</a-typography-title>
      <a-radio-group v-model:value="type">
        <template v-for="type in types">
          <a-radio :value="type">{{ type }}</a-radio>
        </template>
      </a-radio-group>
    </template>
    <a-button type="link">
      <template #icon>
        <plus-outlined/>
      </template>
    </a-button>
  </a-popconfirm>
</template>