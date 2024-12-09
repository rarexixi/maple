<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { onMounted, useTemplateRef } from "vue"

import type { validateFunction } from "@/composables/models"
import common from "@/composables/common"

import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"

import SampleData from "@/assets/sample-data"

interface StarRocksSourceValue {
  resultTable: string,
  persist: boolean,
  storageLevel: string,
  options: any,
  feHttpUrl: string,
  feJdbcUrl: string,
  user: string,
  password: string,
  database: string,
  table: string
}

const rules = {
  resultTable: [{required: true}],
  feHttpUrl: [{required: true}],
  feJdbcUrl: [{required: true}],
  user: [{required: true}],
  password: [{required: true}],
  database: [{required: true}],
  table: [{required: true}],
}

const {value, name} = defineProps<{
  value: StarRocksSourceValue,
  name: string,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

const storageLevels = SampleData.StorageLevels
const labelCols = SampleData.Layout.labelCols

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
        <a-input v-model:value="value!.resultTable" />
      </a-form-item>
      <a-form-item name="persist" label="开启缓存" class="form-item-320">
        <a-switch v-model:checked="value!.persist" />
      </a-form-item>
      <a-form-item name="storageLevel" label="缓存级别" class="form-item-320">
        <a-select v-model:value="value!.storageLevel" :options="storageLevels" :disabled="!value!.persist" />
      </a-form-item>
      <a-form-item name="feHttpUrl" label="feHttpUrl" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input v-model:value="value!.feHttpUrl" />
      </a-form-item>
      <a-form-item name="feJdbcUrl" label="feJdbcUrl" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input v-model:value="value!.feJdbcUrl" />
      </a-form-item>
      <a-form-item name="user" label="用户名" class="form-item-320">
        <a-input v-model:value="value!.user" />
      </a-form-item>
      <a-form-item name="password" label="密码" class="form-item-320">
        <a-input-password v-model:value="value!.password" />
      </a-form-item>
      <a-form-item name="database" label="库名" class="form-item-320">
        <a-input v-model:value="value!.database" />
      </a-form-item>
      <a-form-item name="table" label="表名" class="form-item-320">
        <a-input v-model:value="value!.table" />
      </a-form-item>
      <a-form-item name="options" label="参数" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input-string-map v-model:value="value!.options" />
      </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>