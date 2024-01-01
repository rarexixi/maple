<template>
    <a-drawer :visible="visible" :title="title" @close="closeDrawer" width="600px">
        <a-form ref="formRef" :model="detail" @finish="save" :rules="rules" :label-col="{span: 4}" :wrapper-col="{span: 20}">
            <a-row :gutter="10">
                <a-col :span="24">
                    <a-form-item ref="cluster" label="集群名称" name="cluster">
                        <a-input v-model:value.trim="detail.cluster" type="text" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item ref="name" label="类型名称" name="name">
                        <a-input v-model:value.trim="detail.name" type="text" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item ref="version" label="类型版本" name="version">
                        <a-input v-model:value.trim="detail.version" type="text" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item ref="engineHome" label="引擎目录" name="engineHome">
                        <a-input v-model:value.trim="detail.engineHome" type="text" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item ref="extInfo" label="扩展信息" name="extInfo">
                        <a-input v-model:value="detail.extInfo" type="textarea" :autosize="{ minRows: 5, maxRows: 100}" />
                    </a-form-item>
                </a-col>
            </a-row>
            <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
                <a-button type="primary" html-type="submit">保存</a-button>
                <a-button style="margin-left: 10px" @click="closeDrawer">取消</a-button>
            </a-form-item>
        </a-form>
    </a-drawer>
</template>

<script lang="ts">
import { notification } from 'ant-design-vue'
import { ValidateErrorEntity } from 'ant-design-vue/es/form/interface'
import { AxiosRequestConfig } from 'axios'
import { defineComponent, inject, reactive, ref, toRaw, toRefs, watch } from 'vue'
import common from '@/composables/common'
import { request } from '@/utils/request-utils'

const rules = {
    id: [
        { type: 'integer', required: true, message: '引擎ID不能为空', trigger: 'blur' }
    ],
    extInfo: [
        { required: true, message: '扩展信息不能为空', trigger: 'blur' }
    ],
}


export default defineComponent({
    props: {
        pk: {
            type: [Object, Map],
            required: false,
            default: () => undefined
        },
        operateType: {
            type: Number,
            default: () => common.DataOperationType.default
        },
        visible: {
            type: Boolean,
            default: () => false
        },
    },
    setup(props, { emit }) {
        const { pk, operateType, visible } = toRefs(props)
        const title = ref<string>('')
        const formRef = ref()
        const detail = reactive<any>({
            id: undefined,
            cluster: '',
            name: '',
            version: '',
            engineHome: '',
            extInfo: '',
        })
        const resetForm = () => {
            formRef.value.resetFields()
            detail.id = undefined
        }
        const closeAddOrEditDrawer = inject<(newVal: boolean) => void>('closeAddOrEditDrawer')
        const closeDrawer = () => {
            resetForm()
            if (closeAddOrEditDrawer) closeAddOrEditDrawer(false)
        }

        const getDetail = (newVal: boolean) => {
            if (!newVal) return
            if (operateType.value === common.DataOperationType.default) return
            if (operateType.value === common.DataOperationType.create) {
                title.value = '添加集群引擎'
            } else {
                if (operateType.value === common.DataOperationType.copy)
                    title.value = '复制集群引擎'
                else if (operateType.value === common.DataOperationType.update)
                    title.value = '编辑集群引擎'

                request({ url: '/cluster-engine/detail', method: 'GET', params: pk.value }).then(response => {
                    if (operateType.value !== common.DataOperationType.copy) {
                        detail.id = response.id
                    }
                    detail.cluster = response.cluster
                    detail.name = response.name
                    detail.version = response.version
                    detail.engineHome = response.engineHome
                    detail.extInfo = response.extInfo
                })
            }
        }

        watch(visible, getDetail)

        const save = () => {
            formRef.value.validate().then(() => {
                const requestConfig: AxiosRequestConfig = operateType.value === common.DataOperationType.update
                    ? { url: '/cluster-engine/update', method: "PATCH", data: toRaw(detail), params: pk.value }
                    : { url: '/cluster-engine/add', method: "POST", data: toRaw(detail) }
                request(requestConfig).then(response => {
                    notification.success({
                        message: "保存成功"
                    })
                    closeDrawer()
                    emit("save", response)
                })
            }).catch((error: ValidateErrorEntity<any>) => {
                console.log(error)
                notification.error({
                    message: "参数验证失败"
                })
            })
        }

        return {
            title, detail,
            formRef,
            rules,
            save,
            closeDrawer
        }
    }
})
</script>

<style scoped>
</style>