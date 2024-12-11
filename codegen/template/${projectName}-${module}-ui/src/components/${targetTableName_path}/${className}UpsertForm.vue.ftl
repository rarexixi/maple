<#include "/include/table/properties.ftl">
<script setup lang="ts">
import type { FormInstance } from "ant-design-vue"
import type { ValidateErrorEntity } from "ant-design-vue/es/form/interface"
import { useTemplateRef } from "vue"

import common from "@/composables/common"

const detail = defineModel<any>()

const {
  <#list table.selectColumns as column>
  <#include "/include/column/properties.ftl">
  ${fieldNameExceptKey}Options,
  </#list>
  <#list table.fkSelectColumns as column>
  <#include "/include/column/properties.ftl">
  ${fieldNameExceptKey}Options,
  </#list>
} = defineProps<{
  <#list table.selectColumns as column>
  <#include "/include/column/properties.ftl">
  ${fieldNameExceptKey}Options: any[],
  </#list>
  <#list table.fkSelectColumns as column>
  <#include "/include/column/properties.ftl">
  ${fieldNameExceptKey}Options: any[],
  </#list>
}>()


const rules = {
  <#list table.columnsExceptBase as column>
  <#include "/include/column/properties.ftl">
  <#if ((column.pk && !column.autoIncrement) || (!column.notRequired && !column.nullable && !(column.columnDefault??)))>
  ${fieldName}: [
    { <#if (isInteger)>type: 'integer', <#elseif (isDecimal)>type: 'float', </#if>required: true, message: '${columnComment}不能为空', trigger: '<#if (column.select || column.fkSelect)>change<#else>blur</#if>' }
  ],
  </#if>
  </#list>
}

const formRef = useTemplateRef<FormInstance>("formRef")

const emit = defineEmits<{
  (e: 'save'): void
}>()

const save = () => {
  formRef.value?.validate().then(() => {
    emit("save")
  }).catch((error: ValidateErrorEntity<any>) => {
    common.notifyValidateError()
  })
}

const labelWidth = 4

</script>

<template>
  <a-form ref="formRef" :model="detail" :rules="rules"
          :label-col="{ span: labelWidth }" :wrapper-col="{ span: 24-labelWidth }">
    <#list table.columnsExceptBase as column>
    <#include "/include/column/properties.ftl">
    <#if column.notRequired>
    <#elseif column.autoIncrement>
    <#elseif (column.select || column.fkSelect)>
    <a-form-item ref="${fieldName}" label="${columnComment}" name="${fieldName}">
      <a-select v-model:value="detail.${fieldName}" :options="${fieldNameExceptKey}Options" allow-clear placeholder="请选择" />
    </a-form-item>
    <#elseif (column.validStatus)>
    <#elseif (isInteger)>
    <a-form-item ref="${fieldName}" label="${columnComment}" name="${fieldName}">
      <a-input-number v-model:value="detail.${fieldName}" />
    </a-form-item>
    <#elseif (isDecimal)>
    <a-form-item ref="${fieldName}" label="${columnComment}" name="${fieldName}">
      <a-input-number v-model:value="detail.${fieldName}" />
    </a-form-item>
    <#elseif (isDate)>
    <a-form-item ref="${fieldName}" label="${columnComment}" name="${fieldName}">
      <a-date-picker v-model:value="detail.${fieldName}" type="date" placeholder="选择日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" />
    </a-form-item>
    <#elseif (isDateTime)>
    <a-form-item ref="${fieldName}" label="${columnComment}" name="${fieldName}">
      <a-date-picker v-model:value="detail.${fieldName}" type="date" placeholder="选择日期时间" format="YYYY-MM-DD HH:mm:ss" value-format="YYYY-MM-DD HH:mm:ss" />
    </a-form-item>
    <#elseif (isContent)>
    <a-form-item ref="${fieldName}" label="${columnComment}" name="${fieldName}">
      <a-textarea v-model:value="detail.${fieldName}" :autoSize="{ minRows: 5, maxRows: 100}" />
    </a-form-item>
    <#elseif (isString)>
    <a-form-item ref="${fieldName}" label="${columnComment}" name="${fieldName}">
      <a-input v-model:value.trim="detail.${fieldName}" type="text" />
    </a-form-item>
    <#else>
    </#if>
    </#list>
    <a-form-item :wrapper-col="{ offset: labelWidth }">
      <a-button type="primary" @click="save">保存</a-button>
      <slot name="buttons"></slot>
    </a-form-item>
  </a-form>
</template>

<style scoped>
</style>