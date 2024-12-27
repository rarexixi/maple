<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { onMounted, useTemplateRef } from "vue"

import common from "@/composables/common"
import type { StarRocksSinkConfig } from "@/composables/flink-jobs"
import type { validateFunction } from "@/composables/models"

import SinkJobSourceForms from "@/components/job/SinkJobSourceForms.vue"
import FlinkRdbmsTableFormItems from "@/components/job/flink/data-calc/FlinkRdbmsTableFormItems.vue"

const rules = {
  datasourceId: [{ required: true }],
  saveMode: [{required: true}],
  numPartitions: [{type: 'number', min: 0, max: 99}],
}

const {value, name} = defineProps<{
  value: StarRocksSinkConfig,
  name: string,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

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
          :label-col="labelCols.l125">
    <a-flex wrap="wrap">
      <FlinkRdbmsTableFormItems
          v-model:result-table="value.resultTable"
          v-model:comment="value.comment"
          v-model:datasource-id="value.datasourceId"
          v-model:rdbmsTable="value.rdbmsTable"
          v-model:physical-columns="value.physicalColumns"
          v-model:metadata-columns="value.metadataColumns"
          v-model:computed-columns="value.computedColumns"
          v-model:pk-columns="value.pkColumns"
          v-model:wm-column="value.wmColumn"
          v-model:wm-delay-seconds="value.wmDelaySeconds"
          v-model:partition-columns="value.partitionColumns"
          v-model:options="value.options"
          :datasource-types="['starrocks']">
      </FlinkRdbmsTableFormItems>
      <SinkJobSourceForms v-model:sourceTable="value.sourceTable" v-model:sourceQuery="value.sourceQuery" />
    </a-flex>
  </a-form>
</template>

<style scoped></style>