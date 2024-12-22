<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { onMounted, useTemplateRef } from "vue"

import common from "@/composables/common"
import type { validateFunction } from "@/composables/models"

interface SqlTransformationValue {
  resultTable: string,
  sql: string,
}

const rules = {
  resultTable: [{required: true}],
  sql: [{required: true}],
}

const {value, name} = defineProps<{
  value: SqlTransformationValue,
  name: string,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

const labelCols = common.Layout.labelCols

const formRef = useTemplateRef<FormInstance>("formRef");
const emit = defineEmits<{
  (e: 'push-validated', param: validateFunction): void
}>()

onMounted(() => {
  emit('push-validated', common.getFormValidateFun(formRef))
})
</script>

<template>
  <a-form ref="formRef" :name="name" :model="value" :rules="rules" :validate-messages="validateMessages"
          :label-col="labelCols.w320">
    <a-flex wrap="wrap">
      <a-form-item name="resultTable" label="注册表名" class="form-item-320">
        <a-input v-model:value="value.resultTable" />
      </a-form-item>
      <a-form-item name="sql" label="SQL" :label-col="labelCols.w1280" class="form-item-1280">
        <a-textarea v-model:value="value.sql" :auto-size="{ minRows: 2, maxRows: 20 }" />
      </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped></style>