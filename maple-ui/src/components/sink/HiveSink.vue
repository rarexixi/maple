<script lang="ts">
import { defineComponent, reactive } from "vue"
import type { PropType } from "vue"
import FormLabelPopover from "@/components/FormLabelPopover.vue"
import InputStringMap from "@/components/InputStringMap.vue"
import SampleData from "@/assets/sample-data"

interface HiveSinkValue {
  sourceTable: String,
  sourceQuery: String,
  targetDatabase: String,
  targetTable: String,
  variables: Map<string, string>,
  saveMode: String,
  strongCheck: Boolean,
  writeAsFile: Boolean,
  numPartitions: Number,
  options: Map<string, string>,
}

export default defineComponent({
  components: { InputStringMap, FormLabelPopover },
  props: {
    value: { type: Object as PropType<HiveSinkValue>, isRequired: true },
  },
  setup() {
    const validateMessages = {
      required: '请输入/选择${label}!',
      number: {
        range: '${label}必须在${min}和${max}之间',
      },
    }

    return {
      validateMessages,
      databases: SampleData.Databases,
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
        <a-form-item name="targetDatabase" label="目标库" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-select v-model:value="value.targetDatabase" placeholder="请选择数据库">
            <template v-for="db in databases" :key="db.databaseName">
              <a-select-option :value="db.databaseName">{{ db.databaseName }}</a-select-option>
            </template>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="targetTable" label="目标表" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input v-model:value="value.targetTable" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="saveMode" label="写入模式" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-radio-group v-model:value="value.saveMode">
            <a-radio-button value="append">追加</a-radio-button>
            <a-radio-button value="overwrite">覆盖</a-radio-button>
          </a-radio-group>
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="writeAsFile" label="文件优先" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-checkbox v-model:checked="value.writeAsFile" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="strongCheck" label="强校验" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-checkbox v-model:checked="value.strongCheck" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="numPartitions" label="分区数" :rules="[{ type: 'number', min: 0, max: 99 }]" :label-col="layout.labelCols.small">
          <a-input-number v-model:value="value.numPartitions" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="path" label="来源表" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input v-model:value="value.sourceTable" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-form-item name="sourceQuery" label="来源语句" :rules="[{ required: true }]" :label-col="layout.labelCols.large">
      <a-textarea v-model:value="value.sourceQuery" :auto-size="{ minRows: 2, maxRows: 20 }" />
    </a-form-item>
    <a-form-item name="options" label="参数" :label-col="layout.labelCols.large">
      <input-string-map v-model:value="value.options" />
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>