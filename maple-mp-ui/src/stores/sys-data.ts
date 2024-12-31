import { defineStore } from "pinia"
import { ClusterApis, ClusterEngineApis, DatasourceApis } from "@/composables/service-apis"
import { computed } from "vue"
import { listSearch } from "@/composables/requests"

export const useDatasourceStore = defineStore("datasource", () => {
  const setMap = (list: any[], dataMap: any)  => {
    list.forEach((item: any) => {
      dataMap[item.id] = item
    })
  }
  const resp = listSearch(DatasourceApis.list(), {}, undefined, list => list, setMap)
  const dataList = computed(() => resp.dataList)
  const dataMap = computed(() => resp.dataMap)
  const dataInitialized = computed(() => resp.dataInitialized)

  function refresh() {
    resp.search()
  }

  return { dataList, dataMap, dataInitialized, refresh }
})

export const useClusterEngineStore = defineStore("cluster_engine", () => {
  const setMap = (list: any[], dataMap: any)  => {
    list.forEach((item: any) => {
      dataMap[item.id] = item
    })
  }
  const resp = listSearch(ClusterEngineApis.list(), {}, undefined, list => list, setMap)
  const dataList = computed(() => resp.dataList)
  const dataMap = computed(() => resp.dataMap)
  const dataInitialized = computed(() => resp.dataInitialized)

  function refresh() {
    resp.search()
  }

  return { dataList, dataMap, dataInitialized, refresh }
})

export const useClusterStore = defineStore("cluster", () => {
  const setMap = (list: any[], dataMap: any)  => {
    list.forEach((item: any) => {
      dataMap[item.id] = item
    })
  }
  const resp = listSearch(ClusterApis.list(), {}, undefined, list => list, setMap)
  const dataList = computed(() => resp.dataList)
  const dataMap = computed(() => resp.dataMap)
  const dataInitialized = computed(() => resp.dataInitialized)

  function refresh() {
    resp.search()
  }

  return { dataList, dataMap, dataInitialized, refresh }
})
