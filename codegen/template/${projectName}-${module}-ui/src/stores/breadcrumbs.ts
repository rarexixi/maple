import { defineStore } from "pinia"
import { computed, reactive } from "vue"

interface Breadcrumb {
  path?: string
  text: string
}

export const useBreadcrumbStore = defineStore('breadcrumbs', () => {
  const breadcrumbStore = reactive({
    separator: '/',
    breadcrumbs: [] as Breadcrumb[]
  });

  const breadcrumbs = computed(() => breadcrumbStore.breadcrumbs)

  function setBreadcrumb(breadcrumbs: Breadcrumb[]) {
    breadcrumbStore.breadcrumbs = [{text: '主页'}, ...breadcrumbs];
  }

  return {breadcrumbStore, breadcrumbs, setBreadcrumb}
})