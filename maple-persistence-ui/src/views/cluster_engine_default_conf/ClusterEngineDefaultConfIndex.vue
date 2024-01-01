<template>
    <a-breadcrumb separator="/">
        <a-breadcrumb-item>集群引擎默认配置管理</a-breadcrumb-item>
        <a-breadcrumb-item>集群引擎默认配置列表</a-breadcrumb-item>
    </a-breadcrumb>
    <div class="search-form">
        <a-form ref="searchForm" :model="searchParams" @finish="search" layout="inline">
            <a-form-item label="引擎ID">
                <a-input-number v-model:value="searchParams.id" allow-clear />
            </a-form-item>
            <a-form-item label="主体类型">
                <a-input v-model:value.trim="searchParams.objTypeContains" allow-clear />
            </a-form-item>
            <a-form-item label="所属主体">
                <a-input v-model:value.trim="searchParams.objNameContains" allow-clear />
            </a-form-item>
            <a-form-item label="集群引擎ID">
                <a-input-number v-model:value="searchParams.engineIdMin" />
                <span>-</span>
                <a-input-number v-model:value="searchParams.engineIdMax" />
            </a-form-item>
            <a-form-item>
                <a-button type="primary" html-type="submit">
                    <template #icon>
                        <search-outlined />
                    </template>搜索
                </a-button>
            </a-form-item>
        </a-form>
    </div>
    <div class="list-table">
        <div class="operation-btns">
            <a-button @click="add" type="primary">
                <template #icon>
                    <plus-outlined />
                </template>添加
            </a-button>
            <template v-if="selectedRowKeys.length > 0">
                <a-button @click="enableSelected" type="success">
                    <template #icon>
                        <check-outlined />
                    </template>启用
                </a-button>
                <a-button @click="disableSelected" type="warning">
                    <template #icon>
                        <stop-outlined />
                    </template>禁用
                </a-button>
                <a-button @click="deleteSelected" type="danger">
                    <template #icon>
                        <delete-outlined />
                    </template>删除
                </a-button>
            </template>
        </div>
        <a-table :columns="columns" :data-source="dataPageList.list" :row-selection="rowSelection" :scroll="{ x: 1300 }" :pagination="false" :row-class-name="(record, index) => (index % 2 === 1 ? 'table-striped' : null)" row-key="id" size="small">
            <template #action="{record, index}">
                <div class="table-operations">
                    <a @click="edit(record, index)" class="text-primary">
                        <edit-outlined />
                    </a>
                    <a @click="edit(record, index, true)" class="text-primary">
                        <copy-outlined />
                    </a>
                    <a-popconfirm title="确定删除吗？" ok-text="确定" cancel-text="取消" @confirm="del(record)">
                        <a class="text-danger">
                            <delete-outlined />
                        </a>
                    </a-popconfirm>
                </div>
            </template>
        </a-table>
        <a-pagination v-model:current="pageNum" v-model:pageSize="pageSize" :total="dataPageList.total" :page-size-options="pageSizeOptions" show-size-changer show-quick-jumper></a-pagination>
    </div>
    <cluster-engine-default-conf-add-or-edit :pk="editPk" :visible="addOrEditDrawerVisible" :operateType="operateType" @save="save" />
</template>

<script lang="ts">
import { defineComponent, reactive, provide, onMounted } from 'vue'
import common from '@/composables/common'
import { pageListSearch, execSelected } from '@/composables/requests'
import ClusterEngineDefaultConfAddOrEdit from './ClusterEngineDefaultConfAddOrEdit.vue'
import { getSelection } from './composables/clusterEngineDefaultConfSelect'
import { getOperations } from './composables/clusterEngineDefaultConfOperate'

const columns = [
    { title: '引擎ID', dataIndex: 'id', key: 'id' },
    { title: '主体类型', dataIndex: 'objType', key: 'objType' },
    { title: '所属主体', dataIndex: 'objName', key: 'objName' },
    { title: '集群引擎ID', dataIndex: 'engineId', key: 'engineId' },
    { title: '操作', key: 'action', fixed: 'right', width: 160, slots: { customRender: 'action' } },
]

const pksField = 'ids'


export default defineComponent({
    components: { ClusterEngineDefaultConfAddOrEdit },
    name: "ClusterEngineDefaultConfIndex",
    setup() {
        const searchParams = reactive<any>({
            id: undefined,
            objTypeContains: '',
            objNameContains: '',
            engineIdMin: '',
            engineIdMax: '',
        })

        const { pageNum, pageSize, dataPageList, search } = pageListSearch({ url: '/cluster-engine-default-conf/page-list', method: 'GET' }, searchParams)
        const { rowSelection, selectedRowKeys, selectedRows, emptySelected } = getSelection(dataPageList)


        const { editPk, addOrEditDrawerVisible, operateType, add, del, edit, save } = getOperations(dataPageList, search)
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
            searchParams, pageNum, pageSize,
            search,
            dataPageList,
            operateType,
            addOrEditDrawerVisible,
            editPk,
            add,
            edit,
            save,
            del,
            disableSelected: execSelected({ url: '/cluster-engine-default-conf/disable', method: 'PATCH' }, selectedRowKeys, pksField, '禁用', () => {
                selectedRows.value.forEach((item: any) => item.deleted = 1)
                emptySelected()
            }),
            enableSelected: execSelected({ url: '/cluster-engine-default-conf/enable', method: 'PATCH' }, selectedRowKeys, pksField, '启用', () => {
                selectedRows.value.forEach((item: any) => item.deleted = 0)
                emptySelected()
            }),
            deleteSelected: execSelected({ url: '/cluster-engine-default-conf/delete', method: 'DELETE' }, selectedRowKeys, pksField, '删除', () => {
                search()
                emptySelected()
            })
        }
    }
})
</script>

<style scoped>
</style>
