<script setup lang="ts">
import { onMounted, ref } from "vue"
import common from "@/composables/common"

const sourceTable = defineModel<string>("sourceTable")
const sourceQuery = defineModel<string>("sourceQuery")

const useTable = ref<number>(1)

const labelCols = common.Layout.labelCols

const selectOptions = [{
  label: "来源表",
  value: 1,
}, {
  label: "来源语句",
  value: 0,
}]

onMounted(() => {
  useTable.value = !sourceQuery.value ? 1 : 0
})

function changeOption(value: number) {
  if (value == 0) {
    sourceTable.value = ""
  } else {
    sourceQuery.value = ""
  }
}
</script>

<template>
  <a-form-item name="sourceTable" class="form-item-360" v-if="useTable === 1" :rules="[{required: true}]" htmlFor="">
    <template #label>
      <a-form-item-rest>
        <a-select v-model:value="useTable" :options="selectOptions" @change="changeOption"/>
      </a-form-item-rest>
    </template>
    <a-input v-model:value="sourceTable" />
  </a-form-item>
  <a-form-item name="sourceQuery" :label-col="labelCols.l125" class="form-item-720" v-else :rules="[{required: true}]" htmlFor="">
    <template #label>
      <a-form-item-rest>
        <a-select v-model:value="useTable" :options="selectOptions" @change="changeOption"/>
      </a-form-item-rest>
    </template>
    <a-textarea v-model:value="sourceQuery" :auto-size="{ minRows: 2, maxRows: 20 }" />
  </a-form-item>
</template>

<style lang="less" scoped>
:deep(.label-switch) {
  &.ant-switch {
    background: rgba(0, 0, 0, 0.1);

    &.ant-switch-checked {
      background: rgba(22, 119, 255, 0.1);
    }

    .ant-switch-inner-checked,
    .ant-switch-inner-unchecked {
      font-size: 14px;
      color: #000000;
    }
  }
}
</style>