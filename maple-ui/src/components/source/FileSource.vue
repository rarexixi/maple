<script lang="ts">
import { defineComponent } from "vue"
import type { PropType } from "vue"
import FormLabelPopover from "@/components/FormLabelPopover.vue"
import InputStringArray from "@/components/InputStringArray.vue"
import InputStringMap from "@/components/InputStringMap.vue"
import SampleData from "@/assets/sample-data"

interface FileSourceValue {
  variables: any,
  resultTable: string,
  persist: boolean,
  storageLevel: string,
  options: any,
  path: string,
  serializer: string,
  columnNames: string[],
}

export default defineComponent({
  components: { InputStringMap, InputStringArray, FormLabelPopover },
  props: {
    value: { type: Object as PropType<FileSourceValue>, isRequired: true },
  },
  emits: ['update:value'],
  setup() {
    const validateMessages = {
      required: '请输入/选择${label}!',
      number: {
        range: '${label}必须在${min}和${max}之间',
      },
    }

    return {
      validateMessages,
      fileSerializers: SampleData.FileSerializers,
      storageLevels: SampleData.StorageLevels,
      layout: SampleData.Layout,
    }
  },
})
</script>

<template>
  <a-form :model="value" :validate-messages="validateMessages">
    <a-form-item name="variables" label="变量" :label-col="layout.labelCols.large">
      <input-string-map v-model:value="value.variables" />
    </a-form-item>
    <a-row>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="resultTable" label="注册表名" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input v-model:value="value.resultTable" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="persist" label="开启缓存" :label-col="layout.labelCols.small">
          <a-switch v-model:checked="value.persist" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small" v-show="value.persist">
        <a-form-item name="storageLevel" label="缓存级别" :label-col="layout.labelCols.small">
          <a-select v-model:value="value.storageLevel" :options="storageLevels" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-form-item name="path" label="文件路径" :rules="[{ required: true }]" :label-col="layout.labelCols.large">
      <a-input v-model:value="value.path" />
    </a-form-item>
    <a-row>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="serializer" label="文件格式" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-select v-model:value="value.serializer" placeholder="请选择文件格式">
            <template v-for="serializer in fileSerializers" :key="serializer">
              <a-select-option :value="serializer">{{ serializer }}</a-select-option>
            </template>
          </a-select>
        </a-form-item>
      </a-col>
    </a-row>
    <a-form-item name="columnNames" label="字段名" :label-col="layout.labelCols.large">
      <input-string-array v-model:value="value.columnNames" />
    </a-form-item>
    <a-form-item name="options" label="参数" :label-col="layout.labelCols.large">
      <input-string-map v-model:value="value.options" />
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>