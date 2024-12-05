<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { notification } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { useTemplateRef } from "vue"

import K8sClusterConfig from "@/components/cluster/K8sClusterConfig.vue";
import YarnClusterConfig from "@/components/cluster/YarnClusterConfig.vue";

const detail = defineModel<any>()

const {
  categoryOptions,
} = defineProps<{
  categoryOptions: any[],
}>()

const rules = {
  id: [
    { type: 'integer', required: true, message: '集群ID不能为空', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '集群名称不能为空', trigger: 'blur' }
  ],
  category: [
    { required: true, message: '集群种类不能为空', trigger: 'change' }
  ],
  address: [
    { required: true, message: '集群地址不能为空', trigger: 'blur' }
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

const labelWidth = 4

</script>

<template>
  <a-form ref="formRef" :model="detail" @finish="save" :rules="rules"
          :label-col="{ span: labelWidth }" :wrapper-col="{ span: 24-labelWidth }">
    <a-form-item ref="name" label="集群名称" name="name">
      <a-input v-model:value.trim="detail.name" type="text" />
    </a-form-item>
    <a-form-item ref="category" label="集群种类" name="category">
      <a-select v-model:value="detail.category" :options="categoryOptions" allow-clear placeholder="请选择" />
    </a-form-item>
    <a-form-item ref="address" label="集群地址" name="address">
      <a-input v-model:value.trim="detail.address" type="text" />
    </a-form-item>
    <a-form-item ref="desc" label="集群说明" name="desc">
      <a-textarea v-model:value="detail.desc" type="textarea" :autosize="{ minRows: 5, maxRows: 100}" />
    </a-form-item>
    <template v-if="detail.category == 'K8s'">
      <a-form-item ref="clientCertData" label="客户端证书" :name="['clusterConf', 'config', 'clientCertData']"
                   :rules="[{ required: true, message: '客户端证书不能为空', trigger: 'blur' }]">
        <a-textarea v-model:value.trim="detail.clusterConf.config.clientCertData" placeholder="client-certificate-data"
                    :auto-size="{ minRows: 3, maxRows: 20 }" />
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