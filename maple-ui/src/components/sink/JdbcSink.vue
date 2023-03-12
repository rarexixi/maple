<script lang="ts">
import { defineComponent } from "vue"
import type { PropType } from "vue"
import FormLabelPopover from "@/components/FormLabelPopover.vue"
import InputStringArray from "@/components/InputStringArray.vue"
import InputStringMap from "@/components/InputStringMap.vue"
import SampleData from "@/assets/sample-data"

interface FileSourceValue {
  variables: Map<string, string>,
  sourceTable: string,
  sourceQuery: string,
  options: any,
  url: string,
  driver: string,
  user: string,
  password: string,
  targetDatabase: string,
  targetTable: string,
  saveMode: string,
  preQueries: Array<string>,
  numPartitions: number,
}

export default defineComponent({
  components: { InputStringMap, InputStringArray, FormLabelPopover },
  props: {
    value: { type: Object as PropType<FileSourceValue>, isRequired: true },
    name: String,
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
      layout: SampleData.Layout,
    }
  },
})
</script>

<template>
  <a-form :name="name" :model="value" :validate-messages="validateMessages">
    <a-form-item name="variables" label="变量" :label-col="layout.labelCols.large">
      <input-string-map v-model:value="value!.variables" />
    </a-form-item>
    <a-form-item name="url" label="jdbc url" :rules="[{ required: true }]" :label-col="layout.labelCols.large">
      <a-input v-model:value="value!.url" />
    </a-form-item>
    <a-row>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="driver" label="驱动类名" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input v-model:value="value!.driver" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="user" label="用户名" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input v-model:value="value!.user" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="password" label="密码" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input-password v-model:value="value!.password" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="targetDatabase" label="目标库" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input v-model:value="value!.targetDatabase" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="targetTable" label="目标表" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input v-model:value="value!.targetTable" />
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
        <a-form-item name="numPartitions" label="分区数" :rules="[{ type: 'number', min: 0, max: 99 }]"
          :label-col="layout.labelCols.small">
          <a-input-number v-model:value="value!.numPartitions" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="sourceTable" label="来源表" :label-col="layout.labelCols.small">
          <a-input v-model:value="value!.sourceTable" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-form-item name="sourceQuery" label="来源语句" :label-col="layout.labelCols.large">
      <a-textarea v-model:value="value!.sourceQuery" :auto-size="{ minRows: 2, maxRows: 20 }" />
    </a-form-item>
    <a-form-item v-for="(item, index) in value!.preQueries" :key="index" :label-col="layout.labelCols.large"
      :wrapper-col="index === 0 ? {} : layout.wrapperColsWithLabel.large" :label="index === 0 ? '预执行SQL' : ''"
      :name="['preQueries', index]">
      <a-textarea v-model:value="value!.preQueries[index]" placeholder="预先要执行的SQL语句，一般为delete或者truncate语句"
        style="width: calc(100% - 28px); margin-right: 8px" />
      <MinusCircleOutlined @click="() => value!.preQueries.splice(index, 1)" />
    </a-form-item>
    <a-form-item :label-col="layout.labelCols.large" :wrapper-col="layout.wrapperColsWithLabel.large">
      <a-button type="dashed" @click="() => value!.preQueries.push('')">
        <PlusOutlined />
        添加预执行SQL
      </a-button>
    </a-form-item>
    <a-form-item name="options" label="参数" :label-col="layout.labelCols.large">
      <input-string-map v-model:value="value!.options" />
    </a-form-item>
  </a-form>
</template>

<style scoped></style>