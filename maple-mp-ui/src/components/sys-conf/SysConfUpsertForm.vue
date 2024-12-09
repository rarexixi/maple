<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { useTemplateRef } from "vue"

import common from "@/composables/common"

const detail = defineModel<any>()

const {} = defineProps<{}>()


const rules = {
  confKey: [
    {required: true, message: '配置键不能为空', trigger: 'blur'}
  ],
  confValue: [
    {required: true, message: '配置值不能为空', trigger: 'blur'}
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
    <a-form-item ref="confKey" label="配置键" name="confKey">
      <a-input v-model:value.trim="detail.confKey" type="text" />
    </a-form-item>
    <a-form-item ref="confValue" label="配置值" name="confValue">
      <a-textarea v-model:value="detail.confValue" :autoSize="{ minRows: 5, maxRows: 100}" />
    </a-form-item>
    <a-form-item ref="desc" label="配置说明" name="desc">
      <a-textarea v-model:value="detail.desc" :autoSize="{ minRows: 5, maxRows: 100}" />
    </a-form-item>
    <a-form-item :wrapper-col="{ offset: labelWidth }">
      <a-button type="primary" @click="save">保存</a-button>
      <slot name="buttons"></slot>
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>