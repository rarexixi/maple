<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { useTemplateRef } from "vue"

import common from "@/composables/common"

const detail = defineModel<any>()

const {
} = defineProps<{
}>()


const rules = {
  appName: [
    { required: true, message: '应用名称不能为空', trigger: 'blur' }
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
    common.notifyValidateError()
  })
}

const labelWidth = 4

</script>

<template>
  <a-form ref="formRef" :model="detail" :rules="rules"
          :label-col="{ span: labelWidth }" :wrapper-col="{ span: 24-labelWidth }">
    <a-form-item label="应用名称" name="appName">
      <a-input v-model:value.trim="detail.appName" type="text" />
    </a-form-item>
    <a-form-item label="应用访问密钥" name="accessKey">
      <a-input v-model:value.trim="detail.accessKey" type="text" />
    </a-form-item>
    <a-form-item label="允许请求的IP" name="legalHosts">
      <a-input v-model:value.trim="detail.legalHosts" type="text" />
    </a-form-item>
    <a-form-item label="回调接口" name="webhooks">
      <a-input v-model:value.trim="detail.webhooks" type="text" />
    </a-form-item>
    <a-form-item :wrapper-col="{ offset: labelWidth }">
      <a-button type="primary" @click="save">保存</a-button>
      <slot name="buttons"></slot>
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>