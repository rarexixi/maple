import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/datasource',
      name: 'datasource',
      component: () => import('@/views/datasource/DatasourceIndexView.vue')
    },
    {
      path: '/datasource-type',
      name: 'datasourceType',
      component: () => import('@/views/datasource-type/DatasourceTypeIndexView.vue')
    },
    {
      path: '/data-calc-array',
      name: 'dataCalcArray',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/views/data-calc/DataCalcArrayView.vue')
    },
    {
      path: '/data-calc-group',
      name: 'dataCalcGroup',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/views/data-calc/DataCalcGroupView.vue')
    },
  ]
})

export default router
