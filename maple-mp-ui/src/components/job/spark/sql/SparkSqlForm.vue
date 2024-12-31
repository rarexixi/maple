<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { useTemplateRef } from "vue"

import common from "@/composables/common"

const { runConf } = defineProps<{
  clusterCategory: string,
  jobType: string,
  runConf: any,
}>()

const rules = {
  driverMemory: [
    { required: true, message: 'driver-memory 不能为空', trigger: 'blur' }
  ],
  driverCores: [
    { required: true, message: 'driver-cores 不能为空', trigger: 'blur' }
  ],
  executorMemory: [
    { required: true, message: 'executor-memory 不能为空', trigger: 'blur' }
  ],
  executorCores: [
    { required: true, message: 'executor-cores 不能为空', trigger: 'blur' }
  ],
  numExecutors: [
    { required: true, message: 'num-executors 不能为空', trigger: 'blur' }
  ],
}

const formRef = useTemplateRef<FormInstance>("formRef");

defineExpose({
  validate: common.getFormValidateFun(formRef),
})

</script>

<template>
  <a-form ref="formRef" :model="runConf" :rules="rules" :label-col="common.Layout.labelCols.l125">
    <a-flex wrap="wrap">
      <a-typography-title :level="5">资源配置</a-typography-title>
      <a-flex-br />
      <a-form-item label="driver内存" name="driverMemory" class="form-item-360">
        <a-input v-model:value.trim="runConf.driverMemory" type="text" />
      </a-form-item>
      <a-form-item label="driver核心" name="driverCores" class="form-item-360">
        <a-input v-model:value.number="runConf.driverCores" type="text" />
      </a-form-item>
      <a-form-item label="executor内存" name="executorMemory" class="form-item-360">
        <a-input v-model:value.trim="runConf.executorMemory" type="text" />
      </a-form-item>
      <a-form-item label="executor核心" name="executorCores" class="form-item-360">
        <a-input v-model:value.number="runConf.executorCores" type="text" />
      </a-form-item>
      <a-form-item label="executor个数" name="numExecutors" class="form-item-360">
        <a-input v-model:value.number="runConf.numExecutors" type="text" />
      </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>