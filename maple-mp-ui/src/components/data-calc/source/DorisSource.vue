<script setup lang="ts">
import InputStringMap from "@/components/data-calc/InputStringMap.vue"
import SampleData from "@/assets/sample-data"

interface DorisSourceValue {
  resultTable: string,
  persist: boolean,
  storageLevel: string,
  options: any,
  fenodes: string,
  user: string,
  password: string,
  database: string,
  table: string,
}

const {value, name} = defineProps<{
  value: DorisSourceValue,
  name: string,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

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
    </a-row>
    <a-form-item name="fenodes" label="fenodes" :rules="[{ required: true }]" :label-col="layout.labelCols.large">
      <a-input v-model:value="value!.fenodes" />
    </a-form-item>
    <a-row>
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
        <a-form-item name="database" label="库名" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input v-model:value="value!.database" />
        </a-form-item>
      </a-col>
      <a-col v-bind="layout.cols.small">
        <a-form-item name="table" label="表名" :rules="[{ required: true }]" :label-col="layout.labelCols.small">
          <a-input v-model:value="value!.table" />
        </a-form-item>
      </a-col>
    </a-row>
    <a-form-item name="options" label="参数" :label-col="layout.labelCols.large">
      <input-string-map v-model:value="value!.options" />
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>