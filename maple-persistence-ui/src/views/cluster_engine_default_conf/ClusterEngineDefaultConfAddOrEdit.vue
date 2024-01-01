<template>
    <a-drawer :visible="visible" :title="title" @close="closeDrawer" width="600px">
        <a-form ref="formRef" :model="detail" @finish="save" :rules="rules" :label-col="{span: 4}" :wrapper-col="{span: 20}">
            <a-row :gutter="10">
                <a-col :span="24">
                    <a-form-item ref="objType" label="主体类型" name="objType">
                        <a-input v-model:value.trim="detail.objType" type="text" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item ref="objName" label="所属主体" name="objName">
                        <a-input v-model:value.trim="detail.objName" type="text" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item ref="engineId" label="集群引擎ID" name="engineId">
                        <a-input-number v-model:value="detail.engineId" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item ref="defaultConf" label="默认配置" name="defaultConf">
                        <a-input v-model:value="detail.defaultConf" type="textarea" :autosize="{ minRows: 5, maxRows: 100}" />
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
    engineId: [
        { type: 'integer', required: true, message: '集群引擎ID不能为空', trigger: 'blur' }
    ],
    defaultConf: [
        { required: true, message: '默认配置不能为空', trigger: 'blur' }
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
            objType: '',
            objName: '',
            engineId: undefined,
            defaultConf: '',
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
                title.value = '添加集群引擎默认配置'
            } else {
                if (operateType.value === common.DataOperationType.copy)
                    title.value = '复制集群引擎默认配置'
                else if (operateType.value === common.DataOperationType.update)
                    title.value = '编辑集群引擎默认配置'

                request({ url: '/cluster-engine-default-conf/detail', method: 'GET', params: pk.value }).then(response => {
                    if (operateType.value !== common.DataOperationType.copy) {
                        detail.id = response.id
                    }
                    detail.objType = response.objType
                    detail.objName = response.objName
                    detail.engineId = response.engineId
                    detail.defaultConf = response.defaultConf
                })
            }
        }

        watch(visible, getDetail)

        const save = () => {
            formRef.value.validate().then(() => {
                const requestConfig: AxiosRequestConfig = operateType.value === common.DataOperationType.update
                    ? { url: '/cluster-engine-default-conf/update', method: "PATCH", data: toRaw(detail), params: pk.value }
                    : { url: '/cluster-engine-default-conf/add', method: "POST", data: toRaw(detail) }
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