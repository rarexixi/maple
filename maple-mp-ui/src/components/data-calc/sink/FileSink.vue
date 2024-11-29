<script setup lang="ts">
import InputStringMap from "@/components/data-calc/InputStringMap.vue"
import InputStringArray from "@/components/data-calc/InputStringArray.vue";
import SampleData from "@/assets/sample-data"

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

const fileSerializers = SampleData.FileSerializers
const layout = SampleData.Layout
</script>

<template>
  <a-form :name="name" :model="value" :validate-messages="validateMessages">
    <a-form-item name="path" label="写入路径" :rules="[{ required: true }]" :label-col="layout.labelCols.large">
      <a-input v-model:value="value!.path" />
    </a-form-item>
    <a-row>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="serializer" label="文件格式" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-select v-model:value="value!.serializer" placeholder="请选择文件格式">
            <template v-for="serializer in fileSerializers" :key="serializer">
              <a-select-option :value="serializer">{{ serializer }}</a-select-option>
            </template>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="saveMode" label="写入模式" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-radio-group v-model:value="value!.saveMode">
            <a-radio-button value="append">追加</a-radio-button>
            <a-radio-button value="overwrite">覆盖</a-radio-button>
          </a-radio-group>
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="numPartitions" label="分区数" :rules="[{ type: 'number', min: 0, max: 99 }]" :label-col="layout.labelCols.small">
          <a-input-number v-model:value="value!.numPartitions" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="sourceTable" label="来源表" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input v-model:value="value!.sourceTable" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-form-item name="sourceQuery" label="来源语句" :rules="[{ required: true }]" :label-col="layout.labelCols.large">
      <a-textarea v-model:value="value!.sourceQuery" :auto-size="{ minRows: 2, maxRows: 20 }" />
    </a-form-item>
    <a-form-item name="partitionBy" label="分区字段" :label-col="layout.labelCols.large">
      <input-string-array v-model:value="value!.partitionBy" />
    </a-form-item>
    <a-form-item name="options" label="参数" :label-col="layout.labelCols.large">
      <input-string-map v-model:value="value!.options" />
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>