<script lang="ts">
import { defineComponent } from "vue"
import { MinusOutlined } from "@ant-design/icons-vue";

export default defineComponent({
  components: {
    MinusOutlined
  },
  emits: ['delete'],
  setup(props, { emit }) {

    const confirm = () => {
      emit('delete');
    }
    return {
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
          <minus-outlined />
        </template>
      </a-button>
    </a-tooltip>
  </a-popconfirm>
</template>