<script lang="ts" setup>
import type { FormInstance } from "ant-design-vue"
import { reactive, onMounted, ref, useTemplateRef } from "vue"

import common from '@/composables/common'
import { listSearch, pageListSearch } from '@/composables/requests'
import { SysConfApis } from "@/composables/service-apis"
import type { OperateCallback } from "@/composables/table-operations"
import { getMultiDataOperations, getSingleDataOperations } from "@/composables/table-operations"
import { getSelection } from '@/composables/table-selection'
import { useBreadcrumbStore } from "@/stores/breadcrumbs"

import DataOperations from "@/components/DataOperations.vue"
import TableOperations from "@/components/TableOperations.vue"
import SysConfUpsertForm from "@/components/sys-conf/SysConfUpsertForm.vue"

const pkFields = ['confKey']

const searchParams = reactive<any>({
  confKey: undefined,
})

const {
  pageNum,
  pageSize,
  dataPageList,
  search,
  resetSearch
} = pageListSearch(SysConfApis.pageList(), searchParams, useTemplateRef<FormInstance>("searchForm"))
const selection = getSelection()
const {selected, rowSelection} = selection

onMounted(() => {
  // 设置面包屑
  const {setBreadcrumb} = useBreadcrumbStore()
  setBreadcrumb([{text: '系统配置'}])
})

const columns = [
  { title: '配置键', dataIndex: 'confKey', key: 'confKey' },
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
    detail.confKey = response.confKey
    try {
      detail.confValue = JSON.stringify(JSON.parse(response.confValue), null, 4)
    } catch (err) {
      detail.confValue = response.confValue
    }
    detail.desc = response.desc
  },
  research: search,
  resetDetail: (detail: any) => {
    detail.confKey = ''
    detail.confValue = ''
    detail.desc = ''
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
} = getSingleDataOperations(SysConfApis, callback)

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
} = getMultiDataOperations(selection, SysConfApis, callback)

</script>

<template>
  <div class="search-form">
    <a-form ref="searchForm" :model="searchParams" layout="inline">
      <a-form-item label="配置键">
        <a-input v-model:value.trim="searchParams.confKey" allow-clear />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" @click="search">
          <search-outlined />
          搜索
        </a-button>
        <a-button @click="resetSearch">重置</a-button>
      </a-form-item>
    </a-form>
  </div>
  <div class="list-table">
    <DataOperations :selected="selected"
                    :can-add="true" @add="add('添加系统配置')"
                    :can-enable="true" @enable="() => enableSelected((item: any) => item.disabled = 0)"
                    :can-disable="true" @disable="() => disableSelected((item: any) => item.disabled = 1)"
                    :can-del="true" @del="delSelected" />
    <a-table :columns="columns" :data-source="dataPageList.list" :row-selection="rowSelection" :pagination="false"
             :row-class-name="(_record: any, index: number) => (index % 2 === 1 ? 'table-striped' : null)" row-key="id">
      <template #bodyCell="{ column, record, index }">
        <TableOperations v-if="column.key === 'action'"
                         @edit="() => edit('编辑系统配置', record, index)"
                         @copy="() => copy('复制系统配置', record)"
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
    <SysConfUpsertForm ref="detailFormRef" v-model="detail"
                            @save="upsert">
      <template #buttons>
        <a-button style="margin-left: 10px" @click="() => closeUpsertDrawer()">取消</a-button>
      </template>
    </SysConfUpsertForm>
  </a-drawer>

</template>

<style scoped></style>
