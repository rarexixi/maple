import common from "@/composables/common"
import { PageInfo } from "@/composables/models"
import { request } from "@/utils/request-utils"
import { AxiosRequestConfig } from "axios"
import { reactive, ref, UnwrapRef } from "vue"

export function getOperations(dataPageList: UnwrapRef<PageInfo>, operateCallback: () => void) {
    const operateType = ref(common.DataOperationType.default)
    const visible = ref(false)
    const editPk = reactive({
        id: undefined,
    })
    const editIndex = ref(-1)

    const add = () => {
        editPk.id = undefined
        editIndex.value = -1
        operateType.value = common.DataOperationType.create
        visible.value = true
    }

    const edit = (item: any, index: number, copy = false) => {
        editPk.id = item.id
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
            id: item.id,
        }
        return params
    }

    const del = (item: any) => {
        const params: any = getPkParams(item)
        const requestConfig: AxiosRequestConfig = { url: '/cluster-engine/delete', method: 'DELETE', params }
        request(requestConfig).then(response => {
            if (response > 0 && operateCallback) operateCallback()
        })
    }

    return { editPk, addOrEditDrawerVisible: visible, operateType, add, del, edit, save }
}