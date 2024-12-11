<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { onMounted, useTemplateRef } from "vue"

import type { validateFunction } from "@/composables/models"
import common from "@/composables/common"

import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"
import AInputStringArray from "@/components/ant-ext/AInputStringArray.vue"

import { useSparkFileSerializersStore, useSparkStorageLevelsStore } from "@/stores/sys-conf"

interface FileSourceValue {
  resultTable: string,
  persist: boolean,
  storageLevel: string,
  options: any,
  path: string,
  serializer: string,
  columnNames: string[],
}

const rules = {
  resultTable: [{required: true}],
  path: [{required: true}],
  serializer: [{required: true}],
}

const {value, name} = defineProps<{
  value: FileSourceValue,
  name: string,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

const { confOptions: fileSerializers } = useSparkFileSerializersStore()
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
      <a-form-item name="resultTable" label="注册表名" class="form-item-320">
        <a-input v-model:value="value.resultTable" />
      </a-form-item>
      <a-form-item name="persist" label="开启缓存" class="form-item-320">
        <a-switch v-model:checked="value.persist" />
      </a-form-item>
      <a-form-item name="storageLevel" label="缓存级别" class="form-item-320">
        <a-select v-model:value="value.storageLevel" :options="storageLevels" :disabled="!value.persist" />
      </a-form-item>
      <a-form-item name="path" label="文件路径" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input v-model:value="value.path" />
      </a-form-item>
      <a-form-item name="serializer" label="文件格式" class="form-item-320">
        <a-select v-model:value="value.serializer" :options="fileSerializers" placeholder="请选择"/>
      </a-form-item>
      <a-form-item name="columnNames" label="字段名" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input-string-array v-model:value="value.columnNames" />
      </a-form-item>
      <a-form-item name="options" label="参数" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input-string-map v-model:value="value.options" />
      </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>