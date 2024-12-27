<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { onMounted, useTemplateRef } from "vue"

import common from "@/composables/common"
import type { validateFunction } from "@/composables/models"
import type { FileSinkConfig } from "@/composables/spark-jobs"

import { useSparkFileSerializersStore } from "@/stores/sys-conf"

import AInputStringArray from "@/components/ant-ext/AInputStringArray.vue"
import ConfOptionsForm from "@/components/job/ConfOptionsForm.vue"
import SinkJobSourceForms from "@/components/job/SinkJobSourceForms.vue"

const rules = {
  path: [{required: true}],
  serializer: [{required: true}],
  saveMode: [{required: true}],
  numPartitions: [{required: true}],
  sourceTable: [{required: true}],
  sourceQuery: [{required: true}],
}

const {value, name} = defineProps<{
  value: FileSinkConfig,
  name: String,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

const labelCols = common.Layout.labelCols

const { confOptions: fileSerializers } = useSparkFileSerializersStore()

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
          :label-col="labelCols.l125">
    <a-flex wrap="wrap">
      <a-form-item name="path" label="写入路径" :label-col="labelCols.l125" class="form-item-960">
        <a-input v-model:value="value.path" />
      </a-form-item>
      <a-form-item name="serializer" label="文件格式" class="form-item-360">
        <a-select v-model:value="value.serializer" :options="fileSerializers" placeholder="请选择" />
      </a-form-item>
      <a-form-item name="saveMode" label="写入模式" class="form-item-360">
        <a-radio-group v-model:value="value.saveMode">
          <a-radio-button value="append">追加</a-radio-button>
          <a-radio-button value="overwrite">覆盖</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item name="numPartitions" label="分区数" class="form-item-360">
        <a-input-number v-model:value="value.numPartitions" />
      </a-form-item>
      <a-form-item name="partitionBy" label="分区字段" :label-col="labelCols.l125" class="form-item-960">
        <a-input-string-array v-model:value="value.partitionBy" />
      </a-form-item>
      <a-flex-br />
      <SinkJobSourceForms v-model:sourceTable="value.sourceTable" v-model:sourceQuery="value.sourceQuery" />
      <a-flex-br />
      <ConfOptionsForm name="options" v-model:value="value.options" />
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>