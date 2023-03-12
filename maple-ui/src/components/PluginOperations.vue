<script lang="ts">
import { defineComponent, ref } from "vue"
import { MinusOutlined } from "@ant-design/icons-vue";
// import AddPlugin from "@/components/AddPlugin.vue"
import DeletePlugin from "@/components/DeletePlugin.vue"

export default defineComponent({
  components: {
    MinusOutlined,
    // AddPlugin,
    DeletePlugin,
  },
  props: {
    value: { type: Object, isRequired: true },
    index: { type: Number, isRequired: true },
    types: { type: Array<String>, isRequired: true },
  },
  emits: ['update:value', 'add', 'delete'],
  setup(props, { emit }) {

    const index = ref(props.index);

    // const type = ref(props.types[0]);
    // const addPlugin = () => {
    //   emit('add', type.value);
    // }
    const delPlugin = () => {
      emit('delete');
    }

    const triggerChange = (expand2: boolean) => {
      emit('update:value', { expand: expand2 });
    };

    return {
      // type,
      index,
      triggerChange,
      // addPlugin,
      delPlugin,
    }
  },
})
</script>

<template>
  <a-button type="link" @click="() => triggerChange(!value.expand)">
    {{ index + 1 }}
    <up-outlined v-if="value.expand" />
    <down-outlined v-else />
  </a-button>
  <!-- <add-plugin :types="types" @add="addPlugin" /> -->
  <delete-plugin @delete="delPlugin" />
</template>