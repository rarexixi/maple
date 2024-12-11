<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { onMounted, useTemplateRef } from "vue"

import type { validateFunction } from "@/composables/models"
import common from "@/composables/common"

import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"

import SampleData from "@/assets/sample-data"

interface HiveSinkValue {
  sourceTable: string,
  sourceQuery: string,
  options: any,
  targetDatabase: string,
  targetTable: string,
  saveMode: string,
  strongCheck: boolean,
  writeAsFile: boolean,
  numPartitions: number,
}

const rules = {
  targetDatabase: [{required: true}],
  targetTable: [{required: true}],
  saveMode: [{required: true}],
  writeAsFile: [{required: true}],
  strongCheck: [{required: true}],
  numPartitions: [{type: 'number', min: 0, max: 99}],
  sourceTable: [{required: true}],
  sourceQuery: [{required: true}],
}

const {value, name} = defineProps<{
  value: HiveSinkValue,
  name: string,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}
const databases = SampleData.Databases
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
      <a-form-item name="targetDatabase" label="目标库" class="form-item-320">
        <a-select v-model:value="value.targetDatabase" placeholder="请选择">
          <template v-for="db in databases" :key="db.databaseName">
            <a-select-option :value="db.databaseName">{{ db.databaseName }}</a-select-option>
          </template>
        </a-select>
      </a-form-item>
      <a-form-item name="targetTable" label="目标表" class="form-item-320">
        <a-input v-model:value="value.targetTable" />
      </a-form-item>
      <a-form-item name="saveMode" label="写入模式" class="form-item-320">
        <a-radio-group v-model:value="value.saveMode">
          <a-radio-button value="append">追加</a-radio-button>
          <a-radio-button value="overwrite">覆盖</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item name="writeAsFile" label="文件优先" :label-col="labelCols.w160" class="form-item-160">
        <a-checkbox v-model:checked="value.writeAsFile" />
      </a-form-item>
      <a-form-item name="strongCheck" label="强校验" :label-col="labelCols.w160" class="form-item-160">
        <a-checkbox v-model:checked="value.strongCheck" />
      </a-form-item>
      <a-form-item name="numPartitions" label="分区数" class="form-item-320">
        <a-input-number v-model:value="value.numPartitions" />
      </a-form-item>
      <a-form-item name="sourceTable" label="来源表" class="form-item-320">
        <a-input v-model:value="value.sourceTable" />
      </a-form-item>
      <a-form-item name="sourceQuery" label="来源语句" :label-col="labelCols.w1280" class="form-item-1280">
        <a-textarea v-model:value="value.sourceQuery" :auto-size="{ minRows: 2, maxRows: 20 }" />
      </a-form-item>
      <a-form-item name="options" label="参数" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input-string-map v-model:value="value.options" />
      </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>