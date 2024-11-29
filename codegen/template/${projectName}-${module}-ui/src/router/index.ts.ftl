import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
<#list tableModels as table>
<#include "/include/table/properties.ftl">
    {
      path: '/${tablePath}',
      name: '${classNameFirstLower}',
      component: () => import('@/views/${tablePath}/${className}IndexView.vue')
    },
</#list>
  ]
})

export default router
