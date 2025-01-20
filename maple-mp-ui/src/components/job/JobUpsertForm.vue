<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { minimatch } from "minimatch"
import { computed, useTemplateRef } from "vue"

import common from "@/composables/common"
import type { ValidatableComponent } from "@/composables/models"

import { useDatabaseTypesStore, useFlinkConnectorAvailableMetadataStore, useJobTypesStore } from "@/stores/sys-conf"
import { useClusterEngineStore, useClusterStore, useDatasourceStore } from "@/stores/sys-data"

import SparkRun from "@/components/job/spark/SparkRunForm.vue"
import FlinkRun from "@/components/job/flink/FlinkRunForm.vue"
import SparkDataCalc from "@/components/job/spark/data-calc/SparkDataCalcForm.vue"
import FlinkDataCalc from "@/components/job/flink/data-calc/FlinkDataCalcForm.vue"

const detail = defineModel<any>()

const rules = {
  id: [
    { type: 'integer', required: true, message: '作业ID不能为空', trigger: 'blur' }
  ],
  jobName: [
    { required: true, message: '作业名不能为空', trigger: 'blur' }
  ],
  jobType: [
    { required: true, message: '作业类型不能为空', trigger: 'blur' }
  ],
  engineId: [
    { type: 'integer', required: true, message: '引擎ID不能为空', trigger: 'change' }
  ],
}

const formRef = useTemplateRef<FormInstance>("formRef")
const runFormRef = useTemplateRef<ValidatableComponent>("runFormRef")
const jobConfFormRef = useTemplateRef<ValidatableComponent>("jobConfFormRef")

const { dataInitialized } = useDatasourceStore()
const { confInitialized } = useDatabaseTypesStore()
const { confInitialized: metadataColumnsConfInitialized } = useFlinkConnectorAvailableMetadataStore()
const { confMap: jobTypeMap } = useJobTypesStore()

const { dataList: clusterList, dataMap: clusterMap } = useClusterStore()
const { dataList: engineList, dataMap: engineMap } = useClusterEngineStore()

function versionMatch(patterns: string[], version: string) {
  for (let pattern of patterns) {
    if (minimatch(version, pattern)) {
      return true
    }
  }
  return false
}

const jobTypeDetail = computed(() => jobTypeMap[detail.value.jobType])
const engineOptions = computed(() => {
  let result = new Map<string, any>()

  if (!jobTypeDetail.value)
    return []

  for (let engine of engineList.value) {
    if (jobTypeDetail.value.engineType !== engine.name || !versionMatch(jobTypeDetail.value.engineVersions, engine.version)) {
      continue
    }
    if (result.has(engine.clusterId)) {
      result.get(engine.clusterId).push({ label: `${engine.name} ${engine.version}`, value: engine.id })
    } else {
      result.set(engine.clusterId, [{ label: `${engine.name} ${engine.version}`, value: engine.id }])
    }
  }
  return Array.from(result).map(([clusterId, engines]) => ({ label: clusterMap[clusterId]?.name, options: engines }))
})
const clusterCategory = computed(() => {
  let clusterId = engineMap[detail.value.engineId]?.clusterId
  if (!clusterId) return ""
  return clusterMap[clusterId]?.category
})

const emit = defineEmits<{
  (e: 'save'): void
}>()

async function save() {
  let validated = await common.getFormValidated(runFormRef, jobConfFormRef)
  if (!validated) {
    formRef.value?.validate()
    common.notifyValidateError()
    return
  }
  formRef.value?.validate().then(() => {
    emit("save")
  }).catch((error: ValidateErrorEntity<any>) => {
    common.notifyValidateError()
  })
}

const priorityOptions = [{ label: '低', value: 1 }, { label: '中低', value: 2 }, {  label: '中',  value: 3}, { label: '中高', value: 4 }, { label: '高', value: 5 }]

const labelCols = common.Layout.labelCols
const wrapCols = common.Layout.wrapCols

</script>

<template>
  <a-form ref="formRef" :model="detail" :rules="rules" :label-col="labelCols.l125">
    <a-flex wrap="wrap">
      <a-typography-title :level="5">基础信息</a-typography-title>
      <a-flex-br />
      <a-form-item label="作业名" name="jobName" class="form-item-360">
        <a-input v-model:value.trim="detail.jobName" type="text" />
      </a-form-item>
      <a-form-item label="作业说明" name="description" class="form-item-720">
        <a-input v-model:value="detail.description" type="text" />
      </a-form-item>
      <a-form-item label="引擎ID" name="engineId" class="form-item-360">
        <a-select v-model:value="detail.engineId" :options="engineOptions" allow-clear placeholder="请选择" />
      </a-form-item>
      <a-form-item label="作业优先级" name="priority" class="form-item-360">
        <a-select v-model:value="detail.priority" :options="priorityOptions" allow-clear placeholder="请选择" />
      </a-form-item>
    </a-flex>
    <SparkRun ref="runFormRef" :job-type="detail.jobType" :cluster-category="clusterCategory" :run-conf="detail.runConf"
              v-if="jobTypeDetail?.engineType == 'spark'" />
    <FlinkRun ref="runFormRef" :job-type="detail.jobType" :cluster-category="clusterCategory" :run-conf="detail.runConf"
              v-else-if="jobTypeDetail?.engineType == 'flink'" />
    <template v-if="confInitialized && dataInitialized && metadataColumnsConfInitialized">
      <a-divider />
      <SparkDataCalc ref="jobConfFormRef" :job-conf="detail.jobConf" v-if="detail.jobType === 'spark-data-calc'" />
      <FlinkDataCalc ref="jobConfFormRef" :job-conf="detail.jobConf" v-else-if="detail.jobType === 'flink-data-calc'" />
    </template>
    <a-form-item :wrapper-col="wrapCols.l125" class="form-item-360">
      <a-button type="primary" @click="save">保存</a-button>
      <slot name="buttons"></slot>
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>