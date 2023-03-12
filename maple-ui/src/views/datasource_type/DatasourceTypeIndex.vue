<template>
  <a-breadcrumb separator="/">
    <a-breadcrumb-item>数据源类型管理</a-breadcrumb-item>
  </a-breadcrumb>
  <div class="search-form">
    <a-form ref="searchForm" :model="searchParams" @finish="search" layout="inline">
      <a-form-item label="类型编码">
        <a-input v-model:value.trim="searchParams.typeCode" allow-clear />
      </a-form-item>
      <a-form-item>
        <a-button type="primary" html-type="submit"><search-outlined />搜索</a-button>
      </a-form-item>
    </a-form>
  </div>
  <div class="list-table">
    <div class="operation-btns">
      <a-button @click="add" type="primary"><plus-outlined />添加</a-button>
      <template v-if="selectedRowKeys.length > 0">
        <a-button @click="enableSelected" type="success"><check-outlined />启用</a-button>
        <a-button @click="disableSelected" type="warning"><stop-outlined />禁用</a-button>
        <a-button @click="deleteSelected" type="danger"><delete-outlined />删除</a-button>
      </template>
    </div>
    <a-table :columns="columns" :data-source="dataList" :row-selection="rowSelection" :scroll="{ x: 1300 }"
      :pagination="false" :row-class-name="(_record: any, index: number) => (index % 2 === 1 ? 'table-striped' : null)"
      row-key="typeCode" size="small">
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
  </div>
  <datasource-type-add-or-edit :pk="editPk" :visible="addOrEditDrawerVisible" :operateType="operateType" @save="save" />
</template>

<script lang="ts">
import { defineComponent, reactive, provide, onMounted, unref } from "vue"
import common from '@/composables/common'
import { execSelected, listSearch } from '@/composables/requests'
import DatasourceTypeAddOrEdit from './DatasourceTypeAddOrEdit.vue'
import { getSelection } from './composables/datasourceTypeSelect'
import { getOperations } from './composables/datasourceTypeOperate'

const columns = [
  { title: '类型编码', dataIndex: 'typeCode', key: 'typeCode' },
  { title: '类型名称', dataIndex: 'typeName', key: 'typeName' },
  { title: '图标地址', dataIndex: 'icon', key: 'icon' },
  { title: '分类', dataIndex: 'classifier', key: 'classifier' },
  { title: '版本', dataIndex: 'versions', key: 'versions' },
  { title: '操作', dataIndex: 'action', key: 'action', fixed: 'right', width: 160 },
]

const pksField = 'typeCodes'


export default defineComponent({
  components: { DatasourceTypeAddOrEdit },
  name: "DatasourceTypeIndex",
  setup() {
    const searchParams = reactive<any>({
      typeCode: '',
    })

    const { dataList, search } = listSearch({ url: '/datasource-type/list', method: 'GET' }, searchParams)
    const { rowSelection, selectedRowKeys, selectedRows, emptySelected } = getSelection(unref(dataList))


    const { editPk, addOrEditDrawerVisible, operateType, add, del, edit, switchDeleted, save } = getOperations(unref(dataList), search)
    provide('closeAddOrEditDrawer', () => addOrEditDrawerVisible.value = false)

    onMounted(() => {
      search()
    })

    return {
      columns,
      rowSelection,
      selectedRowKeys,
      selectedRows,
      pageSizeOptions: common.PageSizeOptions,
      searchParams,
      search,
      dataList,
      operateType,
      addOrEditDrawerVisible,
      editPk,
      add,
      edit,
      save,
      switchDeleted,
      del,
      disableSelected: execSelected({ url: '/datasource-type/disable', method: 'PATCH' }, selectedRowKeys, pksField, '禁用', () => {
        selectedRows.value.forEach((item: any) => item.deleted = 1)
        emptySelected()
      }),
      enableSelected: execSelected({ url: '/datasource-type/enable', method: 'PATCH' }, selectedRowKeys, pksField, '启用', () => {
        selectedRows.value.forEach((item: any) => item.deleted = 0)
        emptySelected()
      }),
      deleteSelected: execSelected({ url: '/datasource-type/delete', method: 'DELETE' }, selectedRowKeys, pksField, '删除', () => {
        search()
        emptySelected()
      })
    }
  }
})
</script>

<style scoped></style>
