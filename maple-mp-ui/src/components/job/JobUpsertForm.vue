<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { useTemplateRef } from "vue"

import common from "@/composables/common"
import type { ValidatableComponent } from "@/composables/models"

import SparkRunForm from "@/components/job/spark/SparkRunForm.vue"
import SparkDataCalcArrayForm from "@/components/job/spark/data-calc/SparkDataCalcArrayForm.vue"
import SparkDataCalcGroupForm from "@/components/job/spark/data-calc/SparkDataCalcGroupForm.vue"

const detail = defineModel<any>()

const {
  engineOptions,
} = defineProps<{
  engineOptions: any[],
}>()


const rules = {
  id: [
    {type: 'integer', required: true, message: '作业ID不能为空', trigger: 'blur'}
  ],
  jobName: [
    {required: true, message: '作业名不能为空', trigger: 'blur'}
  ],
  jobType: [
    {required: true, message: '作业类型不能为空', trigger: 'blur'}
  ],
  engineId: [
    {type: 'integer', required: true, message: '引擎ID不能为空', trigger: 'change'}
  ],
}

const formRef = useTemplateRef<FormInstance>("formRef")
const runFormRef = useTemplateRef<ValidatableComponent>("runFormRef")
const jobConfFormRef = useTemplateRef<ValidatableComponent>("jobConfFormRef")

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
      <a-form-item ref="desc" label="作业说明" name="desc" class="form-item-640" :label-col="{ span: 4 }"
                   :wrapper-col="{ span: 20 }">
        <a-input v-model:value="detail.desc" />
      </a-form-item>
    </a-flex>
    <a-divider />
    <SparkRunForm ref="runFormRef" :run-conf="detail.runConf" />
    <a-divider />
    <!--<SparkDataCalcGroupForm ref="jobConfFormRef" :job-conf="detail.jobConf" />-->
    <SparkDataCalcGroupForm ref="jobConfFormRef" :job-conf="detail.jobConf"
                            v-if="detail.jobType === 'spark-data-calc-group'" />
    <SparkDataCalcArrayForm ref="jobConfFormRef" :job-conf="detail.jobConf"
                            v-else-if="detail.jobType === 'spark-data-calc-array'" />
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