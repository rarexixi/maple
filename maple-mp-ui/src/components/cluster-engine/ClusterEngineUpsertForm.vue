<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { useTemplateRef } from "vue"

import common from "@/composables/common"
import type { ValidatableComponent } from "@/composables/models";

import SparkConfig from "@/components/cluster-engine/SparkConfig.vue"
import FlinkConfig from "@/components/cluster-engine/FlinkConfig.vue"

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
const engineConfRef = useTemplateRef<ValidatableComponent>("engineConfRef")

const emit = defineEmits<{
  (e: 'save'): void
}>()

async function save() {
  let validated = await common.getFormValidated(engineConfRef)
  if (!validated) {
    common.notifyValidateError()
    return
  }
  formRef.value?.validate().then(() => {
    emit("save")
  }).catch((error: ValidateErrorEntity<any>) => {
    common.notifyValidateError()
  })
}

const labelWidth = 3

</script>

<template>
  <a-form ref="formRef" :model="detail" :rules="rules"
          :label-col="{ span: labelWidth }" :wrapper-col="{ span: 24-labelWidth }">
    <a-form-item ref="clusterId" label="所属集群" name="clusterId">
      <a-select v-model:value="detail.clusterId" :options="clusterOptions" allow-clear placeholder="请选择" />
    </a-form-item>
    <a-form-item ref="version" label="引擎版本" name="version">
      <a-input v-model:value.trim="detail.version" type="text" />
    </a-form-item>
    <a-form-item ref="engineHome" label="引擎目录" name="engineHome">
      <a-input v-model:value.trim="detail.engineHome" type="text" />
    </a-form-item>
    <SparkConfig ref="engineConfRef" v-model="detail.engineConf"/>
    <!--<template v-if="detail.name == 'spark'">-->
    <!--  <SparkConfig ref="engineConfRef" v-model="detail.engineConf"/>-->
    <!--</template>-->
    <!--<template v-else-if="detail.name == 'flink'">-->
    <!--  <FlinkConfig ref="engineConfRef" v-model="detail.engineConf"/>-->
    <!--</template>-->
    <a-form-item :wrapper-col="{ offset: labelWidth }">
      <a-button type="primary" @click="save">保存</a-button>
      <slot name="buttons"></slot>
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>