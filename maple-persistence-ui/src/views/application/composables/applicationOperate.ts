import common from "@/composables/common"
import { PageInfo } from "@/composables/models"
import { request } from "@/utils/request-utils"
import { AxiosRequestConfig } from "axios"
import { reactive, ref, UnwrapRef } from "vue"

export function getOperations(dataPageList: UnwrapRef<PageInfo>, operateCallback: () => void) {
    const operateType = ref(common.DataOperationType.default)
    const visible = ref(false)
    const editPk = reactive({
        appName: '',
    })
    const editIndex = ref(-1)

    const add = () => {
        editPk.appName = ''
        editIndex.value = -1
        operateType.value = common.DataOperationType.create
        visible.value = true
    }

    const edit = (item: any, index: number, copy = false) => {
        editPk.appName = item.appName
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
            dataPageList.list[editIndex.value] = detail
        } else {
            if (operateCallback) operateCallback()
        }
        editIndex.value = -1
    }

    const getPkParams = (item: any) => {
        const params: any = {
            appName: item.appName,
        }
        return params
    }

    const switchDeleted = (item: any) => {
        const params: any = getPkParams(item)
        const requestConfig: AxiosRequestConfig = item.deleted === 1
            ? { url: '/application/enable', method: 'PATCH', params }
            : { url: '/application/disable', method: 'PATCH', params }
        request(requestConfig).then(response => {
            if (response > 0) item.deleted = item.deleted === 1 ? 0 : 1
        })
    }

    const del = (item: any) => {
        const params: any = getPkParams(item)
        const requestConfig: AxiosRequestConfig = { url: '/application/delete', method: 'DELETE', params }
        request(requestConfig).then(response => {
            if (response > 0 && operateCallback) operateCallback()
        })
    }

    return { editPk, addOrEditDrawerVisible: visible, operateType, add, del, edit, switchDeleted, save }
}