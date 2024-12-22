import { defineStore } from "pinia"
import { DatasourceApis } from "@/composables/service-apis"
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
