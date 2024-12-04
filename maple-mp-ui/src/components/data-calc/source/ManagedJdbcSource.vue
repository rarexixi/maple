<script setup lang="ts">
import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"
import SampleData from "@/assets/sample-data"

interface ManagedJdbcSourceValue {
  resultTable: string,
  persist: boolean,
  storageLevel: string,
  options: any,
  datasource: string,
  query: string,
}

const {value, name} = defineProps<{
  value: ManagedJdbcSourceValue,
  name: string,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

const datasourceList = SampleData.DatasourceList
const storageLevels = SampleData.StorageLevels
const layout = SampleData.Layout
</script>

<template>
  <a-form :name="name" :model="value" :validate-messages="validateMessages">
    <a-row>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="resultTable" label="注册表名" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input v-model:value="value!.resultTable" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="persist" label="开启缓存" :label-col="layout.labelCols.small">
          <a-switch v-model:checked="value!.persist" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small" v-show="value!.persist">
        <a-form-item name="storageLevel" label="缓存级别" :label-col="layout.labelCols.small">
          <a-select v-model:value="value!.storageLevel" :options="storageLevels"></a-select>
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="datasource" label="数据源" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-select v-model:value="value!.datasource" placeholder="请选择数据源">
            <template v-for="ds in datasourceList" :key="ds.name">
              <a-select-option :value="ds.name">{{ ds.name }}</a-select-option>
            </template>
          </a-select>
        </a-form-item>
      </a-col>
    </a-row>
    <a-form-item name="query" label="查询语句" :label-col="layout.labelCols.large">
      <a-textarea v-model:value="value!.query" :auto-size="{ minRows: 2, maxRows: 20 }" />
    </a-form-item>
    <a-form-item name="options" label="参数" :label-col="layout.labelCols.large">
      <a-input-string-map v-model:value="value!.options" />
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>