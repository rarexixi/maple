<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { computed, onMounted, useTemplateRef } from "vue"

import common from "@/composables/common"
import { getDatasourceOptions } from "@/composables/datasources"
import type { validateFunction } from "@/composables/models"
import type { StarRocksSourceConfig } from "@/composables/spark-jobs"

import { useSparkStorageLevelsStore } from "@/stores/sys-conf"
import { useDatasourceStore } from "@/stores/sys-data"

import ParamsMap from "@/components/ParamsMap.vue"
import TableSelectFormItems from "@/components/datasource/TableSelectFormItems.vue"

const rules = {
  resultTable: [{required: true}],
  datasource: [{required: true}],
}

const {value, name} = defineProps<{
  value: StarRocksSourceConfig,
  name: string,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

const labelCols = common.Layout.labelCols

const { confOptions: storageLevels } = useSparkStorageLevelsStore()
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
      <a-form-item name="resultTable" label="注册表名" class="form-item-320">
        <a-input v-model:value="value.resultTable" />
      </a-form-item>
      <a-form-item name="persist" label="开启缓存" class="form-item-320">
        <a-switch v-model:checked="value.persist" />
      </a-form-item>
      <a-form-item name="storageLevel" label="缓存级别" class="form-item-320">
        <a-select v-model:value="value.storageLevel" :options="storageLevels" :disabled="!value.persist" />
      </a-form-item>
      <a-flex-br/>
      <a-form-item name="datasource" label="数据源" class="form-item-320">
        <a-select v-model:value="value.datasource" :options="datasourceOptions" placeholder="请选择" />
      </a-form-item>
      <TableSelectFormItems v-model:database-name="value.sourceTable.databaseName"
                            v-model:schema-name="value.sourceTable.schemaName"
                            v-model:table-name="value.sourceTable.tableName"
                            :datasourceId="value.datasource" :validated-name-prefix="['sourceTable']" />
      <a-flex-br />
      <a-form-item name="options" label="参数" :label-col="labelCols.w1280" class="form-item-1280">
        <params-map v-model:value="value.options" />
      </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>