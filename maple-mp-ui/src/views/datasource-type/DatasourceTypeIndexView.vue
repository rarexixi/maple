<script lang="ts" setup>
import type { FormInstance } from "ant-design-vue"
import { reactive, onMounted, useTemplateRef } from "vue"

import common from '@/composables/common'
import { pageListSearch } from '@/composables/requests'
import { DatasourceTypeApis } from "@/composables/service-apis"
import type { OperateCallback } from "@/composables/table-operations"
import { getMultiDataOperations, getSingleDataOperations } from "@/composables/table-operations"
import { getSelection } from '@/composables/table-selection'
import { useBreadcrumbStore } from "@/stores/breadcrumbs"

import DataOperations from "@/components/DataOperations.vue"
import TableOperations from "@/components/TableOperations.vue"
import DatasourceTypeUpsertForm from "@/components/datasource-type/DatasourceTypeUpsertForm.vue"

const pkFields = ['typeCode']

const searchParams = reactive<any>({
  typeCode: undefined,
})

const {
  pageNum,
  pageSize,
  dataPageList,
  search,
  resetSearch
} = pageListSearch(DatasourceTypeApis.pageList(), searchParams, useTemplateRef<FormInstance>("searchForm"))
const selection = getSelection()
const {selected, rowSelection} = selection

onMounted(() => {
  // 设置面包屑
  const {setBreadcrumb} = useBreadcrumbStore()
  setBreadcrumb([{text: '数据源类型'}])
})

const columns = [
  { title: '类型编码', dataIndex: 'typeCode', key: 'typeCode' },
  { title: '类型名称', dataIndex: 'typeName', key: 'typeName' },
  { title: '图标地址', dataIndex: 'icon', key: 'icon' },
  { title: '分类', dataIndex: 'classifier', key: 'classifier' },
  { title: '版本', dataIndex: 'versions', key: 'versions' },
  { title: '创建人', dataIndex: 'createdBy', key: 'createdBy' },
  { title: '修改人', dataIndex: 'updatedBy', key: 'updatedBy' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '更新时间', dataIndex: 'updatedAt', key: 'updatedAt' },
  {title: '操作', dataIndex: 'action', key: 'action', fixed: 'right', width: 120},
]

const callback: OperateCallback = {
  detail: (response: any, copyPk: boolean) => {
    if (copyPk) {
      for (const field of pkFields) {
        detail[field] = response[field]
      }
    }
    detail.typeCode = response.typeCode
    detail.typeName = response.typeName
    detail.icon = response.icon
    detail.classifier = response.classifier
    detail.versions = response.versions
    detail.configurations = response.configurations
  },
  research: search,
  resetDetail: (detail: any) => {
    detail.typeCode = ''
    detail.typeName = ''
    detail.icon = ''
    detail.classifier = ''
    detail.versions = ''
    detail.configurations = []
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
  enable,
  disable,
  del
} = getSingleDataOperations(DatasourceTypeApis, callback)

const {
  title: drawerTitle,
  opened: upsertDrawerOpened,
  openDialog: showUpsertDrawer,
  closeDialog: closeUpsertDrawer
} = dialogOperations

const {
  enableSelected,
  disableSelected,
  delSelected
} = getMultiDataOperations(selection, DatasourceTypeApis, callback)

</script>

<template>
  <div class="search-form">
    <a-form ref="searchForm" :model="searchParams" layout="inline">
      <a-form-item label="类型编码">
        <a-input v-model:value.trim="searchParams.typeCode" allow-clear />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="search">
          <SearchOutlined />
          搜索
        </a-button>
        <a-button @click="resetSearch">重置</a-button>
      </a-form-item>
    </a-form>
  </div>
  <div class="list-table">
    <DataOperations :selected="selected"
                    :can-add="true" @add="add('添加数据源类型')"
                    :can-enable="true" @enable="() => enableSelected((item: any) => item.disabled = 0)"
                    :can-disable="true" @disable="() => disableSelected((item: any) => item.disabled = 1)"
                    :can-del="true" @del="delSelected" />
    <a-table :columns="columns" :data-source="dataPageList.list" :row-selection="rowSelection" :pagination="false"
             :row-class-name="(_record: any, index: number) => (index % 2 === 1 ? 'table-striped' : null)" row-key="id">
      <template #bodyCell="{ column, record, index }">
        <TableOperations v-if="column.key === 'action'"
                         @edit="() => edit('编辑数据源类型', record, index)"
                         @copy="() => copy('复制数据源类型', record)"
                         :disabled="record.disabled"
                         :can-enable="true" @enable="() => enable(record, () => record.disabled = 0)"
                         :can-disable="true" @disable="() => disable(record, () => record.disabled = 1)"
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
    <DatasourceTypeUpsertForm ref="detailFormRef" v-model="detail"
                            @save="upsert">
      <template #buttons>
        <a-button style="margin-left: 10px" @click="() => closeUpsertDrawer()">取消</a-button>
      </template>
    </DatasourceTypeUpsertForm>
  </a-drawer>

</template>

<style scoped></style>
