<template>
  <a-drawer :visible="visible" :title="`${title} ${typeMap[detail.datasourceType]} ${detail.version}`"
    @close="closeDrawer" width="600px">
    <a-form ref="formRef" :model="detail" @finish="save" :rules="rules" :label-col="{ span: 4 }"
      :wrapper-col="{ span: 20 }">
      <a-form-item ref="name" label="名称" name="name">
        <a-input v-model:value.trim="detail.name" type="text" />
      </a-form-item>
      <a-form-item ref="description" label="描述" name="description">
        <a-input v-model:value.trim="detail.description" type="text" />
      </a-form-item>
      <template v-for="(item, index) in datasourceTypeDetail.configKeys" :key="index">
        <a-form-item :ref="item.keyCode" :label="item.keyName" :name="['datasourceConfig', item.keyCode]"
          :rules="getRules(item)">
          <template v-if="item.valueType === 'STRING'">
            <a-input v-model:value="detail.datasourceConfig[item.keyCode]" :placeholder="item.description" />
          </template>
          <template v-else-if="item.valueType === 'JSON'">
            <a-textarea v-model:value="detail.datasourceConfig[item.keyCode]" :placeholder="item.description"
              :auto-size="{ minRows: 5, maxRows: 100 }" />
          </template>
          <template v-else-if="item.valueType === 'TEXT'">
            <a-textarea v-model:value="detail.datasourceConfig[item.keyCode]" :placeholder="item.description"
              :auto-size="{ minRows: 5, maxRows: 100 }" />
          </template>
          <template v-else-if="item.valueType === 'PASSWORD'">
            <a-input-password v-model:value="detail.datasourceConfig[item.keyCode]" :placeholder="item.description" />
          </template>
          <template v-else-if="item.valueType === 'INTEGER'">
            <a-input-number v-model:value="detail.datasourceConfig[item.keyCode]" :placeholder="item.description" />
          </template>
        </a-form-item>
      </template>
      <a-form-item>
        <a-button type="primary" html-type="submit">保存</a-button>
        <a-button style="margin-left: 10px" @click="closeDrawer">取消</a-button>
      </a-form-item>
    </a-form>
  </a-drawer>
</template>

<script lang="ts">
import { notification } from 'ant-design-vue'
import type { ValidateErrorEntity } from 'ant-design-vue/es/form/interface'
import type { AxiosRequestConfig } from 'axios'
import { defineComponent, inject, reactive, ref, toRaw, toRefs, watch } from "vue"
import common from '@/composables/common'
import { request } from '@/utils/request-utils'

const rules = {
  id: [
    { type: 'integer', required: true, message: 'Id不能为空', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '名称不能为空', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '描述不能为空', trigger: 'blur' }
  ]
}


export default defineComponent({
  props: {
    pk: {
      type: Object,
      required: false,
      default: () => undefined
    },
    typeVersion: {
      type: Array,
      default: () => ({})
    },
    operateType: {
      type: Number,
      default: () => common.DataOperationType.default
    },
    visible: {
      type: Boolean,
      default: () => false
    },
  },
  setup(props, { emit }) {
    const { pk, typeVersion, operateType, visible } = toRefs(props)
    const title = ref<string>('')
    const formRef = ref()
    const detail = reactive<any>({
      id: undefined,
      name: '',
      description: '',
      datasourceType: '',
      version: '',
      datasourceConfig: {},
    })
    const resetForm = () => {
      formRef.value.resetFields()
      detail.id = undefined
      detail.datasourceConfig = {}
    }
    const closeAddOrEditDrawer = inject<(newVal: boolean) => void>('closeAddOrEditDrawer')
    const typeMap = inject<Map<string, string>>('typeMap')
    const closeDrawer = () => {
      resetForm()
      if (closeAddOrEditDrawer) closeAddOrEditDrawer(false)
    }

    const getRules = (configKey: any) => {
      let rules = []
      if (configKey.required) {
        rules.push({ required: true, message: `${configKey.keyName}不能为空`, trigger: 'blur' })
      }
      if (configKey.valueRegex) {
        rules.push({ pattern: new RegExp(configKey.valueRegex), message: `${configKey.keyName}格式不正确`, trigger: 'blur' })
      }
      return rules
    }

    const getDetail = (newVal: boolean) => {
      if (!newVal) return
      if (operateType.value === common.DataOperationType.default) return
      if (operateType.value === common.DataOperationType.create) {
        title.value = `添加数据源配置`
        detail.datasourceType = typeVersion.value[0]
        detail.version = typeVersion.value[1]
        getDatasourceTypeDetail({})
      } else {
        if (operateType.value === common.DataOperationType.copy)
          title.value = '复制数据源配置'
        else if (operateType.value === common.DataOperationType.update)
          title.value = '编辑数据源配置'

        request({ url: '/datasource/detail', method: 'GET', params: pk.value }).then(response => {
          if (operateType.value !== common.DataOperationType.copy) {
            detail.id = response.id
          }
          detail.name = response.name
          detail.description = response.description
          detail.datasourceType = response.datasourceType
          detail.version = response.version
          getDatasourceTypeDetail(response.datasourceConfig)
        })
      }
    }

    watch(visible, getDetail)

    const save = () => {
      formRef.value.validate().then(() => {
        const requestConfig: AxiosRequestConfig = operateType.value === common.DataOperationType.update
          ? { url: '/datasource/update', method: "PATCH", data: toRaw(detail), params: pk.value }
          : { url: '/datasource/add', method: "POST", data: toRaw(detail) }
        request(requestConfig).then(response => {
          notification.success({
            message: "保存成功"
          })
          closeDrawer()
          emit("save", response)
        })
      }).catch((error: ValidateErrorEntity<any>) => {
        console.log(error)
        notification.error({
          message: "参数验证失败"
        })
      })
    }

    const datasourceTypeDetail = reactive<any>({
      configKeys: [],
    })

    const getDatasourceTypeDetail = (datasourceConfig: any) => {
      if (!detail.datasourceType) {
        datasourceTypeDetail.configKeys = []
        return;
      }
      let datasourceTypePk = {
        typeCode: detail.datasourceType
      }
      request({ url: '/datasource-type/detail', method: 'GET', params: datasourceTypePk }).then(response => {
        datasourceTypeDetail.configKeys = (response.configKeys || []).filter((k: any) => k.versions == '*' || k.versions.includes(detail.version))
        for (let k of datasourceTypeDetail.configKeys) {
          detail.datasourceConfig[k.keyCode] = datasourceConfig[k.keyCode] || k.defaultValue || undefined
        }
      })
    }

    return {
      title, detail,
      formRef,
      rules,
      getRules,
      save,
      datasourceTypeDetail,
      getDatasourceTypeDetail,
      closeDrawer,
      typeMap
    }
  }
})
</script>

<style scoped></style>