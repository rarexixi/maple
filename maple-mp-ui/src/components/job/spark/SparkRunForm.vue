<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { useTemplateRef } from "vue"

import common from "@/composables/common"

interface SparkRunConf {
  driverMemory: string
  driverCores: string
  executorMemory: string
  executorCores: string
  numExecutors: string
}

const {runConf} = defineProps<{
  runConf: SparkRunConf,
}>()

const rules = {
  driverMemory: [
    {required: true, message: 'driver-memory 不能为空', trigger: 'blur'}
  ],
  driverCores: [
    {required: true, message: 'driver-cores 不能为空', trigger: 'blur'}
  ],
  executorMemory: [
    {required: true, message: 'executor-memory 不能为空', trigger: 'blur'}
  ],
  executorCores: [
    {required: true, message: 'executor-cores 不能为空', trigger: 'blur'}
  ],
  numExecutors: [
    {required: true, message: 'num-executors 不能为空', trigger: 'blur'}
  ],
}

const formRef = useTemplateRef<FormInstance>("formRef");

defineExpose({
  validate: common.getFormValidateFun(formRef),
})

const labelWidth = 12

</script>

<template>
  <a-typography-title :level="5">资源配置</a-typography-title>
  <a-form ref="formRef" :model="runConf" :rules="rules"
          :label-col="{ span: labelWidth }" :wrapper-col="{ span: 24-labelWidth }">
    <a-flex wrap="wrap">
        <a-form-item ref="driverMemory" label="driver-memory" name="driverMemory" class="form-item-320">
          <a-input v-model:value.trim="runConf.driverMemory" type="text" />
        </a-form-item>
        <a-form-item ref="driverCores" label="driver-cores" name="driverCores" class="form-item-320">
          <a-input v-model:value.number="runConf.driverCores" type="text" />
        </a-form-item>
        <a-form-item ref="executorMemory" label="executor-memory" name="executorMemory" class="form-item-320">
          <a-input v-model:value.trim="runConf.executorMemory" type="text" />
        </a-form-item>
        <a-form-item ref="executorCores" label="executor-cores" name="executorCores" class="form-item-320">
          <a-input v-model:value.number="runConf.executorCores" type="text" />
        </a-form-item>
        <a-form-item ref="numExecutors" label="num-executors" name="numExecutors" class="form-item-320">
          <a-input v-model:value.number="runConf.numExecutors" type="text" />
        </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>