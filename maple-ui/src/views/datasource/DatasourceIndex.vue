<template>
  <a-breadcrumb separator="/">
    <a-breadcrumb-item>数据源配置管理</a-breadcrumb-item>
  </a-breadcrumb>
  <div class="search-form">
    <a-form ref="searchForm" :model="searchParams" @finish="search" layout="inline">
      <a-form-item label="Id">
        <a-input-number v-model:value="searchParams.id" allow-clear />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value.trim="searchParams.nameContains" allow-clear />
      </a-form-item>
      <a-form-item label="类型">
        <a-select v-model:value="searchParams.datasourceType" allow-clear placeholder="全部" style="width: 120px">
          <template v-for="(item, index) in datasourceTypeSelectList" :key="index">
            <a-select-option :value="item.typeCode">{{ item.typeName }}</a-select-option>
          </template>
        </a-select>
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit"><search-outlined />搜索</a-button>
      </a-form-item>
    </a-form>
  </div>
  <div class="list-table">
    <div class="operation-btns">
      <a-dropdown>
        <template #overlay>
          <a-menu @click="add">
            <a-sub-menu v-for="item in datasourceTypeSelectList" :key="item.typeCode" :title="item.typeName">
              <a-menu-item v-for="k in item.versions" :key="k">{{ k }}</a-menu-item>
            </a-sub-menu>
          </a-menu>
        </template>
        <a-button type="primary"><plus-outlined />添加<down-outlined /></a-button>
      </a-dropdown>
      <template v-if="selectedRowKeys.length > 0">
        <a-button @click="enableSelected" type="success"><check-outlined />启用</a-button>
        <a-button @click="disableSelected" type="warning"><stop-outlined />禁用</a-button>
        <a-button @click="deleteSelected" type="danger"><delete-outlined />删除</a-button>
      </template>
    </div>
    <a-table :columns="columns" :data-source="dataPageList.list" :row-selection="rowSelection" :pagination="false"
      :row-class-name="(_record: any, index: number) => (index % 2 === 1 ? 'table-striped' : null)" row-key="id">
      <template #bodyCell="{ column, record, index }">
        <div class="table-operations" v-if="column.key === 'action'">
          <a-popconfirm :title="`确定${record.deleted === 1 ? '启用' : '禁用'}吗？`" @confirm="() => switchDeleted(record)">
            <a v-if="record.deleted" class="enable"><check-outlined /><stop-outlined /></a>
            <a v-else class="disable"><check-outlined /><stop-outlined /></a>
          </a-popconfirm>
          <a @click="() => edit(record, index)" class="text-primary"><edit-outlined /></a>
          <a @click="() => edit(record, index, true)" class="text-primary"><copy-outlined /></a>
          <a-popconfirm title="确定删除吗？" ok-text="确定" cancel-text="取消" @confirm="() => del(record)">
            <a class="text-danger"><delete-outlined /></a>
          </a-popconfirm>
        </div>
      </template>
    </a-table>
    <a-pagination v-model:current="pageNum" v-model:pageSize="pageSize" :total="dataPageList.total"
      :page-size-options="pageSizeOptions" show-size-changer show-quick-jumper size="small"></a-pagination>
  </div>
  <datasource-add-or-edit :pk="editPk" :type-version="typeVersion" :visible="addOrEditDrawerVisible"
    :operateType="operateType" @save="save" />
</template>

<script lang="ts">
import { defineComponent, reactive, provide, onMounted } from "vue"
import common from '@/composables/common'
import { listSearch, pageListSearch, execSelected } from '@/composables/requests'
import DatasourceAddOrEdit from './DatasourceAddOrEdit.vue'
import { getSelection } from './composables/datasourceSelect'
import { getOperations } from './composables/datasourceOperate'

const pksField = 'idIn'


export default defineComponent({
  components: { DatasourceAddOrEdit },
  name: "DatasourceIndex",
  setup() {
    const searchParams = reactive<any>({
      id: undefined,
      nameContains: '',
      datasourceTypeContains: '',
    })

    const { pageNum, pageSize, dataPageList, search } = pageListSearch({ url: '/datasource/page-list', method: 'GET' }, searchParams)
    const { rowSelection, selectedRowKeys, selectedRows, emptySelected } = getSelection(dataPageList)

    const datasourceTypeSearchParams = reactive<any>({
      deleted: 0
    })
    const typeMap = reactive<any>({})
    const convertList = (list: any[]) => list.map(item => {
      typeMap[item.typeCode] = item.typeName
      return {
        ...item,
        versions: item.versions.split(",").map((v: string) => v.trim())
      }
    })
    const getDatasourceTypeSelectList = listSearch({ url: '/datasource-type/list', method: 'GET' }, datasourceTypeSearchParams, convertList)

    const { editPk, typeVersion, addOrEditDrawerVisible, operateType, add, del, edit, switchDeleted, save } = getOperations(dataPageList, search)
    provide('closeAddOrEditDrawer', () => addOrEditDrawerVisible.value = false)
    provide('typeMap', typeMap)

    onMounted(() => {
      search()
      getDatasourceTypeSelectList.search()
    })


    const columns = [
      { title: 'ID', dataIndex: 'id', key: 'id' },
      { title: '名称', dataIndex: 'name', key: 'name' },
      { title: '描述', dataIndex: 'description', key: 'description' },
      { title: '类型', dataIndex: 'datasourceType', key: 'datasourceType', customRender: (data: any) => `${typeMap[data.record.datasourceType]} ${data.record.version}` },
      { title: '操作', dataIndex: 'action', key: 'action', fixed: 'right', width: 160 },
    ]

    return {
      columns,
      rowSelection,
      selectedRowKeys,
      selectedRows,
      pageSizeOptions: common.PageSizeOptions,
      datasourceTypeSelectList: getDatasourceTypeSelectList.dataList,
      searchParams, pageNum, pageSize,
      search,
      dataPageList,
      operateType,
      addOrEditDrawerVisible,
      editPk,
      typeVersion,
      add,
      edit,
      save,
      switchDeleted,
      del,
      disableSelected: execSelected({ url: '/datasource/disable', method: 'PATCH' }, selectedRowKeys, pksField, '禁用', () => {
        selectedRows.value.forEach((item: any) => item.deleted = 1)
        emptySelected()
      }),
      enableSelected: execSelected({ url: '/datasource/enable', method: 'PATCH' }, selectedRowKeys, pksField, '启用', () => {
        selectedRows.value.forEach((item: any) => item.deleted = 0)
        emptySelected()
      }),
      deleteSelected: execSelected({ url: '/datasource/delete', method: 'DELETE' }, selectedRowKeys, pksField, '删除', () => {
        search()
        emptySelected()
      })
    }
  }
})
</script>

<style scoped></style>
