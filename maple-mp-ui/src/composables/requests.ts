import type { FormInstance } from "ant-design-vue"
import type { AxiosRequestConfig } from 'axios'
import type { ShallowRef, UnwrapRef } from "vue"
import { ref, reactive, watch } from "vue"

import common from '@/composables/common'
import type { PageInfo } from '@/composables/models'
import { request } from '@/utils/request-utils'
import { SysConfApis } from "@/composables/service-apis"

export function listSearch(listRequestConfig: AxiosRequestConfig,
                           searchParams: UnwrapRef<any>,
                           formRef?: Readonly<ShallowRef<FormInstance | null>>,
                           convertToList: (list: any[]) => any[] = (list: any[]) => list,
                           setMap: (list: any[], dataMap: any) => void = (list: any[]) => {
                           }) {
  const dataList = ref<any[]>([])
  const dataMap = reactive<any>({})
  const dataInitialized = ref(false)
  const search = () => {
    request({ ...listRequestConfig, params: { ...searchParams } }).then(response => {
      dataList.value = convertToList(response)
      setMap(response, dataMap)
      dataInitialized.value = true
    })
  }
  const resetSearch = () => {
    formRef?.value?.resetFields()
  }

  search()
  return { dataList, dataMap, search, resetSearch, dataInitialized }
}

export function pageListSearch(pageListRequestConfig: AxiosRequestConfig,
                               searchParams: UnwrapRef<any>,
                               formRef?: Readonly<ShallowRef<FormInstance | null>>,
                               convertList: (list: any[]) => any[] = (list: any[]) => list) {
  const dataPageList = reactive<PageInfo>(common.getDefaultPageInfo())
  const pageNum = ref(1)
  const pageSize = ref(10)
  const search = () => {
    request({
      ...pageListRequestConfig,
      params: { ...searchParams, pageNum: pageNum.value, pageSize: pageSize.value }
    }).then(response => {
      dataPageList.list = convertList(response.list)
      dataPageList.total = response.total
      dataPageList.pageNum = response.pageNum
      dataPageList.pageSize = response.pageSize
    })
  }
  const resetSearch = () => {
    formRef?.value?.resetFields()
  }
  watch(pageSize, () => {
    pageNum.value = 1
    search()
  })
  watch(pageNum, search)

  search()
  return { pageNum, pageSize, dataPageList, search, resetSearch }
}

export function getArrayConf(configKey: string, valueField: string = "value", labelField: string = "label") {

  const confArray = ref<any[]>([])
  const confMap = reactive<any>({})
  const confOptions = ref<any[]>([])
  const confOptionMap = reactive<any>({})
  const confInitialized = ref(false)

  request(SysConfApis.detail(configKey)).then(response => {
    confArray.value = JSON.parse(response.confValue);
    confOptions.value = confArray.value.map((item: any) => {
      if (typeof item === 'string') {
        confMap[item] = item
        confOptionMap[item] = item
        return { value: item, label: item }
      } else {
        confMap[item[valueField]] = item
        confOptionMap[item[valueField]] = item[labelField]
        return { value: item[valueField], label: item[labelField] }
      }
    })
    confInitialized.value = true
  })

  return { confArray, confMap, confOptions, confOptionMap, confInitialized }
}