import common from "@/composables/common"
import type {PageInfo} from "@/composables/models"
import {request} from "@/utils/request-utils"
import type {AxiosRequestConfig} from "axios"
import type {UnwrapRef} from "vue"
import {reactive, ref} from "vue"

export function getOperations(dataList: UnwrapRef<any[]>, operateCallback: () => void) {
    const operateType = ref(common.DataOperationType.default)
    const visible = ref(false)
    const editPk = reactive({
        typeCode: '',
    })
    const editIndex = ref(-1)

    const add = () => {
        editPk.typeCode = ''
        editIndex.value = -1
        operateType.value = common.DataOperationType.create
        visible.value = true
    }

    const edit = (item: any, index: number, copy = false) => {
        editPk.typeCode = item.typeCode
        if (copy) {
            editIndex.value = -1
            operateType.value = common.DataOperationType.copy
        } else {
            editIndex.value = index
            operateType.value = common.DataOperationType.update
        }
        visible.value = true
    }

    const save = (detail: any) => {
        if (editIndex.value >= 0) {
            dataList[editIndex.value] = detail
        } else {
            if (operateCallback) operateCallback()
        }
        editIndex.value = -1
    }

    const getPkParams = (item: any) => {
        const params: any = {
            typeCode: item.typeCode,
        }
        return params
    }

    const switchDeleted = (item: any) => {
        const params: any = getPkParams(item)
        const requestConfig: AxiosRequestConfig = item.deleted === 1
            ? {url: '/datasource-type/enable', method: 'PATCH', params}
            : {url: '/datasource-type/disable', method: 'PATCH', params}
        request(requestConfig).then(response => {
            if (response > 0) item.deleted = item.deleted === 1 ? 0 : 1
        })
    }

    const del = (item: any) => {
        const params: any = getPkParams(item)
        const requestConfig: AxiosRequestConfig = {url: '/datasource-type/delete', method: 'DELETE', params}
        request(requestConfig).then(response => {
            if (response > 0 && operateCallback) operateCallback()
        })
    }

    return {editPk, addOrEditDrawerVisible: visible, operateType, add, del, edit, switchDeleted, save}
}