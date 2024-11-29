<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import { notification } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { useTemplateRef } from "vue"

const detail = defineModel<any>()

const {} = defineProps<{}>()


const rules = {
  typeCode: [
    {required: true, message: '类型编码不能为空', trigger: 'blur'}
  ],
  classifier: [
    {required: true, message: '分类不能为空', trigger: 'blur'}
  ],
  configurations: [
    {required: true, message: '数据源配置信息不能为空', trigger: 'blur'}
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

const subFormLabelCol = {
  style: {
    width: 'auto'
  }
}

interface ConfOption {
  keyCode: string;
  keyName: string;
  dataType: string;
  versions: string;
  defaultValue: string;
  nullable: boolean;
  valueRegex: string;
  description: string;
}

const removeConfOption = (item: ConfOption) => {
  let index = detail.value.configurations.indexOf(item);
  if (index !== -1) {
    detail.value.configurations.splice(index, 1);
  }
};
const addConfOption = () => {
  detail.value.configurations.push({
    keyCode: '',
    keyName: '',
    dataType: 'TEXT',
    versions: '',
    defaultValue: '',
    nullable: false,
    valueRegex: '',
    description: '',
  });
};

</script>

<template>
  <a-form ref="formRef" :model="detail" @finish="save" :rules="rules"
          :label-col="{ span: labelWidth }" :wrapper-col="{ span: 24-labelWidth }">
    <a-form-item ref="typeCode" label="类型编码" name="typeCode">
      <a-input v-model:value.trim="detail.typeCode" type="text" />
    </a-form-item>
    <a-form-item ref="typeName" label="类型名称" name="typeName">
      <a-input v-model:value.trim="detail.typeName" type="text" />
    </a-form-item>
    <a-form-item ref="icon" label="图标地址" name="icon">
      <a-input v-model:value.trim="detail.icon" type="text" />
    </a-form-item>
    <a-form-item ref="classifier" label="分类" name="classifier">
      <a-input v-model:value.trim="detail.classifier" type="text" />
    </a-form-item>
    <a-form-item ref="versions" label="版本" name="versions">
      <a-input v-model:value.trim="detail.versions" type="text" />
    </a-form-item>
    <h3>
      <span style="font-weight: bold">数据源配置信息</span>
      <a-button type="dashed" @click="addConfOption" style="margin-left: 10px">
        <PlusOutlined />
        添加配置项
      </a-button>
    </h3>
    <template v-for="(confOption, index) in detail.configurations">
      {{ confOption }}<br>
    </template>
    <div style="width: 100%; overflow-x: auto;">
      <table class="config-keys-table" style="text-align: left;">
        <thead>
        <tr>
          <th style="position: sticky; left: 0; z-index: 2"></th>
          <th style="position: sticky; left: 32px; z-index: 2">配置编码</th>
          <th>配置名</th>
          <th>版本</th>
          <th>默认值</th>
          <th>类型</th>
          <th>可空</th>
          <th>校验正则</th>
          <th>配置说明</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(confOption, index) in detail.configurations">
          <td style="position: sticky; left: 0; z-index: 2;">
            <a-form-item style="width: 30px">
              <a-button type="link" @click="() => removeConfOption(confOption)" danger>
                <template #icon>
                  <MinusCircleOutlined />
                </template>
              </a-button>
            </a-form-item>
          </td>
          <td style="position: sticky; left: 32px; z-index: 2;">
            <a-form-item label="" :name="['configurations', index, 'keyCode']" :label-col="subFormLabelCol"
                         :rules="[{ required: true, message: '配置编码不能为空', trigger: 'blur' }]">
              <a-input v-model:value="confOption.keyCode" style="width: 170px" />
            </a-form-item>
          </td>
          <td>
            <a-form-item label="" :name="['configurations', index, 'keyName']" :label-col="subFormLabelCol">
              <a-input v-model:value="confOption.keyName" style="width: 120px" />
            </a-form-item>
          </td>
          <td>
            <a-form-item label="" :name="['configurations', index, 'versions']" :label-col="subFormLabelCol">
              <a-input v-model:value="confOption.versions" style="width: 170px" />
            </a-form-item>
          </td>
          <td>
            <a-form-item label="" :name="['configurations', index, 'defaultValue']" :label-col="subFormLabelCol">
              <a-input v-model:value="confOption.defaultValue" style="width: 170px" />
            </a-form-item>
          </td>
          <td>
            <a-form-item label="" :name="['configurations', index, 'dataType']" :label-col="subFormLabelCol">
              <a-select v-model:value="confOption.dataType" style="width: 120px">
                <a-select-option value="STRING">STRING</a-select-option>
                <a-select-option value="TEXT">TEXT</a-select-option>
                <a-select-option value="PASSWORD">PASSWORD</a-select-option>
                <a-select-option value="JSON">JSON</a-select-option>
              </a-select>
            </a-form-item>
          </td>
          <td>
            <a-form-item label="" :name="['configurations', index, 'nullable']" :label-col="subFormLabelCol">
              <a-switch v-model:checked="confOption.nullable" />
            </a-form-item>
          </td>
          <td>
            <a-form-item label="" :name="['configurations', index, 'valueRegex']" :label-col="subFormLabelCol">
              <a-input v-model:value="confOption.valueRegex" style="width: 250px" />
            </a-form-item>
          </td>
          <td>
            <a-form-item label="" :name="['configurations', index, 'description']" :label-col="subFormLabelCol">
              <a-input v-model:value="confOption.description" style="width: 250px" />
            </a-form-item>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <a-form-item :wrapper-col="{ offset: labelWidth }">
      <a-button type="primary" html-type="submit">保存</a-button>
      <slot name="buttons"></slot>
    </a-form-item>
  </a-form>
</template>

<style scoped>

.config-keys-table {
  border-top: 1px solid #eeeeee;
  border-bottom: 1px solid #eeeeee;
}

.config-keys-table th,
.config-keys-table td {
  background-color: #ffffff !important;
}

.config-keys-table th {
  padding: 3px;
}
</style>