<template>
    <a-drawer :visible="visible" :title="title" @close="closeDrawer" width="600px">
        <a-form ref="formRef" :model="detail" @finish="save" :rules="rules" :label-col="{span: 4}" :wrapper-col="{span: 20}">
            <a-row :gutter="10">
                <a-col :span="24">
                    <a-form-item ref="appName" label="应用名称" name="appName">
                        <a-input v-model:value.trim="detail.appName" type="text" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item ref="accessKey" label="应用访问密钥" name="accessKey">
                        <a-input v-model:value.trim="detail.accessKey" type="text" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item ref="legalHosts" label="允许请求的IP" name="legalHosts">
                        <a-input v-model:value.trim="detail.legalHosts" type="text" />
                    </a-form-item>
                </a-col>
                <a-col :span="24">
                    <a-form-item ref="webhooks" label="回调接口" name="webhooks">
                        <a-input v-model:value.trim="detail.webhooks" type="text" />
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
    appName: [
        { required: true, message: '应用名称不能为空', trigger: 'blur' }
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
            appName: '',
            accessKey: '',
            legalHosts: '',
            webhooks: '',
        })
        const resetForm = () => {
            formRef.value.resetFields()
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
                title.value = '添加访问程序'
            } else {
                if (operateType.value === common.DataOperationType.copy)
                    title.value = '复制访问程序'
                else if (operateType.value === common.DataOperationType.update)
                    title.value = '编辑访问程序'

                request({ url: '/application/detail', method: 'GET', params: pk.value }).then(response => {
                    detail.appName = response.appName
                    detail.accessKey = response.accessKey
                    detail.legalHosts = response.legalHosts
                    detail.webhooks = response.webhooks
                })
            }
        }

        watch(visible, getDetail)

        const save = () => {
            formRef.value.validate().then(() => {
                const requestConfig: AxiosRequestConfig = operateType.value === common.DataOperationType.update
                    ? { url: '/application/update', method: "PATCH", data: toRaw(detail), params: pk.value }
                    : { url: '/application/add', method: "POST", data: toRaw(detail) }
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