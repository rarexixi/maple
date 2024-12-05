<script lang="ts" setup>
import type { FormInstance } from "ant-design-vue"
import { reactive, onMounted, ref, useTemplateRef } from "vue"

import common from '@/composables/common'
import { listSearch, pageListSearch } from '@/composables/requests'
import { ClusterEngineApis, ClusterApis } from "@/composables/service-apis"
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
  cluster: undefined,
  name: undefined,
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
} = listSearch(ClusterApis.list(), clusterSearchParams, undefined, common.convertToOptions('id', 'name'))

const nameOptions = [
  {value: 'spark', label: 'Spark'},
  {value: 'flink', label: 'Flink'}
]

onMounted(() => {
  // 设置面包屑
  const {setBreadcrumb} = useBreadcrumbStore()
  setBreadcrumb([{text: '计算引擎'}])

  // 获取列表数据
  search()

  clusterSearch()
})

const columns = [
  {title: '引擎ID', dataIndex: 'id', key: 'id'},
  {title: '所属集群', dataIndex: 'cluster', key: 'cluster'},
  {title: '引擎名称', dataIndex: 'name', key: 'name'},
  {title: '引擎版本', dataIndex: 'version', key: 'version'},
  {title: '引擎目录', dataIndex: 'engineHome', key: 'engineHome'},
  {title: '创建人', dataIndex: 'createdBy', key: 'createdBy'},
  {title: '修改人', dataIndex: 'updatedBy', key: 'updatedBy'},
  {title: '创建时间', dataIndex: 'createdAt', key: 'createdAt'},
  {title: '更新时间', dataIndex: 'updatedAt', key: 'updatedAt'},
  {title: '操作', dataIndex: 'action', key: 'action', fixed: 'right', width: 120},
]

const callback: OperateCallback = {
  detail: (response: any, copyPk: boolean) => {
    if (copyPk) {
      for (const field of pkFields) {
        detail[field] = response[field]
      }
    }
    detail.cluster = response.cluster + ''
    detail.name = response.name
    detail.version = response.version
    detail.engineHome = response.engineHome
    detail.engineConf = response.engineConf
  },
  research: search,
  resetDetail: (detail: any) => {
    detail.id = undefined
    detail.cluster = ''
    detail.name = ''
    detail.version = ''
    detail.engineHome = ''
    detail.engineConf = {}
  },
  setItem: (detail: any, editIndex: number) => {
    dataPageList.list[editIndex] = detail
  }
}

const {
  dialogOperations,
  detail,
  get: getDetail,
  add: showAdd,
  copy,
  edit,
  upsert,
  enable,
  disable,
  del
} = getSingleDataOperations(ClusterEngineApis, callback)

function add(engine: any) {
  showAdd('添加计算引擎')
  detail.name = engine.keyPath[0]
}

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
} = getMultiDataOperations(selection, ClusterEngineApis, callback)

</script>

<template>
  <div class="search-form">
    <a-form ref="searchForm" :model="searchParams" @finish="search" layout="inline">
      <a-form-item label="引擎ID">
        <a-input-number v-model:value="searchParams.id" allow-clear />
      </a-form-item>
      <a-form-item label="所属集群">
        <a-select v-model:value="searchParams.cluster" :options="clusterOptions" allow-clear placeholder="全部" style="min-width: 150px" />
      </a-form-item>
      <a-form-item label="引擎名称">
        <a-select v-model:value="searchParams.name" :options="nameOptions" allow-clear placeholder="全部" style="min-width: 120px" />
      </a-form-item>
      <a-form-item label="引擎版本">
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
                    :can-add="false" @add="add('添加计算引擎')"
                    :can-enable="true" @enable="() => enableSelected((item: any) => item.disabled = 0)"
                    :can-disable="true" @disable="() => disableSelected((item: any) => item.disabled = 1)"
                    :can-del="true" @del="delSelected">
      <template #before>
        <a-dropdown>
          <template #overlay>
            <a-menu @click="add">
              <a-menu-item v-for="item in nameOptions" :key="item.value">{{ item.label }}</a-menu-item>
            </a-menu>
          </template>
          <a-button type="primary">
            <plus-outlined />
            添加
            <down-outlined />
          </a-button>
        </a-dropdown>
      </template>
    </DataOperations>
    <a-table :columns="columns" :data-source="dataPageList.list" :row-selection="rowSelection" :pagination="false"
             :row-class-name="(_record: any, index: number) => (index % 2 === 1 ? 'table-striped' : null)" row-key="id">
      <template #bodyCell="{ column, record, index }">
        <TableOperations v-if="column.key === 'action'"
                         @edit="() => edit('编辑计算引擎', record, index)"
                         @copy="() => copy('复制计算引擎', record)"
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
      {{ drawerTitle }} {{ detail.name }}
    </template>
    <ClusterEngineUpsertForm ref="detailFormRef" v-model="detail"
                             :name-options="nameOptions"
                             :cluster-options="clusterOptions"
                             @save="upsert">
      <template #buttons>
        <a-button style="margin-left: 10px" @click="() => closeUpsertDrawer()">取消</a-button>
      </template>
    </ClusterEngineUpsertForm>
  </a-drawer>

</template>

<style scoped></style>
