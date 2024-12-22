<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { computed, onMounted, useTemplateRef } from "vue"

import common from "@/composables/common"
import { getDatasourceOptions } from "@/composables/datasources"
import type { validateFunction } from "@/composables/models"
import type { StarRocksSinkConfig } from "@/composables/spark-jobs"

import { useDatasourceStore } from "@/stores/sys-data"

import ParamsMap from "@/components/ParamsMap.vue"
import TableSelectFormItems from "@/components/datasource/TableSelectFormItems.vue"

const rules = {
  targetDatasource: [{required: true}],
  saveMode: [{required: true}],
  numPartitions: [{type: 'number', min: 0}],
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
const wrapCols = common.Layout.wrapCols

const { dataList: datasourceList } = useDatasourceStore()
const datasourceOptions = computed(() => getDatasourceOptions(datasourceList.value, 'starrocks'))

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
      <a-form-item name="targetDatasource" label="目标数据源" class="form-item-320">
        <a-select v-model:value="value.targetDatasource" :options="datasourceOptions" placeholder="请选择" />
      </a-form-item>
      <TableSelectFormItems v-model:database-name="value.targetTable.databaseName"
                            v-model:schema-name="value.targetTable.schemaName"
                            v-model:table-name="value.targetTable.tableName"
                            :datasourceId="value.targetDatasource" :validated-name-prefix="['targetTable']" />
      <a-flex-br />
      <a-form-item name="saveMode" label="写入模式" class="form-item-320">
        <a-radio-group v-model:value="value.saveMode">
          <a-radio-button value="append">追加</a-radio-button>
          <a-radio-button value="overwrite">覆盖</a-radio-button>
        </a-radio-group>
      </a-form-item>
      <a-form-item name="numPartitions" label="分区数" class="form-item-320">
        <a-input-number v-model:value="value.numPartitions" />
      </a-form-item>
      <a-form-item name="sourceTable" label="来源表" class="form-item-320">
        <a-input v-model:value="value.sourceTable" />
      </a-form-item>
      <a-form-item name="sourceQuery" label="来源语句" :label-col="labelCols.w1280" class="form-item-1280">
        <a-textarea v-model:value="value.sourceQuery" :auto-size="{ minRows: 2, maxRows: 20 }" />
      </a-form-item>
      <a-form-item name="options" label="参数" :label-col="labelCols.w1280" class="form-item-1280">
        <params-map v-model:value="value.options" />
      </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped></style>