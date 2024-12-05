<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { notification } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { useTemplateRef } from "vue"

import SparkConfig from "@/components/cluster-engine/SparkConfig.vue";
import FlinkConfig from "@/components/cluster-engine/FlinkConfig.vue";

const detail = defineModel<any>()

const {
  clusterOptions,
} = defineProps<{
  clusterOptions: any[],
}>()


const rules = {
  id: [
    { type: 'integer', required: true, message: '引擎ID不能为空', trigger: 'blur' }
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

const labelWidth = 3

</script>

<template>
  <a-form ref="formRef" :model="detail" @finish="save" :rules="rules"
          :label-col="{ span: labelWidth }" :wrapper-col="{ span: 24-labelWidth }">
    <a-form-item ref="cluster" label="所属集群" name="cluster">
      <a-select v-model:value="detail.cluster" :options="clusterOptions" allow-clear placeholder="请选择" />
    </a-form-item>
    <a-form-item ref="version" label="引擎版本" name="version">
      <a-input v-model:value.trim="detail.version" type="text" />
    </a-form-item>
    <a-form-item ref="engineHome" label="引擎目录" name="engineHome">
      <a-input v-model:value.trim="detail.engineHome" type="text" />
    </a-form-item>
    <template v-if="detail.name == 'spark'">
      <SparkConfig v-model="detail.engineConf"/>
    </template>
    <template v-else-if="detail.name == 'flink'">
      <FlinkConfig v-model="detail.engineConf"/>
    </template>
    <a-form-item :wrapper-col="{ offset: labelWidth }">
      <a-button type="primary" html-type="submit">保存</a-button>
      <slot name="buttons"></slot>
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>