<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { useTemplateRef } from "vue"

import common from "@/composables/common"
import type { ValidatableComponent } from "@/composables/models"

import SparkRun from "@/components/job/spark/SparkRunForm.vue"
import FlinkRun from "@/components/job/flink/FlinkRunForm.vue"
import SparkDataCalc from "@/components/job/spark/data-calc/SparkDataCalcForm.vue"
import FlinkDataCalc from "@/components/job/flink/data-calc/FlinkDataCalcForm.vue"
import { useDatabaseTypesStore } from "@/stores/sys-conf";
import { useDatasourceStore } from "@/stores/sys-data";

const detail = defineModel<any>()

const {
  engineOptions,
  jobType
} = defineProps<{
  engineOptions: any[],
  jobType: any
}>()


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

const labelWidth = 8

</script>

<template>
  <a-form ref="formRef" :model="detail" :rules="rules"
          :label-col="{ span: labelWidth }" :wrapper-col="{ span: 24-labelWidth }">
    <a-flex wrap="wrap">
      <a-form-item ref="jobName" label="作业名" name="jobName" class="form-item-320">
        <a-input v-model:value.trim="detail.jobName" type="text" />
      </a-form-item>
      <a-form-item ref="engineId" label="引擎ID" name="engineId" class="form-item-320">
        <a-select v-model:value="detail.engineId" :options="engineOptions" allow-clear placeholder="请选择" />
      </a-form-item>
      <a-form-item ref="owner" label="作业负责人" name="owner" class="form-item-320">
        <a-input v-model:value.trim="detail.owner" type="text" />
      </a-form-item>
      <a-form-item ref="description" label="作业说明" name="description" class="form-item-640" :label-col="{ span: 4 }"
                   :wrapper-col="{ span: 20 }">
        <a-input v-model:value="detail.description" />
      </a-form-item>
    </a-flex>
    <a-divider />
    <SparkRun ref="runFormRef" :run-conf="detail.runConf" v-if="jobType?.engineType == 'spark'" />
    <FlinkRun ref="runFormRef" :run-conf="detail.runConf" v-else-if="jobType?.engineType == 'flink'" />
    <template v-if="confInitialized && dataInitialized">
      <a-divider />
      <SparkDataCalc ref="jobConfFormRef" :job-conf="detail.jobConf" v-if="detail.jobType === 'spark-data-calc'" />
      <FlinkDataCalc ref="jobConfFormRef" :job-conf="detail.jobConf" v-else-if="detail.jobType === 'flink-data-calc'" />
    </template>
    <a-form-item :wrapper-col="{ offset: labelWidth / 4 }" class="form-item-1280">
      <a-affix :offset-bottom="70">
        <a-button type="primary" @click="save">保存</a-button>
        <slot name="buttons"></slot>
      </a-affix>
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>