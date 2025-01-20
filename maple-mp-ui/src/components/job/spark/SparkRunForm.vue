<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { ref, useTemplateRef } from "vue"

import common from "@/composables/common"

import ParamsMap from "@/components/ParamsMap.vue"

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

const advanced = ref<boolean>(false)

const formRef = useTemplateRef<FormInstance>("formRef")

defineExpose({
  validate: common.getFormValidateFun(formRef),
})

const labelCols = common.Layout.labelCols
</script>

<template>
  <a-form ref="formRef" :model="runConf" :rules="rules" :label-col="labelCols.l125">
    <a-flex wrap="wrap">
      <a-flex-br />
      <a-typography-title :level="5">启动配置</a-typography-title>
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
      <a-form-item label="高级配置" class="form-item-360">
        <a-checkbox v-model:checked="advanced" />
      </a-form-item>
      <template v-if="advanced">
        <a-flex-br />
        <a-form-item label="driverJavaOptions" name="driverJavaOptions" class="form-item-720">
          <a-input v-model:value.number="runConf.driverJavaOptions" type="text" />
        </a-form-item>
        <a-form-item label="driverClassPath" name="driverClassPath" class="form-item-720">
          <a-input v-model:value.number="runConf.driverClassPath" type="text" />
        </a-form-item>
        <a-form-item label="files" name="files" class="form-item-720">
          <a-input v-model:value.number="runConf.files" type="text" />
        </a-form-item>
        <a-form-item label="archives" name="archives" class="form-item-720">
          <a-input v-model:value.number="runConf.archives" type="text" />
        </a-form-item>
        <a-form-item label="jars" name="jars" class="form-item-720">
          <a-input v-model:value.number="runConf.jars" type="text" />
        </a-form-item>
        <a-flex-br />
        <a-form-item label="conf参数" name="confs" class="form-item-720">
          <params-map v-model:value="runConf.confs" />
        </a-form-item>
      </template>
    </a-flex>
  </a-form>
</template>

<style scoped>
</style>