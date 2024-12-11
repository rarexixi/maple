<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { onMounted, useTemplateRef } from "vue"

import type { validateFunction } from "@/composables/models"
import common from "@/composables/common"

import AInputStringArray from "@/components/ant-ext/AInputStringArray.vue"
import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"

import { useSparkFileSerializersStore } from "@/stores/sys-conf";

interface FileSinkValue {
  sourceTable: string,
  sourceQuery: string,
  options: any,
  path: string,
  serializer: string,
  saveMode: string,
  partitionBy: Array<string>,
  numPartitions: number,
}

const rules = {
  path: [{required: true}],
  serializer: [{required: true}],
  saveMode: [{required: true}],
  numPartitions: [{required: true}],
  sourceTable: [{required: true}],
  sourceQuery: [{required: true}],
}

const {value, name} = defineProps<{
  value: FileSinkValue,
  name: String,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

const { confOptions: fileSerializers } = useSparkFileSerializersStore()
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
      <a-form-item name="path" label="写入路径" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input v-model:value="value.path" />
      </a-form-item>
      <a-form-item name="serializer" label="文件格式" class="form-item-320">
        <a-select v-model:value="value.serializer" :options="fileSerializers" placeholder="请选择" />
      </a-form-item>
      <a-form-item name="saveMode" label="写入模式" class="form-item-320">
        <a-radio-group v-model:value="value.saveMode">
          <a-radio-button value="append">追加</a-radio-button>
          <a-radio-button value="overwrite">覆盖</a-radio-button>
        </a-radio-group>
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
      <a-form-item name="partitionBy" label="分区字段" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input-string-array v-model:value="value.partitionBy" />
      </a-form-item>
      <a-form-item name="options" label="参数" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input-string-map v-model:value="value.options" />
      </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>