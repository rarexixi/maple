<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { useTemplateRef } from "vue"

import common from "@/composables/common"

interface SparkRunConf {
  jobmanagerMemory: string
  taskmanagerMemory: string
  numberOfTaskSlots: string
  numTaskmangager: string
  jars: string
}

const {runConf} = defineProps<{
  runConf: SparkRunConf,
}>()

const rules = {
  jobmanagerMemory: [
    {required: true, message: 'driver-memory 不能为空', trigger: 'blur'}
  ],
  taskmanagerMemory: [
    {required: true, message: 'driver-cores 不能为空', trigger: 'blur'}
  ],
  numberOfTaskSlots: [
    {required: true, message: 'executor-memory 不能为空', trigger: 'blur'}
  ],
  numTaskmangager: [
    {required: true, message: 'executor-cores 不能为空', trigger: 'blur'}
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
        <a-form-item ref="jobmanagerMemory" label="JM内存" name="jobmanagerMemory" class="form-item-320">
          <a-input v-model:value.number="runConf.jobmanagerMemory" type="text" />
        </a-form-item>
        <a-form-item ref="taskmanagerMemory" label="TM内存" name="taskmanagerMemory" class="form-item-320">
          <a-input v-model:value.trim="runConf.taskmanagerMemory" type="text" />
        </a-form-item>
        <a-form-item ref="numberOfTaskSlots" label="TM任务槽数" name="numberOfTaskSlots" class="form-item-320">
          <a-input v-model:value.number="runConf.numberOfTaskSlots" type="text" />
        </a-form-item>
        <a-form-item ref="numTaskmangager" label="TM个数" name="numTaskmangager" class="form-item-320">
          <a-input v-model:value.number="runConf.numTaskmangager" type="text" />
        </a-form-item>
        <a-form-item ref="jars" label="jars" name="jars" class="form-item-320">
          <a-input v-model:value.number="runConf.jars" type="text" />
        </a-form-item>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>