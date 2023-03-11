<script type="ts">
import {defineComponent, ref} from "vue"
import {MinusOutlined} from "@ant-design/icons-vue";

export default defineComponent({
  components: {
    MinusOutlined
  },
  props: {
    types: {
      type: Array,
      isRequired: true
    }
  },
  emits: ['delete'],
  setup(props, {emit}) {

    const type = ref(props.types[0]);

    const confirm = () => {
      emit('delete', type.value);
    }
    return {
      type,
      confirm,
    }
  },
})
</script>

<template>
  <a-popconfirm @confirm="confirm" ok-text="确定" cancel-text="取消" placement="right" :show-cancel="true">
    <template #title>确定删除吗？</template>
    <a-tooltip title="删除">
      <a-button type="link" danger>
        <template #icon>
          <minus-outlined/>
        </template>
      </a-button>
    </a-tooltip>
  </a-popconfirm>
</template>