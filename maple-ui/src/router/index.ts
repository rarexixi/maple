import { createRouter, createWebHistory } from 'vue-router'
import MapleView from '@/views/MapleView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: MapleView
    },
    {
      path: '/maple',
      name: 'maple',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/views/MapleView.vue')
    },
    {
      path: '/datasource',
      name: 'datasource',
      component: () => import('@/views/datasource/DatasourceIndex.vue')
    },
    {
      path: '/datasource-type',
      name: 'datasourceType',
      component: () => import('@/views/datasource_type/DatasourceTypeIndex.vue')
    },
    {
      path: '/sample',
      name: 'sample',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('@/views/Sample.vue')
    }
  ]
})

export default router
