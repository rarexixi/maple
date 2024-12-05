<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { notification } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { useTemplateRef } from "vue"

const detail = defineModel<any>()

const {
} = defineProps<{
}>()


const rules = {
  id: [
    { type: 'integer', required: true, message: '作业ID不能为空', trigger: 'blur' }
  ],
  runContent: [
    { required: true, message: '执行内容不能为空', trigger: 'blur' }
  ],
  jobConf: [
    { required: true, message: '作业配置不能为空', trigger: 'blur' }
  ],
}

const formRef = useTemplateRef<FormInstance>("formRef")

const emit = defineEmits<{
  (e: 'save'): void
}>()

const save = () => {
  formRef.value?.validate().then(() => {
    emit("save")
  }).catch((error: ValidateErrorEntity<any>) => {
    console.log(error)
    notification.error({
      message: "参数验证失败"
    })
  })
}

const labelWidth = 4

</script>

<template>
  <a-form ref="formRef" :model="detail" @finish="save" :rules="rules"
          :label-col="{ span: labelWidth }" :wrapper-col="{ span: 24-labelWidth }">
    <a-form-item ref="jobName" label="作业名" name="jobName">
      <a-input v-model:value.trim="detail.jobName" type="text" />
    </a-form-item>
    <a-form-item ref="desc" label="作业说明" name="desc">
      <a-textarea v-model:value="detail.desc" type="textarea" :autosize="{ minRows: 5, maxRows: 100}" />
    </a-form-item>
    <a-form-item ref="jobType" label="作业类型" name="jobType">
      <a-input v-model:value.trim="detail.jobType" type="text" />
    </a-form-item>
    <a-form-item ref="clusterCategory" label="集群种类" name="clusterCategory">
      <a-input v-model:value.trim="detail.clusterCategory" type="text" />
    </a-form-item>
    <a-form-item ref="engineCategory" label="引擎种类" name="engineCategory">
      <a-input v-model:value.trim="detail.engineCategory" type="text" />
    </a-form-item>
    <a-form-item ref="engineVersion" label="引擎版本" name="engineVersion">
      <a-input v-model:value.trim="detail.engineVersion" type="text" />
    </a-form-item>
    <a-form-item ref="owner" label="作业负责人" name="owner">
      <a-input v-model:value.trim="detail.owner" type="text" />
    </a-form-item>
    <a-form-item ref="runContent" label="执行内容" name="runContent">
      <a-textarea v-model:value="detail.runContent" type="textarea" :autosize="{ minRows: 5, maxRows: 100}" />
    </a-form-item>
    <a-form-item ref="jobConf" label="作业配置" name="jobConf">
      <a-textarea v-model:value="detail.jobConf" type="textarea" :autosize="{ minRows: 5, maxRows: 100}" />
    </a-form-item>
    <a-form-item :wrapper-col="{ offset: labelWidth }">
      <a-button type="primary" html-type="submit">保存</a-button>
      <slot name="buttons"></slot>
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>