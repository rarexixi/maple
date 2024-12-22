<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { computed, onMounted, useTemplateRef } from "vue"

import common from "@/composables/common"
import { getDatasourceOptions } from "@/composables/datasources"
import type { UpsertKafkaSourceConfig } from "@/composables/flink-jobs"
import type { validateFunction } from "@/composables/models"

import { useDatasourceStore } from "@/stores/sys-data"

import ParamsMap from "@/components/ParamsMap.vue"

const rules = {
  resultTable: [{required: true}],
  datasourceId: [{ required: true }],
  topic: [{required: true}],
  groupId: [{required: true}],
  format: [{required: true}],
  scanStartupMode: '"earliest-offset|latest-offset|group-offset|timestamp|specific-offset"'
}

const {value, name} = defineProps<{
  value: UpsertKafkaSourceConfig,
  name: string,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

const labelCols = common.Layout.labelCols

const { dataList: datasourceList } = useDatasourceStore()
const datasourceOptions = computed(() => getDatasourceOptions(datasourceList.value, 'kafka'))

const formRef = useTemplateRef<FormInstance>("formRef")
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
      <a-flex-br />
      <a-form-item name="datasourceId" label="数据源" class="form-item-320">
        <a-select v-model:value="value.datasourceId" :options="datasourceOptions" placeholder="请选择" />
      </a-form-item>
      <a-flex-br />
      <a-form-item :name="['watermark', 'columnName']" label="watermark" class="form-item-320">
        <a-select v-model:value="value.watermark.columnName" />
      </a-form-item>
      <a-form-item :name="['watermark', 'delaySeconds']" class="form-item-320">
        <a-input-number v-model:value="value.watermark.delaySeconds" style="width: 50%" addon-before="延迟"
                        addon-after="秒" />
      </a-form-item>
      <a-form-item name="topic" label="topic" class="form-item-320">
        <a-input v-model:value="value.topic" />
      </a-form-item>
      <a-form-item name="groupId" label="group id" class="form-item-320">
        <a-input v-model:value="value.groupId" />
      </a-form-item>
      <a-form-item name="format" label="format" class="form-item-320">
        <a-input v-model:value="value.format" />
      </a-form-item>
      <a-form-item name="scanStartupMode" label="启动模式" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input-string-map v-model:value="value.scanStartupMode" />
      </a-form-item>
      <a-form-item name="options" label="参数" :label-col="labelCols.w1280" class="form-item-1280">
        <params-map v-model:value="value.options" />
      </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>