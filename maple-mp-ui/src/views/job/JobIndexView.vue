<script lang="ts" setup>
import type { FormInstance } from "ant-design-vue"
import { reactive, onMounted, ref, useTemplateRef } from "vue"

import common from '@/composables/common'
import { listSearch, pageListSearch } from '@/composables/requests'
import { JobApis } from "@/composables/service-apis"
import type { OperateCallback } from "@/composables/table-operations"
import { getMultiDataOperations, getSingleDataOperations } from "@/composables/table-operations"
import { getSelection } from '@/composables/table-selection'
import { useBreadcrumbStore } from "@/stores/breadcrumbs"

import DataOperations from "@/components/DataOperations.vue"
import TableOperations from "@/components/TableOperations.vue"
import JobUpsertForm from "@/components/job/JobUpsertForm.vue"

const pkFields = ['id']

const searchParams = reactive<any>({
  id: undefined,
  jobNameContains: undefined,
  jobTypeContains: undefined,
  clusterCategoryContains: undefined,
  engineCategoryContains: undefined,
  engineVersionContains: undefined,
  ownerContains: undefined,
})

const {
  pageNum,
  pageSize,
  dataPageList,
  search,
  resetSearch
} = pageListSearch(JobApis.pageList(), searchParams, useTemplateRef<FormInstance>("searchForm"))
const selection = getSelection()
const {selected, rowSelection} = selection

onMounted(() => {
  // 设置面包屑
  const {setBreadcrumb} = useBreadcrumbStore()
  setBreadcrumb([{text: '执行作业'}])

  // 获取列表数据
  search()

})

const columns = [
  { title: '作业ID', dataIndex: 'id', key: 'id' },
  { title: '作业名', dataIndex: 'jobName', key: 'jobName' },
  { title: '作业类型', dataIndex: 'jobType', key: 'jobType' },
  { title: '集群种类', dataIndex: 'clusterCategory', key: 'clusterCategory' },
  { title: '引擎种类', dataIndex: 'engineCategory', key: 'engineCategory' },
  { title: '引擎版本', dataIndex: 'engineVersion', key: 'engineVersion' },
  { title: '作业负责人', dataIndex: 'owner', key: 'owner' },
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
    detail.jobName = response.jobName
    detail.desc = response.desc
    detail.jobType = response.jobType
    detail.clusterCategory = response.clusterCategory
    detail.engineCategory = response.engineCategory
    detail.engineVersion = response.engineVersion
    detail.owner = response.owner
    detail.runContent = response.runContent
    detail.jobConf = response.jobConf
  },
  research: search,
  resetDetail: (detail: any) => {
    detail.id = undefined
    detail.jobName = ''
    detail.desc = ''
    detail.jobType = ''
    detail.clusterCategory = ''
    detail.engineCategory = ''
    detail.engineVersion = ''
    detail.owner = ''
    detail.runContent = ''
    detail.jobConf = ''
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
} = getSingleDataOperations(JobApis, callback)

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
} = getMultiDataOperations(selection, JobApis, callback)

</script>

<template>
  <div class="search-form">
    <a-form ref="searchForm" :model="searchParams" @finish="search" layout="inline">
      <a-form-item label="作业ID">
        <a-input-number v-model:value="searchParams.id" allow-clear />
      </a-form-item>
      <a-form-item label="作业名">
        <a-input v-model:value.trim="searchParams.jobNameContains" allow-clear />
      </a-form-item>
      <a-form-item label="作业类型">
        <a-input v-model:value.trim="searchParams.jobTypeContains" allow-clear />
      </a-form-item>
      <a-form-item label="集群种类">
        <a-input v-model:value.trim="searchParams.clusterCategoryContains" allow-clear />
      </a-form-item>
      <a-form-item label="引擎种类">
        <a-input v-model:value.trim="searchParams.engineCategoryContains" allow-clear />
      </a-form-item>
      <a-form-item label="引擎版本">
        <a-input v-model:value.trim="searchParams.engineVersionContains" allow-clear />
      </a-form-item>
      <a-form-item label="作业负责人">
        <a-input v-model:value.trim="searchParams.ownerContains" allow-clear />
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
                    :can-add="true" @add="add('添加执行作业')"
                    :can-enable="true" @enable="() => enableSelected((item: any) => item.disabled = 0)"
                    :can-disable="true" @disable="() => disableSelected((item: any) => item.disabled = 1)"
                    :can-del="true" @del="delSelected" />
    <a-table :columns="columns" :data-source="dataPageList.list" :row-selection="rowSelection" :pagination="false"
             :row-class-name="(_record: any, index: number) => (index % 2 === 1 ? 'table-striped' : null)" row-key="id">
      <template #bodyCell="{ column, record, index }">
        <TableOperations v-if="column.key === 'action'"
                         @edit="() => edit('编辑执行作业', record, index)"
                         @copy="() => copy('复制执行作业', record)"
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
    <JobUpsertForm ref="detailFormRef" v-model="detail"
                            @save="upsert">
      <template #buttons>
        <a-button style="margin-left: 10px" @click="() => closeUpsertDrawer()">取消</a-button>
      </template>
    </JobUpsertForm>
  </a-drawer>

</template>

<style scoped></style>
