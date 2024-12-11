<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { onMounted, useTemplateRef } from "vue"

import type { validateFunction } from "@/composables/models"
import common from "@/composables/common"

import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"

import { useSparkStorageLevelsStore } from "@/stores/sys-conf"

interface JdbcSourceValue {
  resultTable: string,
  persist: boolean,
  storageLevel: string,
  options: any,
  url: string,
  driver: string,
  user: string,
  password: string,
  query: string,
  table: string,
}

const rules = {
  resultTable: [{required: true}],
  driver: [{required: true}],
  user: [{required: true}],
  password: [{required: true}],
  url: [{required: true}],
}

const {value, name} = defineProps<{
  value: JdbcSourceValue,
  name: string,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

const { confOptions: storageLevels } = useSparkStorageLevelsStore()
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
      <a-form-item name="resultTable" label="注册表名" :label-col="labelCols.w320"
                   class="form-item-320">
        <a-input v-model:value="value.resultTable" />
      </a-form-item>
      <a-form-item name="persist" label="开启缓存" class="form-item-320">
        <a-switch v-model:checked="value.persist" />
      </a-form-item>
      <a-form-item name="storageLevel" label="缓存级别" class="form-item-320">
        <a-select v-model:value="value.storageLevel" :options="storageLevels" :disabled="!value.persist" />
      </a-form-item>
      <a-form-item name="driver" label="驱动类名" :label-col="labelCols.w320"
                   class="form-item-320">
        <a-input v-model:value="value.driver" />
      </a-form-item>
      <a-form-item name="user" label="用户名" :label-col="labelCols.w320"
                   class="form-item-320">
        <a-input v-model:value="value.user" />
      </a-form-item>
      <a-form-item name="password" label="密码" :label-col="labelCols.w320"
                   class="form-item-320">
        <a-input-password v-model:value="value.password" />
      </a-form-item>
      <a-form-item name="url" label="jdbc url" :label-col="labelCols.w1280"
                   class="form-item-1280">
        <a-input v-model:value="value.url" />
      </a-form-item>
      <a-form-item name="table" label="来源表" :label-col="labelCols.w320" class="form-item-320">
        <a-input v-model:value="value.table" />
      </a-form-item>
      <a-form-item name="query" label="查询语句" :label-col="labelCols.w1280" class="form-item-1280">
        <a-textarea v-model:value="value.query" :auto-size="{ minRows: 2, maxRows: 20 }" />
      </a-form-item>
      <a-form-item name="options" label="参数" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input-string-map v-model:value="value.options" />
      </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>