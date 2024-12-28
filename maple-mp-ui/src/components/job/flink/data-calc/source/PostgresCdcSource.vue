<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { onMounted, useTemplateRef } from "vue"

import common from "@/composables/common"
import type { PostgresCdcSourceConfig } from "@/composables/flink-jobs"
import type { validateFunction } from "@/composables/models"


import FlinkRdbmsTableFormItems from "@/components/job/flink/data-calc/FlinkRdbmsTableFormItems.vue"

const rules = {
  resultTable: [{required: true}],
  datasourceId: [{ required: true }],
}

const {value, name} = defineProps<{
  value: PostgresCdcSourceConfig,
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
          :operation-type="'source'"
          :metadata-type="'postgres-cdc'"
          :datasource-types="['postgres']">
        <template #definedOptions>
          <a-form-item name="scanStartupMode" label="启动模式" class="form-item-360">
            <a-input v-model:value="value.scanStartupMode" />
          </a-form-item>
        </template>
      </FlinkRdbmsTableFormItems>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>