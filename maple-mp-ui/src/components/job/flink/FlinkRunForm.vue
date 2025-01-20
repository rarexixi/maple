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
  jobManagerMemory: [
    { required: true, message: 'driver-memory 不能为空', trigger: 'blur' }
  ],
  taskManagerMemory: [
    { required: true, message: 'driver-cores 不能为空', trigger: 'blur' }
  ],
  numberOfTaskSlots: [
    { required: true, message: 'executor-memory 不能为空', trigger: 'blur' }
  ],
  numberOfTaskManager: [
    { required: true, message: 'executor-cores 不能为空', trigger: 'blur' }
  ],
}

const advanced = ref<boolean>(false)

const formRef = useTemplateRef<FormInstance>("formRef");

defineExpose({
  validate: common.getFormValidateFun(formRef),
})

</script>

<template>
  <a-form ref="formRef" :model="runConf" :rules="rules" :label-col="common.Layout.labelCols.l125">
    <a-flex wrap="wrap">
      <a-flex-br />
      <a-typography-title :level="5">启动配置</a-typography-title>
      <a-flex-br />
      <a-form-item label="JM内存" name="jobManagerMemory" class="form-item-360">
        <a-input v-model:value.number="runConf.jobManagerMemory" type="text" />
      </a-form-item>
      <a-form-item label="TM内存" name="taskManagerMemory" class="form-item-360">
        <a-input v-model:value.trim="runConf.taskManagerMemory" type="text" />
      </a-form-item>
      <a-form-item label="TM任务槽数" name="numberOfTaskSlots" class="form-item-360">
        <a-input v-model:value.number="runConf.numberOfTaskSlots" type="text" />
      </a-form-item>
      <a-form-item label="TM个数" name="numberOfTaskManager" class="form-item-360">
        <a-input v-model:value.number="runConf.numberOfTaskManager" type="text" />
      </a-form-item>
      <template v-if="clusterCategory == 'K8s'">
        <a-form-item label="JM CPU" name="jobManagerCores" class="form-item-360">
          <a-input v-model:value.number="runConf.jobManagerCores" type="text" />
        </a-form-item>
        <a-form-item label="TM CPU" name="taskManagerCores" class="form-item-360">
          <a-input v-model:value.number="runConf.taskManagerCores" type="text" />
        </a-form-item>
        <a-form-item label="JM HA" name="jobManagerHaEnable" class="form-item-360">
          <a-switch v-model:checked="runConf.jobManagerHaEnable" type="text" />
        </a-form-item>
        <a-form-item label="JM 副本数" name="jobManagerReplicas" class="form-item-360">
          <a-input v-model:value.number="runConf.jobManagerReplicas" type="text" />
        </a-form-item>
      </template>
      <template v-if="jobType == 'flink-py'">

      </template>
      <a-form-item label="高级配置" class="form-item-360">
        <a-checkbox v-model:checked="advanced" />
      </a-form-item>
      <template v-if="advanced">
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