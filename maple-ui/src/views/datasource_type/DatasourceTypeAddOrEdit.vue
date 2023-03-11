<template>
  <a-drawer :visible="visible" :title="title" @close="closeDrawer" width="60%">
    <a-form ref="formRef" :model="detail" @finish="save" :rules="rules" :label-col="{style: { width: '80px' }}">
      <a-row>
        <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <a-form-item ref="typeCode" :wrapper-col="{span:24}" label="类型编码" name="typeCode">
            <a-input v-model:value.trim="detail.typeCode" type="text" />
          </a-form-item>
        </a-col>
        <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <a-form-item ref="typeName" label="类型名称" name="typeName">
            <a-input v-model:value.trim="detail.typeName" type="text" />
          </a-form-item>
        </a-col>
        <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <a-form-item ref="classifier" label="分类" name="classifier">
            <a-input v-model:value.trim="detail.classifier" type="text" />
          </a-form-item>
        </a-col>
        <a-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
          <a-form-item ref="versions" label="版本" name="versions">
            <a-input v-model:value.trim="detail.versions" type="text" />
          </a-form-item>
        </a-col>
        <a-col :span="24">
          <a-form-item ref="icon" label="图标地址" name="icon">
            <a-input v-model:value.trim="detail.icon" type="text" />
          </a-form-item>
        </a-col>
      </a-row>
      <h3>数据源配置管理</h3>
      <div style="width: 100%; overflow-x: auto;">
        <a-space v-for="(configKey, index) in detail.configKeys" style="width: 1750px; overflow-x: auto" :key="configKey.id">
          <a-form-item>
            {{ index + 1 }}
            <a-button type="link" @click="removeConfigKey(configKey)" danger>
              <template #icon>
                <MinusCircleOutlined />
              </template>
            </a-button>
          </a-form-item>
          <a-form-item label="版本" :name="['configKeys', index, 'versions']" :label-col="subFormLabelCol">
            <a-input v-model:value="configKey.versions" />
          </a-form-item>
          <a-form-item label="配置编码" :name="['configKeys', index, 'keyCode']" :label-col="subFormLabelCol" :rules="[{ required: true, message: '配置编码不能为空', trigger: 'blur' }]">
            <a-input v-model:value="configKey.keyCode" />
          </a-form-item>
          <a-form-item label="配置名" :name="['configKeys', index, 'keyName']" :label-col="subFormLabelCol">
            <a-input v-model:value="configKey.keyName" style="width: 120px" />
          </a-form-item>
          <a-form-item label="默认值" :name="['configKeys', index, 'defaultValue']" :label-col="subFormLabelCol">
            <a-input v-model:value="configKey.defaultValue" />
          </a-form-item>
          <a-form-item label="类型" :name="['configKeys', index, 'valueType']" :label-col="subFormLabelCol">
            <a-select v-model:value="configKey.valueType" style="width: 120px">
              <a-select-option value="STRING">STRING</a-select-option>
              <a-select-option value="TEXT">TEXT</a-select-option>
              <a-select-option value="PASSWORD">PASSWORD</a-select-option>
              <a-select-option value="JSON">JSON</a-select-option>
            </a-select>
          </a-form-item>
          <a-form-item label="必填" :name="['configKeys', index, 'required']" :label-col="subFormLabelCol">
            <a-switch v-model:checked="configKey.required" :checked-value="1" :un-checked-value="0" />
          </a-form-item>
          <a-form-item label="校验正则" :name="['configKeys', index, 'valueRegex']" :label-col="subFormLabelCol">
            <a-input v-model:value="configKey.valueRegex" style="width: 250px" />
          </a-form-item>
          <a-form-item label="配置说明" :name="['configKeys', index, 'description']" :label-col="subFormLabelCol">
            <a-input v-model:value="configKey.description" style="width: 250px" />
          </a-form-item>
        </a-space>
      </div>
      <a-form-item>
        <a-button type="dashed" block @click="addConfigKey">
          <PlusOutlined />
          添加配置项
        </a-button>
      </a-form-item>
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
  typeCode: [
    { required: true, message: '类型编码不能为空', trigger: 'blur' }
  ],
  classifier: [
    { required: true, message: '分类不能为空', trigger: 'blur' }
  ],
}

const subFormLabelCol = {
  style: {
    width: 'auto'
  }
}

interface ConfigKey {
  datasourceType: string;
  versions: string;
  keyCode: number;
  keyName: string;
  keyOrder: string;
  defaultValue: number;
  valueType: string;
  required: string;
  valueRegex: number;
  description: string;
}

export default defineComponent({
  props: {
    pk: {
      type: [Object, Map],
      required: false,
      default: () => undefined
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
    const { pk, operateType, visible } = toRefs(props)
    const title = ref<string>('')
    const formRef = ref()
    const detail = reactive<any>({
      typeCode: '',
      typeName: '',
      icon: '',
      classifier: '',
      versions: '',
      configKeys: [],
    })
    const resetForm = () => {
      formRef.value.resetFields()
      detail.configKeys = []
    }
    const closeAddOrEditDrawer = inject<(newVal: boolean) => void>('closeAddOrEditDrawer')
    const closeDrawer = () => {
      resetForm()
      if (closeAddOrEditDrawer) closeAddOrEditDrawer(false)
    }

    const getDetail = (newVal: boolean) => {
      if (!newVal) return
      if (operateType.value === common.DataOperationType.default) return
      if (operateType.value === common.DataOperationType.create) {
        title.value = '添加数据源类型'
      } else {
        if (operateType.value === common.DataOperationType.copy)
          title.value = '复制数据源类型'
        else if (operateType.value === common.DataOperationType.update)
          title.value = '编辑数据源类型'

        request({ url: '/datasource-type/detail', method: 'GET', params: pk.value }).then(response => {
          detail.typeCode = response.typeCode
          detail.typeName = response.typeName
          detail.icon = response.icon
          detail.classifier = response.classifier
          detail.versions = response.versions
          detail.configKeys = response.configKeys || []
        })
      }
    }

    watch(visible, getDetail)

    const save = () => {
      formRef.value.validate().then(() => {
        const requestConfig: AxiosRequestConfig = operateType.value === common.DataOperationType.update
          ? { url: '/datasource-type/update', method: "PATCH", data: toRaw(detail), params: pk.value }
          : { url: '/datasource-type/add', method: "POST", data: toRaw(detail) }
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

    const removeConfigKey = (item: ConfigKey) => {
      let index = detail.configKeys.indexOf(item);
      if (index !== -1) {
        detail.configKeys.splice(index, 1);
      }
    };
    const addConfigKey = () => {
      detail.configKeys.push({
        datasourceType: undefined,
        versions: undefined,
        keyCode: undefined,
        keyName: undefined,
        keyOrder: undefined,
        defaultValue: undefined,
        valueType: 'TEXT',
        required: 0,
        valueRegex: undefined,
        description: undefined,
      });
    };

    return {
      title, detail,
      formRef,
      rules,
      subFormLabelCol,
      save,
      removeConfigKey,
      addConfigKey,
      closeDrawer
    }
  }
})
</script>

<style scoped>
</style>