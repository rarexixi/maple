<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { onMounted, useTemplateRef } from "vue"

import type { validateFunction } from "@/composables/models"
import common from "@/composables/common"

import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"
import { useDatasourceStore } from "@/stores/sys-data";

interface ManagedJdbcSinkValue {
  sourceTable: string,
  sourceQuery: string,
  options: any,
  targetDatasource: string,
  targetDatabase: string,
  targetTable: string,
  saveMode: string,
  preQueries: Array<string>,
  numPartitions: number,
}

const rules = {
  targetDatasource: [{required: true}],
  targetDatabase: [{required: true}],
  targetTable: [{required: true}],
  saveMode: [{required: true}],
  numPartitions: [{type: 'number', min: 0, max: 99}],
}

const {value, name} = defineProps<{
  value: ManagedJdbcSinkValue,
  name: string,
}>()

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

const { dataList: datasourceList } = useDatasourceStore()
const labelCols = common.Layout.labelCols
const wrapCols = common.Layout.wrapCols

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
      <a-form-item name="targetDatasource" label="数据源" class="form-item-320">
        <a-select v-model:value="value.targetDatasource" placeholder="请选择">
          <template v-for="ds in datasourceList" :key="ds.name">
            <a-select-option :value="ds.name">{{ ds.name }}</a-select-option>
          </template>
        </a-select>
      </a-form-item>
      <a-form-item name="targetDatabase" label="数据库" class="form-item-320">
        <a-input v-model:value="value.targetDatabase" />
      </a-form-item>
      <a-form-item name="targetTable" label="表" class="form-item-320">
        <a-input v-model:value="value.targetTable" />
      </a-form-item>
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
      <a-form-item v-for="(item, index) in value.preQueries" :name="['preQueries', index]" :key="index"
                   :label-col="labelCols.w1280" :wrapper-col="index === 0 ? {} : wrapCols.w1280"
                   :label="index === 0 ? '预执行SQL' : ''" class="form-item-1280">
        <a-textarea v-model:value="value.preQueries[index]"
                    placeholder="预先要执行的SQL语句，一般为delete或者truncate语句"
                    style="width: calc(100% - 28px); margin-right: 8px" />
        <MinusCircleOutlined @click="() => value.preQueries.splice(index, 1)" />
      </a-form-item>
      <a-form-item :wrapper-col="wrapCols.w1280" class="form-item-1280">
        <a-button type="dashed" @click="() => value.preQueries.push('')">
          <PlusOutlined />
          添加预执行SQL
        </a-button>
      </a-form-item>
      <a-form-item name="options" label="参数" :label-col="labelCols.w1280" class="form-item-1280">
        <a-input-string-map v-model:value="value.options" />
      </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped></style>