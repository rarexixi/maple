import type { PageInfo, ValidatableComponent } from "@/composables/models"
import { type FormInstance, notification } from 'ant-design-vue'
import { ref, type ShallowRef } from "vue"

const SortEnum = {ASC: 'ASC', DESC: 'DESC'}
const PageSizeOptions = ['10', '20', '50', '100']
const DefaultSearchParams = {defaultPageNum: 1, defaultPageSize: 50}

export enum DataOperationType {create = 1, copy = 2, update = 3}

const validateMessages = {
  required: '请输入/选择${label}!',
  number: {
    range: '${label}必须在${min}和${max}之间',
  },
}

function getDefaultPageInfo(): PageInfo {
  return {
    total: 0,
    pageNum: 0,
    pageSize: 0,
    list: []
  }
}

function convertToOptions(valueField: string, labelField: string) {
  return (list: any[]) => {
    return list.map(item => {
      return {value: item[valueField], label: item[labelField]}
    })
  }
}

function setOptionMap(keyField: string, valueField: string) {
  return (list: any[], optionMap: any) => {
    for (let item of list) {
      optionMap[item[keyField]] = item[valueField]
    }
  }
}

function notifyValidateError() {
  notification.error({message: "参数验证失败"})
}

// (setValidated: (success: boolean) => void) => Promise<void>
function getFormValidateFun(formRef: Readonly<ShallowRef<FormInstance | null>>) {
  async function validate(setValidated: (success: boolean) => void) {
    await formRef.value?.validate().then(() => {
      setValidated(true)
    }).catch((error: any) => {
      setValidated(false)
    })
  }

  return validate
}

async function getFormValidated(...formRefs: Readonly<ShallowRef<ValidatableComponent | null>>[]) {
  let validated = true
  const setValidated = (success: boolean) => {
    validated = success && validated
  }
  for (let formRef of formRefs) {
    await formRef.value?.validate(setValidated)
  }
  return validated
}

export default {
  SortEnum,
  DefaultSearchParams,
  PageSizeOptions,
  validateMessages,
  getDefaultPageInfo,
  convertToOptions,
  setOptionMap,
  notifyValidateError,
  getFormValidateFun,
  getFormValidated,
}
