import { ExclamationCircleOutlined } from "@ant-design/icons-vue"
import { Modal, notification } from "ant-design-vue"
import type { AxiosRequestConfig } from "axios"
import { createVNode, reactive, ref, toRaw } from "vue"

import { request } from "@/utils/request-utils"

export function getDialogOperations() {
  const opened = ref(false)
  const editIndex = ref(-1)
  const title = ref<string>('')

  const openDialog = (index: number = -1) => {
    editIndex.value = index
    opened.value = true
  }

  const closeDialog = () => {
    editIndex.value = -1
    opened.value = false
  }

  return {editIndex, title, opened, openDialog, closeDialog}
}

export interface OperateCallback {
  detail?: (detail: any, copyPk: boolean) => void
  research?: () => void
  resetDetail?: (detail: any) => void
  setItem?: (detail: any, editIndex: number) => void
}

export function getSingleDataOperations(operationUrls: any, operateCallback: OperateCallback) {

  const dialogOperations = getDialogOperations()

  function getData() {
    const data: any = {};
    operateCallback.resetDetail?.(data)
    return data;
  }

  const detail = reactive(getData())

  function upsert() {
    const requestConfig: AxiosRequestConfig = dialogOperations.editIndex.value >= 0
      ? {...operationUrls.update(toRaw(detail)), data: toRaw(detail)}
      : {...operationUrls.add(), data: toRaw(detail)}
    request(requestConfig).then(response => {
      notification.success({message: '保存成功'})
      dialogOperations.closeDialog()
      if (dialogOperations.editIndex.value >= 0) {
        operateCallback.setItem?.(detail, dialogOperations.editIndex.value)
        dialogOperations.editIndex.value = -1
      } else {
        operateCallback.research?.()
      }
    })
  }

  function add(title: string) {
    dialogOperations.title.value = title
    operateCallback.resetDetail?.(detail)
    dialogOperations.openDialog()
  }

  function copy(title: string, item: any) {
    dialogOperations.title.value = title
    operateCallback.resetDetail?.(detail)
    dialogOperations.openDialog()
    get(item, false)
  }

  function edit(title: string, item: any, index: number) {
    dialogOperations.title.value = title
    operateCallback.resetDetail?.(detail)
    dialogOperations.openDialog(index)
    get(item, true)
  }

  function get(item: any, copyPk: boolean) {
    const requestConfig: AxiosRequestConfig = {...operationUrls.detail(item)}
    request(requestConfig).then(response => {
      operateCallback.detail?.(response, copyPk)
    })
  }

  function enable(item: any, callback: () => void = () => {}) {
    exec(operationUrls.enable(item), "启用", callback)
  }

  function disable(item: any, callback: () => void = () => {}) {
    exec(operationUrls.disable(item), "禁用", callback)
  }

  function del(item: any) {
    exec(operationUrls.delete(item), "删除", operateCallback.research)
  }

  function exec(requestConfig: AxiosRequestConfig, operateMessage: string, successCallback: () => void = () => undefined) {
    request(requestConfig).then(response => {
      if (response > 0) {
        notification.success({message: `${operateMessage}成功`})
        if (successCallback) successCallback()
      } else {
        notification.error({message: `${operateMessage}失败`})
      }
    }).catch(() => {
      notification.error({message: `${operateMessage}失败`})
    })
  }

  return {dialogOperations, detail, get, add, copy, edit, upsert, enable, disable, del}
}

export function getMultiDataOperations(selection: any, operationUrls: any, operateCallback: OperateCallback) {

  const disableSelected = (callback: (item: any) => void = (item: any) => {}) =>
    execSelected(operationUrls.disable(selection.selectedRowKeys.value), selection.selected, '禁用', () => {
      selection.selectedRows.value.forEach((item: any) => callback(item))
      selection.emptySelected()
    })

  const enableSelected = (callback: (item: any) => void = (item: any) => {}) =>
    execSelected(operationUrls.enable(selection.selectedRowKeys.value), selection.selected, '启用', () => {
      selection.selectedRows.value.forEach((item: any) => callback(item))
      selection.emptySelected()
    })

  const delSelected = () =>
    execSelected(operationUrls.delete(selection.selectedRowKeys.value), selection.selected, '删除', () => {
      operateCallback.research?.()
      selection.emptySelected()
    })

  function execSelected(requestConfig: AxiosRequestConfig, selected: boolean, operateMessage: string, successCallback: () => void = () => undefined) {
    if (!selected) {
      notification.warning({message: `${operateMessage}列表不能为空`})
      return
    }
    Modal.confirm({
      title: `确定${operateMessage}吗？`,
      content: '',
      icon: createVNode(ExclamationCircleOutlined),
      okText: '确定',
      cancelText: '取消',
      onOk() {
        request(requestConfig).then(response => {
          if (response > 0) {
            notification.success({message: `${operateMessage}成功`})
            if (successCallback) successCallback()
          } else {
            notification.error({message: `${operateMessage}失败`})
          }
        }).catch(() => {
          notification.error({message: `${operateMessage}失败`})
        })
      }
    })
  }

  return {enableSelected, disableSelected, delSelected}
}