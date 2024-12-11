import { defineStore } from "pinia"
import { DatasourceApis } from "@/composables/service-apis"
import { computed } from "vue"
import { listSearch } from "@/composables/requests"

export const useDatasourceStore = defineStore("datasource", () => {
  const resp = listSearch(DatasourceApis.list(), {})
  const dataList = computed(() => resp.dataList)
  const dataMap = computed(() => resp.dataMap)

  function refresh() {
    resp.search()
  }

  return { dataList, dataMap, refresh }
})
