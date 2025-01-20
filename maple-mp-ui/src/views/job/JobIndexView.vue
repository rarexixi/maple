<script lang="ts" setup>
import type { FormInstance } from "ant-design-vue"
import { reactive, onMounted, useTemplateRef, computed } from "vue"
import { useRouter } from "vue-router"

import common from '@/composables/common'
import { listSearch, pageListSearch } from '@/composables/requests'
import { JobApis, ClusterEngineApis, ClusterApis } from "@/composables/service-apis"
import { useJobTypesStore } from "@/stores/sys-conf"
import type { OperateCallback } from "@/composables/table-operations"
import { getMultiDataOperations, getSingleDataOperations } from "@/composables/table-operations"
import { getSelection } from '@/composables/table-selection'
import { useBreadcrumbStore } from "@/stores/breadcrumbs"

import DataOperations from "@/components/DataOperations.vue"
import TableOperations from "@/components/TableOperations.vue"
import { useClusterEngineStore, useClusterStore } from "@/stores/sys-data";

const searchParams = reactive<any>({
  id: undefined,
  jobNameContains: undefined,
  jobType: undefined,
  owner: undefined,
  engineId: undefined,
})

const {
  pageNum,
  pageSize,
  dataPageList,
  search,
  resetSearch
} = pageListSearch(JobApis.pageList(), searchParams, useTemplateRef<FormInstance>("searchForm"))
const selection = getSelection()
const { selected, rowSelection } = selection

const clusterSearchParams = reactive<any>({
  deleted: 0
})
const {
  dataMap: clusterOptionMap
} = listSearch(ClusterApis.list(), clusterSearchParams, undefined, common.convertToOptions('id', 'name'), common.setOptionMap('id', 'name'))


const { dataList: engineList, dataMap: engineMap } = useClusterEngineStore()
const { dataMap: clusterMap } = useClusterStore()

const engineOptions = computed(() => {
  let result = new Map<string, any>()
  for (let engine of engineList.value) {
    if (result.has(engine.clusterId)) {
      result.get(engine.clusterId).push({ label: `${engine.name} ${engine.version}`, value: engine.id })
    } else {
      result.set(engine.clusterId, [{ label: `${engine.name} ${engine.version}`, value: engine.id }])
    }
  }
  return Array.from(result).map(([clusterId, engines]) => ({ label: clusterOptionMap[clusterId], options: engines }))
})

const {
  confOptions: jobTypeOptions,
  confOptionMap: jobTypeOptionMap
} = useJobTypesStore()

onMounted(() => {
  // 设置面包屑
  const { setBreadcrumb } = useBreadcrumbStore()
  setBreadcrumb([{ text: '执行作业' }])
})

function getEngineShowName(data: any) {
  let engine = engineMap[data.engineId]
  return `${engine?.name} ${engine?.version}`
}

function getClusterShowName(data: any) {
  let cluster = clusterMap[data.clusterId]
  return `${cluster?.name} (${cluster?.category})`
}

const columns = [
  { title: '作业ID', dataIndex: 'id', key: 'id' },
  { title: '作业名', dataIndex: 'jobName', key: 'jobName' },
  { title: '作业类型', dataIndex: 'jobType', key: 'jobType', customRender: (row: any) => jobTypeOptionMap[row.text] },
  { title: '来源应用', dataIndex: 'fromApp', key: 'fromApp' },
  { title: '集群', dataIndex: 'clusterId', key: 'clusterCategory', customRender: (row: any) => getClusterShowName(row.record) },
  { title: '引擎', dataIndex: 'engineId', key: 'engineId', customRender: (row: any) => getEngineShowName(row.record) },
  { title: '用户组', dataIndex: 'userGroup', key: 'userGroup' },
  { title: '作业负责人', dataIndex: 'owner', key: 'owner' },
  { title: '创建人', dataIndex: 'createdBy', key: 'createdBy' },
  { title: '修改人', dataIndex: 'updatedBy', key: 'updatedBy' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '更新时间', dataIndex: 'updatedAt', key: 'updatedAt' },
  {title: '操作', dataIndex: 'action', key: 'action', fixed: 'right', width: 120},
]

const callback: OperateCallback = {
  research: search,
}

const {
  enable,
  disable,
  del
} = getSingleDataOperations(JobApis, callback)

const router = useRouter()

const {
  enableSelected,
  disableSelected,
  delSelected
} = getMultiDataOperations(selection, JobApis, callback)

</script>

<template>
  <div class="search-form">
    <a-form ref="searchForm" :model="searchParams" labelAlign="left" :label-col="{style:{width:'80px'}}">
      <a-flex wrap="wrap">
        <a-form-item label="作业ID" class="form-item-240">
          <a-input v-model:value.number="searchParams.id" allow-clear />
        </a-form-item>
        <a-form-item label="作业名" class="form-item-240">
          <a-input v-model:value.trim="searchParams.jobNameContains" allow-clear />
        </a-form-item>
        <a-form-item label="作业类型" class="form-item-240">
          <a-select v-model:value="searchParams.jobType" :options="jobTypeOptions" allow-clear placeholder="全部" />
        </a-form-item>
        <a-form-item label="作业负责人" class="form-item-240">
          <a-input v-model:value.number="searchParams.owner" allow-clear />
        </a-form-item>
        <a-form-item label="引擎ID" class="form-item-240">
          <a-select v-model:value="searchParams.engineId" :options="engineOptions" allow-clear placeholder="全部" />
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
                    :can-add="false"
                    :can-enable="true" @enable="() => enableSelected((item: any) => item.disabled = 0)"
                    :can-disable="true" @disable="() => disableSelected((item: any) => item.disabled = 1)"
                    :can-del="true" @del="delSelected">
      <template #before>
        <a-dropdown>
          <template #overlay>
            <a-menu @click="(jobType: any) => router.push({name: 'jobAdd', params: {jobType: jobType.keyPath[0]}})">
              <a-menu-item v-for="item in jobTypeOptions" :key="item.value">{{ item.label }}</a-menu-item>
            </a-menu>
          </template>
          <a-button type="primary">
            <PlusOutlined />
            添加
            <DownOutlined />
          </a-button>
        </a-dropdown>
      </template>
    </DataOperations>
    <a-table :columns="columns" :data-source="dataPageList.list" :row-selection="rowSelection" :pagination="false"
             :row-class-name="(_record: any, index: number) => (index % 2 === 1 ? 'table-striped' : null)" row-key="id">
      <template #bodyCell="{ column, record, index }">
        <TableOperations v-if="column.key === 'action'"
                         @edit="() => router.push('job/edit/' + record.id)"
                         @copy="() => router.push('job/copy/' + record.id)"
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
</template>
