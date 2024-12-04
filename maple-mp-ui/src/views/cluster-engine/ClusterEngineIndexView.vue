<script lang="ts" setup>
import type { FormInstance } from "ant-design-vue"
import { reactive, onMounted, ref, useTemplateRef } from "vue"

import common from '@/composables/common'
import { listSearch, pageListSearch } from '@/composables/requests'
import { ClusterApis, ClusterEngineApis, DatasourceTypeApis } from "@/composables/service-apis"
import type { OperateCallback } from "@/composables/table-operations"
import { getMultiDataOperations, getSingleDataOperations } from "@/composables/table-operations"
import { getSelection } from '@/composables/table-selection'
import { useBreadcrumbStore } from "@/stores/breadcrumbs"

import DataOperations from "@/components/DataOperations.vue"
import TableOperations from "@/components/TableOperations.vue"
import ClusterEngineUpsertForm from "@/components/cluster-engine/ClusterEngineUpsertForm.vue"

const pkFields = ['id']

const searchParams = reactive<any>({
  id: undefined,
  clusterContains: undefined,
  nameContains: undefined,
  versionContains: undefined,
})

const {
  pageNum,
  pageSize,
  dataPageList,
  search,
  resetSearch
} = pageListSearch(ClusterEngineApis.pageList(), searchParams, useTemplateRef<FormInstance>("searchForm"))
const selection = getSelection()
const {selected, rowSelection} = selection

const clusterSearchParams = reactive<any>({
  deleted: 0
})
const {
  search: clusterSearch,
  dataList: clusterOptions
} = listSearch(ClusterApis.list(), clusterSearchParams, undefined, common.convertToOptions("name", "name"))

onMounted(() => {
  // 设置面包屑
  const {setBreadcrumb} = useBreadcrumbStore()
  setBreadcrumb([{text: '集群引擎'}])

  // 获取列表数据
  search()
  clusterSearch()
})

const columns = [
  { title: '引擎ID', dataIndex: 'id', key: 'id' },
  { title: '集群名称', dataIndex: 'cluster', key: 'cluster' },
  { title: '类型名称', dataIndex: 'name', key: 'name' },
  { title: '类型版本', dataIndex: 'version', key: 'version' },
  { title: '引擎目录', dataIndex: 'engineHome', key: 'engineHome' },
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
    detail.cluster = response.cluster
    detail.name = response.name
    detail.version = response.version
    detail.engineHome = response.engineHome
    detail.extInfo = response.extInfo
  },
  research: search,
  resetDetail: (detail: any) => {
    detail.id = undefined
    detail.cluster = ''
    detail.name = ''
    detail.version = ''
    detail.engineHome = ''
    detail.extInfo = ''
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
  del
} = getSingleDataOperations(ClusterEngineApis, callback)

const {
  title: drawerTitle,
  opened: upsertDrawerOpened,
  openDialog: showUpsertDrawer,
  closeDialog: closeUpsertDrawer
} = dialogOperations

const {
  delSelected
} = getMultiDataOperations(selection, ClusterEngineApis, callback)

</script>

<template>
  <div class="search-form">
    <a-form ref="searchForm" :model="searchParams" @finish="search" layout="inline">
      <a-form-item label="引擎ID">
        <a-input-number v-model:value="searchParams.id" allow-clear />
      </a-form-item>
      <a-form-item label="集群名称">
        <a-select v-model:value="searchParams.cluster" :options="clusterOptions" allow-clear placeholder="全部" style="width: 120px" />
      </a-form-item>
      <a-form-item label="类型名称">
        <a-input v-model:value.trim="searchParams.nameContains" allow-clear />
      </a-form-item>
      <a-form-item label="类型版本">
        <a-input v-model:value.trim="searchParams.versionContains" allow-clear />
      </a-form-item>
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
                    :can-add="true" @add="add('添加集群引擎')"
                    :can-enable="false"
                    :can-disable="false"
                    :can-del="true" @del="delSelected" />
    <a-table :columns="columns" :data-source="dataPageList.list" :row-selection="rowSelection" :pagination="false"
             :row-class-name="(_record: any, index: number) => (index % 2 === 1 ? 'table-striped' : null)" row-key="id">
      <template #bodyCell="{ column, record, index }">
        <TableOperations v-if="column.key === 'action'"
                         @edit="() => edit('编辑集群引擎', record, index)"
                         @copy="() => copy('复制集群引擎', record)"
                         :can-enable="false"
                         :can-disable="false"
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
    <ClusterEngineUpsertForm ref="detailFormRef" v-model="detail"
                             :cluster-options="clusterOptions"
                            @save="upsert">
      <template #buttons>
        <a-button style="margin-left: 10px" @click="() => closeUpsertDrawer()">取消</a-button>
      </template>
    </ClusterEngineUpsertForm>
  </a-drawer>

</template>

<style scoped></style>
