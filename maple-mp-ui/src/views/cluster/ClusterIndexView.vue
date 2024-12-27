<script lang="ts" setup>
import type { FormInstance } from "ant-design-vue"
import { reactive, onMounted, useTemplateRef } from "vue"

import common from '@/composables/common'
import { pageListSearch } from '@/composables/requests'
import { ClusterApis } from "@/composables/service-apis"
import type { OperateCallback } from "@/composables/table-operations"
import { getMultiDataOperations, getSingleDataOperations } from "@/composables/table-operations"
import { getSelection } from '@/composables/table-selection'
import { useBreadcrumbStore } from "@/stores/breadcrumbs"

import DataOperations from "@/components/DataOperations.vue"
import TableOperations from "@/components/TableOperations.vue"
import ClusterUpsertForm from "@/components/cluster/ClusterUpsertForm.vue"
import { useClusterCategoriesStore } from "@/stores/sys-conf"

const pkFields = ['id']

const searchParams = reactive<any>({
  id: undefined,
  addressContains: undefined,
})

const {
  pageNum,
  pageSize,
  dataPageList,
  search,
  resetSearch
} = pageListSearch(ClusterApis.pageList(), searchParams, useTemplateRef<FormInstance>("searchForm"))
const selection = getSelection()
const { selected, rowSelection } = selection

const { confOptions: categoryOptions } = useClusterCategoriesStore()

onMounted(() => {
  // 设置面包屑
  const { setBreadcrumb } = useBreadcrumbStore()
  setBreadcrumb([{ text: '集群' }])
})

const columns = [
  { title: '集群ID', dataIndex: 'id', key: 'id' },
  { title: '集群名称', dataIndex: 'name', key: 'name' },
  { title: '集群种类', dataIndex: 'category', key: 'category' },
  { title: '集群地址', dataIndex: 'address', key: 'address' },
  { title: '创建人', dataIndex: 'createdBy', key: 'createdBy' },
  { title: '修改人', dataIndex: 'updatedBy', key: 'updatedBy' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '更新时间', dataIndex: 'updatedAt', key: 'updatedAt' },
  { title: '操作', dataIndex: 'action', key: 'action', fixed: 'right', width: 120 },
]

const callback: OperateCallback = {
  detail: (response: any, copyPk: boolean) => {
    if (copyPk) {
      for (const field of pkFields) {
        detail[field] = response[field]
      }
    }
    detail.name = response.name
    detail.category = response.category
    detail.address = response.address
    detail.description = response.description
    detail.clusterConf = response.clusterConf
    if (response.category == 'K8s') {
      detail.clusterConf = { config: { clientCertData: '', ...response.clusterConf.config } }
    } else {
      detail.clusterConf = { ...response.clusterConf }
    }
  },
  research: search,
  resetDetail: (detail: any) => {
    detail.id = undefined
    detail.name = ''
    detail.category = ''
    detail.address = ''
    detail.description = ''
    detail.clusterConf = ''
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
} = getSingleDataOperations(ClusterApis, callback)

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
} = getMultiDataOperations(selection, ClusterApis, callback)

</script>

<template>
  <div class="search-form">
    <a-form ref="searchForm" :model="searchParams" labelAlign="left" :label-col="{style:{width:'70px'}}">
      <a-flex>
        <a-form-item label="集群ID" class="form-item-240">
          <a-input v-model:value.number="searchParams.id" allow-clear />
        </a-form-item>
        <a-form-item label="集群地址" class="form-item-240">
          <a-input v-model:value.trim="searchParams.addressContains" allow-clear />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" @click="search">
            <SearchOutlined />
            搜索
          </a-button>
          <a-button @click="resetSearch">重置</a-button>
        </a-form-item>
      </a-flex>
    </a-form>
  </div>
  <div class="list-table">
    <DataOperations :selected="selected"
                    :can-add="true" @add="add('添加集群')"
                    :can-enable="true" @enable="() => enableSelected((item: any) => item.disabled = 0)"
                    :can-disable="true" @disable="() => disableSelected((item: any) => item.disabled = 1)"
                    :can-del="true" @del="delSelected" />
    <a-table :columns="columns" :data-source="dataPageList.list" :row-selection="rowSelection" :pagination="false"
             :row-class-name="(_record: any, index: number) => (index % 2 === 1 ? 'table-striped' : null)" row-key="id">
      <template #bodyCell="{ column, record, index }">
        <TableOperations v-if="column.key === 'action'"
                         @edit="() => edit('编辑集群', record, index)"
                         @copy="() => copy('复制集群', record)"
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
    <ClusterUpsertForm ref="detailFormRef" v-model="detail"
                       :category-options="categoryOptions"
                       @save="upsert">
      <template #buttons>
        <a-button style="margin-left: 10px" @click="() => closeUpsertDrawer()">取消</a-button>
      </template>
    </ClusterUpsertForm>
  </a-drawer>

</template>

<style scoped></style>
