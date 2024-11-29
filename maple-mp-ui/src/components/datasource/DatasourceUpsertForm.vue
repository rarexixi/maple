<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { notification } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { useTemplateRef } from "vue"

const detail = defineModel<any>()

const {confOptions} = defineProps<{
  confOptions: any[],
}>()


const rules = {
  id: [
    { type: 'integer', required: true, message: 'Id不能为空', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '数据源名称不能为空', trigger: 'blur' }
  ],
  datasourceConf: [
    { required: true, message: '数据源配置不能为空', trigger: 'blur' }
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

const getRules = (configKey: any) => {
  let rules = []
  if (!configKey.nullable) {
    rules.push({required: true, message: `${configKey.keyName}不能为空`, trigger: 'blur'})
  }
  if (configKey.valueRegex) {
    rules.push({pattern: new RegExp(configKey.valueRegex), message: `${configKey.keyName}格式不正确`, trigger: 'blur'})
  }
  return rules
}

const labelWidth = 4

</script>

<template>
  <a-form ref="formRef" :model="detail" @finish="save" :rules="rules"
          :label-col="{ span: labelWidth }" :wrapper-col="{ span: 24-labelWidth }">
    <a-form-item ref="name" label="数据源名称" name="name">
      <a-input v-model:value.trim="detail.name" type="text" />
    </a-form-item>
    <a-form-item ref="description" label="数据源描述" name="description">
      <a-input v-model:value.trim="detail.description" type="text" />
    </a-form-item>
    <template v-for="(item, index) in confOptions" :key="item.keyCode">
      <a-form-item :ref="item.keyCode" :label="item.keyName" :name="['datasourceConf', item.keyCode]"
                   :rules="getRules(item)">
        <template v-if="item.dataType === 'STRING'">
          <a-input v-model:value="detail.datasourceConf[item.keyCode]" :placeholder="item.description" />
        </template>
        <template v-else-if="item.dataType === 'JSON'">
          <a-textarea v-model:value="detail.datasourceConf[item.keyCode]" :placeholder="item.description"
                      :auto-size="{ minRows: 5, maxRows: 100 }" />
        </template>
        <template v-else-if="item.dataType === 'TEXT'">
          <a-textarea v-model:value="detail.datasourceConf[item.keyCode]" :placeholder="item.description"
                      :auto-size="{ minRows: 3, maxRows: 100 }" />
        </template>
        <template v-else-if="item.dataType === 'PASSWORD'">
          <a-input-password v-model:value="detail.datasourceConf[item.keyCode]" :placeholder="item.description" />
        </template>
        <template v-else-if="item.dataType === 'INTEGER'">
          <a-input-number v-model:value="detail.datasourceConf[item.keyCode]" :placeholder="item.description" />
        </template>
      </a-form-item>
    </template>
    <a-form-item :wrapper-col="{ offset: labelWidth }">
      <a-button type="primary" html-type="submit">保存</a-button>
      <slot name="buttons"></slot>
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>