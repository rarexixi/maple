<template>
    <a-breadcrumb separator="/">
        <a-breadcrumb-item>访问程序管理</a-breadcrumb-item>
        <a-breadcrumb-item>访问程序列表</a-breadcrumb-item>
    </a-breadcrumb>
    <div class="search-form">
        <a-form ref="searchForm" :model="searchParams" @finish="search" layout="inline">
            <a-form-item label="应用名称">
                <a-input v-model:value.trim="searchParams.appName" allow-clear />
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
        <a-table :columns="columns" :data-source="dataPageList.list" :row-selection="rowSelection" :scroll="{ x: 1300 }" :pagination="false" :row-class-name="(record, index) => (index % 2 === 1 ? 'table-striped' : null)" row-key="appName" size="small">
            <template #action="{record, index}">
                <div class="table-operations">
                    <a-popconfirm :title="`确定${record.deleted === 1 ? '启用' : '禁用'}吗？`" ok-text="确定" cancel-text="取消" @confirm="switchDeleted(record)">
                        <a v-if="record.deleted" class="enable">
                            <check-outlined />
                            <stop-outlined />
                        </a>
                        <a v-else class="disable">
                            <check-outlined />
                            <stop-outlined />
                        </a>
                    </a-popconfirm>
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
    <application-add-or-edit :pk="editPk" :visible="addOrEditDrawerVisible" :operateType="operateType" @save="save" />
</template>

<script lang="ts">
import { defineComponent, reactive, provide, onMounted } from 'vue'
import common from '@/composables/common'
import { pageListSearch, execSelected } from '@/composables/requests'
import ApplicationAddOrEdit from './ApplicationAddOrEdit.vue'
import { getSelection } from './composables/applicationSelect'
import { getOperations } from './composables/applicationOperate'

const columns = [
    { title: '应用名称', dataIndex: 'appName', key: 'appName' },
    { title: '应用访问密钥', dataIndex: 'accessKey', key: 'accessKey' },
    { title: '允许请求的IP', dataIndex: 'legalHosts', key: 'legalHosts' },
    { title: '回调接口', dataIndex: 'webhooks', key: 'webhooks' },
    { title: '创建人', dataIndex: 'createUser', key: 'createUser' },
    { title: '修改人', dataIndex: 'updateUser', key: 'updateUser' },
    { title: '创建时间', dataIndex: 'createTime', key: 'createTime' },
    { title: '更新时间', dataIndex: 'updateTime', key: 'updateTime' },
    { title: '操作', key: 'action', fixed: 'right', width: 160, slots: { customRender: 'action' } },
]

const pksField = 'ids'


export default defineComponent({
    components: { ApplicationAddOrEdit },
    name: "ApplicationIndex",
    setup() {
        const searchParams = reactive<any>({
            appName: '',
        })

        const { pageNum, pageSize, dataPageList, search } = pageListSearch({ url: '/application/page-list', method: 'GET' }, searchParams)
        const { rowSelection, selectedRowKeys, selectedRows, emptySelected } = getSelection(dataPageList)


        const { editPk, addOrEditDrawerVisible, operateType, add, del, edit, switchDeleted, save } = getOperations(dataPageList, search)
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
            switchDeleted,
            del,
            disableSelected: execSelected({ url: '/application/disable', method: 'PATCH' }, selectedRowKeys, pksField, '禁用', () => {
                selectedRows.value.forEach((item: any) => item.deleted = 1)
                emptySelected()
            }),
            enableSelected: execSelected({ url: '/application/enable', method: 'PATCH' }, selectedRowKeys, pksField, '启用', () => {
                selectedRows.value.forEach((item: any) => item.deleted = 0)
                emptySelected()
            }),
            deleteSelected: execSelected({ url: '/application/delete', method: 'DELETE' }, selectedRowKeys, pksField, '删除', () => {
                search()
                emptySelected()
            })
        }
    }
})
</script>

<style scoped>
</style>
