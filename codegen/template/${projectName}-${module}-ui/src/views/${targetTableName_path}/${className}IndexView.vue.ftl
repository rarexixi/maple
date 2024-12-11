<#include "/include/table/properties.ftl">
<script lang="ts" setup>
import type { FormInstance } from "ant-design-vue"
import { reactive, onMounted, ref, useTemplateRef } from "vue"

import common from '@/composables/common'
import { listSearch, pageListSearch } from '@/composables/requests'
import { ${className}Apis<#list table.fkSelectColumns as column>, ${column.fkSelectColumn.foreignClassName}Apis</#list> } from "@/composables/service-apis"
import type { OperateCallback } from "@/composables/table-operations"
import { getMultiDataOperations, getSingleDataOperations } from "@/composables/table-operations"
import { getSelection } from '@/composables/table-selection'
import { useBreadcrumbStore } from "@/stores/breadcrumbs"

import DataOperations from "@/components/DataOperations.vue"
import TableOperations from "@/components/TableOperations.vue"
import ${className}UpsertForm from "@/components/${tablePath}/${className}UpsertForm.vue"

const pkFields = [<#list pks as column><#include "/include/column/properties.ftl">'${fieldName}'<#if (column?has_next)>, </#if></#list>]

const searchParams = reactive<any>({
<#list table.indexes as column>
<#include "/include/column/properties.ftl">
<#if (column.validStatus)>
<#elseif (column.select || column.fkSelect || column.pk)>
  ${fieldName}: undefined,
<#elseif (isInteger || isDecimal)>
  ${fieldName}Min: undefined,
  ${fieldName}Max: undefined,
<#elseif (isString)>
  ${fieldName}Contains: undefined,
<#elseif (isDate || isDateTime)>
  ${fieldName}Range: [],
</#if>
</#list>
})

const {
  pageNum,
  pageSize,
  dataPageList,
  search,
  resetSearch
} = pageListSearch(${className}Apis.pageList(), searchParams, useTemplateRef<FormInstance>("searchForm"))
const selection = getSelection()
const {selected, rowSelection} = selection
<#list table.fkSelectColumns as column>
<#include "/include/column/properties.ftl">

const ${fieldNameExceptKey}SearchParams = reactive<any>({
  <#list column.fkSelectColumn.conditions as condition>
  ${condition.fieldTargetName?uncap_first}: '${condition.value}'<#if condition?has_next>,</#if>
  </#list>
})
const {
  dataList: ${fieldNameExceptKey}Options
  dataMap: ${fieldNameExceptKey}OptionMap
} = listSearch(${column.fkSelectColumn.foreignClassName}Apis.list(), ${fieldNameExceptKey}SearchParams, undefined, common.convertToOptions('${column.fkSelectColumn.valueName?uncap_first}', '${column.fkSelectColumn.textName?uncap_first}'), common.setOptionMap('${column.fkSelectColumn.valueName?uncap_first}', '${column.fkSelectColumn.textName?uncap_first}'))
</#list>
<#list table.selectColumns as column>
<#include "/include/column/properties.ftl">

const ${fieldNameExceptKey}Options = [
  <#list column.selectOptions as option>
  { <#if (isInteger)>value: ${option.value}, label: '${option.text}'<#else>value: '${option.value}', label: '${option.text}'</#if> }<#if option?has_next>, </#if>
  </#list>
]
</#list>

onMounted(() => {
  // 设置面包屑
  const {setBreadcrumb} = useBreadcrumbStore()
  setBreadcrumb([{text: '${tableComment}'}])
})

const columns = [
  <#list table.columns as column>
  <#include "/include/column/properties.ftl">
  <#if (isContent || column.validStatus)>
  <#elseif (column.fkSelect)>
  { title: '${columnComment}', dataIndex: '${fieldName}', key: '${fieldName}', customRender: (row: any) => ${fieldNameExceptKey}OptionMap[row.text] },
  <#else>
  { title: '${columnComment}', dataIndex: '${fieldName}', key: '${fieldName}' },
  </#if>
  </#list>
  {title: '操作', dataIndex: 'action', key: 'action', fixed: 'right', width: 120},
]

const callback: OperateCallback = {
  detail: (response: any, copyPk: boolean) => {
    if (copyPk) {
      for (const field of pkFields) {
        detail[field] = response[field]
      }
    }
    <#list table.columnsExceptBase as column>
    <#include "/include/column/properties.ftl">
    <#if column.notRequired>
    <#elseif column.autoIncrement>
    <#elseif (column.fkSelect)>
    detail.${fieldName} = response.${fieldName} + ''
    <#else>
    detail.${fieldName} = response.${fieldName}
    </#if>
    </#list>
  },
  research: search,
  resetDetail: (detail: any) => {
    <#list table.columnsExceptBase as column>
    <#include "/include/column/properties.ftl">
    <#if !column.notRequired>
    detail.${fieldName} = <#if (isInteger || isDecimal)>undefined<#else>''</#if>
    </#if>
    </#list>
  },
  setItem: (detail: any, editIndex: number) => {
    dataPageList.list[editIndex] = detail
  }
}

const {
  dialogOperations,
  detail,
  get: getDetail,
  add,
  copy,
  edit,
  upsert,
  <#if hasValidStatusColumn>
  enable,
  disable,
  </#if>
  del
} = getSingleDataOperations(${className}Apis, callback)

const {
  title: drawerTitle,
  opened: upsertDrawerOpened,
  openDialog: showUpsertDrawer,
  closeDialog: closeUpsertDrawer
} = dialogOperations
<#if (table.hasUniPk)>

const {
  <#if hasValidStatusColumn>
  enableSelected,
  disableSelected,
  </#if>
  delSelected
} = getMultiDataOperations(selection, ${className}Apis, callback)
</#if>

</script>

<template>
  <div class="search-form">
    <a-form ref="searchForm" :model="searchParams" @finish="search" layout="inline">
      <#list table.indexes as column>
      <#include "/include/column/properties.ftl">
      <#if column.validStatus>
      <#elseif (column.select || column.fkSelect)>
      <a-form-item label="${columnComment}">
        <a-select v-model:value="searchParams.${fieldName}" :options="${fieldNameExceptKey}Options" allow-clear placeholder="全部" />
      </a-form-item>
      <#elseif column.pk>
      <a-form-item label="${columnComment}">
        <a-input<#if isInteger>-number</#if> v-model:value<#if isString>.trim</#if>="searchParams.${fieldName}" allow-clear />
      </a-form-item>
      <#elseif (isInteger || isDecimal)>
      <a-form-item label="${columnComment}">
        <a-input-number-ranger v-model:min="searchParams.${fieldName}Min" v-model:max="searchParams.${fieldName}Max" />
      </a-form-item>
      <#elseif (isString)>
      <a-form-item label="${columnComment}">
        <a-input v-model:value.trim="searchParams.${fieldName}Contains" allow-clear />
      </a-form-item>
      <#elseif (isDate)>
      <a-form-item label="${columnComment}">
        <a-range-picker v-model:value="searchParams.${fieldName}Range"
                        :placeholder="['开始日期', '结束日期']"
                        format="YYYY-MM-DD"
                        value-format="YYYY-MM-DD" />
      </a-form-item>
      <#elseif (isDateTime)>
      <a-form-item label="${columnComment}">
        <a-range-picker v-model:value="searchParams.${fieldName}Range"
                        :placeholder="['开始时间', '结束时间']"
                        show-time
                        format="YYYY-MM-DD HH:mm:ss"
                        value-format="YYYY-MM-DD HH:mm:ss" />
      </a-form-item>
      </#if>
      </#list>
      <a-form-item>
        <a-button type="primary" html-type="submit">
          <search-outlined />
          搜索
        </a-button>
        <a-button @click="resetSearch">重置</a-button>
      </a-form-item>
    </a-form>
  </div>
  <div class="list-table">
    <DataOperations :selected="selected"
                    :can-add="true" @add="add('添加${tableComment}')"
                    <#if hasValidStatusColumn>
                    :can-enable="true" @enable="() => enableSelected((item: any) => item.${validStatusFieldName} = 0)"
                    :can-disable="true" @disable="() => disableSelected((item: any) => item.${validStatusFieldName} = 1)"
                    <#else>
                    :can-enable="false"
                    :can-disable="false"
                    </#if>
                    :can-del="true" @del="delSelected" />
    <a-table :columns="columns" :data-source="dataPageList.list" :row-selection="rowSelection" :pagination="false"
             :row-class-name="(_record: any, index: number) => (index % 2 === 1 ? 'table-striped' : null)" row-key="id">
      <template #bodyCell="{ column, record, index }">
        <TableOperations v-if="column.key === 'action'"
                         @edit="() => edit('编辑${tableComment}', record, index)"
                         @copy="() => copy('复制${tableComment}', record)"
                         <#if hasValidStatusColumn>
                         :disabled="record.${validStatusFieldName}"
                         :can-enable="true" @enable="() => enable(record, () => record.${validStatusFieldName} = 0)"
                         :can-disable="true" @disable="() => disable(record, () => record.${validStatusFieldName} = 1)"
                         <#else>
                         :can-enable="false"
                         :can-disable="false"
                         </#if>
                         :can-del="true" @del="() => del(record)"
        />
      </template>
    </a-table>
    <a-pagination v-model:current="pageNum" v-model:pageSize="pageSize" :total="dataPageList.total"
                  :page-size-options="common.PageSizeOptions" show-size-changer show-quick-jumper />
  </div>
  <a-drawer v-model:open="upsertDrawerOpened" @close="closeUpsertDrawer" width="600px">
    <template #title>
      {{ drawerTitle }}
    </template>
    <${className}UpsertForm ref="detailFormRef" v-model="detail"
                            <#list table.selectColumns as column>
                            <#include "/include/column/properties.ftl">
                            :${fieldNameExceptKey}-options="${fieldNameExceptKey}Options"
                            </#list>
                            <#list table.fkSelectColumns as column>
                            <#include "/include/column/properties.ftl">
                            :${fieldNameExceptKey}-options="${fieldNameExceptKey}Options"
                            </#list>
                            @save="upsert">
      <template #buttons>
        <a-button style="margin-left: 10px" @click="() => closeUpsertDrawer()">取消</a-button>
      </template>
    </${className}UpsertForm>
  </a-drawer>

</template>

<style scoped></style>
