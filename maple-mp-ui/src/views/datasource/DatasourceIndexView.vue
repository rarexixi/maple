<script lang="ts" setup>
import type { FormInstance } from "ant-design-vue"
import { reactive, onMounted, ref, useTemplateRef } from "vue"

import common from '@/composables/common'
import { listSearch, pageListSearch } from '@/composables/requests'
import { DatasourceApis, DatasourceTypeApis } from "@/composables/service-apis"
import type { OperateCallback } from "@/composables/table-operations"
import { getMultiDataOperations, getSingleDataOperations } from "@/composables/table-operations"
import { getSelection } from '@/composables/table-selection'
import { useBreadcrumbStore } from "@/stores/breadcrumbs"
import { request } from "@/utils/request-utils";

import DataOperations from "@/components/DataOperations.vue"
import TableOperations from "@/components/TableOperations.vue"
import DatasourceUpsertForm from "@/components/datasource/DatasourceUpsertForm.vue"

const pkFields = ['id']

const searchParams = reactive<any>({
  id: undefined,
  nameContains: undefined,
  datasourceType: undefined,
})

const {
  pageNum,
  pageSize,
  dataPageList,
  search,
  resetSearch
} = pageListSearch(DatasourceApis.pageList(), searchParams, useTemplateRef<FormInstance>("searchForm"))
const selection = getSelection()
const {selected, rowSelection} = selection

const datasourceTypeSearchParams = reactive<any>({
})
const typeMap = reactive<any>({})
const convertList = (list: any[]) => list.map(item => {
  typeMap[item.typeCode] = item.typeName
  return {
    value: item.typeCode,
    label: item.typeName,
    versions: item.versions.split(",").map((v: string) => v.trim())
  }
})
const {
  dataList: datasourceTypeOptions
} = listSearch(DatasourceTypeApis.list(), datasourceTypeSearchParams, undefined, convertList)

onMounted(() => {
  // 设置面包屑
  const {setBreadcrumb} = useBreadcrumbStore()
  setBreadcrumb([{text: '数据源'}])
})

const columns = [
  { title: 'Id', dataIndex: 'id', key: 'id' },
  { title: '数据源名称', dataIndex: 'name', key: 'name' },
  { title: '数据源描述', dataIndex: 'description', key: 'description' },
  { title: '数据源类型', dataIndex: 'datasourceType', key: 'datasourceType' },
  { title: '数据源版本', dataIndex: 'version', key: 'version' },
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
    detail.name = response.name
    detail.description = response.description
    detail.datasourceType = response.datasourceType
    detail.version = response.version
    getDatasourceTypeDetail(response.datasourceConf)
  },
  research: search,
  resetDetail: (detail: any) => {
    detail.id = undefined
    detail.name = ''
    detail.description = ''
    detail.datasourceType = ''
    detail.version = ''
    detail.datasourceConf = {}
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
} = getSingleDataOperations(DatasourceApis, callback)

function add(dsType: any) {
  showAdd('添加数据源')
  detail.datasourceType = dsType.keyPath[0]
  detail.version = dsType.keyPath[1]
  getDatasourceTypeDetail({})
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
} = getMultiDataOperations(selection, DatasourceApis, callback)


const confOptions = ref([] as any[])
const getDatasourceTypeDetail = (datasourceConf: any) => {
  if (!detail.datasourceType) {
    confOptions.value = []
    return;
  }
  let datasourceTypePk = {
    typeCode: detail.datasourceType
  }
  request(DatasourceTypeApis.detail(datasourceTypePk)).then(response => {
    confOptions.value = (response.configurations || []).filter((k: any) => k.versions == '*' || k.versions.includes(detail.version))
    for (let k of confOptions.value) {
      detail.datasourceConf[k.keyCode] = datasourceConf[k.keyCode] || k.defaultValue || undefined
    }
  })
}
</script>

<template>
  <div class="search-form">
    <a-form ref="searchForm" :model="searchParams" layout="inline">
      <a-form-item label="Id">
        <a-input-number v-model:value="searchParams.id" allow-clear />
      </a-form-item>
      <a-form-item label="数据源名称">
        <a-input v-model:value.trim="searchParams.nameContains" allow-clear />
      </a-form-item>
      <a-form-item label="数据源类型">
        <a-select v-model:value="searchParams.datasourceType" :options="datasourceTypeOptions" allow-clear placeholder="全部" style="width: 120px" />
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
                    :can-add="false"
                    :can-enable="true" @enable="() => enableSelected((item: any) => item.disabled = 0)"
                    :can-disable="true" @disable="() => disableSelected((item: any) => item.disabled = 1)"
                    :can-del="true" @del="delSelected">
      <template #before>
        <a-dropdown>
          <template #overlay>
            <a-menu @click="add">
              <a-sub-menu v-for="item in datasourceTypeOptions" :key="item.value" :title="item.label">
                <a-menu-item v-for="k in item.versions" :key="k">{{ k }}</a-menu-item>
              </a-sub-menu>
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
                         @edit="() => edit('编辑数据源', record, index)"
                         @copy="() => copy('复制数据源', record)"
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
      <a-typography-text type="secondary">({{ detail.datasourceType }} {{ detail.version }})</a-typography-text>
    </template>
    <DatasourceUpsertForm ref="detailFormRef" v-model="detail"
                            :datasourceTypeOptions="datasourceTypeOptions"
                            :confOptions="confOptions"
                            @save="upsert">
      <template #buttons>
        <a-button style="margin-left: 10px" @click="() => closeUpsertDrawer()">取消</a-button>
      </template>
    </DatasourceUpsertForm>
  </a-drawer>

</template>

<style scoped></style>
