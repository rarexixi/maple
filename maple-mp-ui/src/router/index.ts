import { createRouter, createWebHistory } from 'vue-router'
import { DataOperationType } from "@/composables/common";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/sys-conf',
      name: 'sysConf',
      component: () => import('@/views/sys-conf/SysConfIndexView.vue')
    },
    {
      path: '/cluster',
      name: 'cluster',
      component: () => import('@/views/cluster/ClusterIndexView.vue')
    },
    {
      path: '/cluster-engine',
      name: 'clusterEngine',
      component: () => import('@/views/cluster-engine/ClusterEngineIndexView.vue')
    },
    {
      path: '/datasource-type',
      name: 'datasourceType',
      component: () => import('@/views/datasource-type/DatasourceTypeIndexView.vue')
    },
    {
      path: '/datasource',
      name: 'datasource',
      component: () => import('@/views/datasource/DatasourceIndexView.vue')
    },
    {
      path: '/job',
      name: 'job',
      component: () => import('@/views/job/JobIndexView.vue')
    },
    {
      path: '/job/add/:jobType',
      name: 'jobAdd',
      component: () => import('@/views/job/JobUpsertView.vue'),
      props: route => ({
        jobType: route.params.jobType,
        operateType: DataOperationType.create
      })
    },
    {
      path: '/job/copy/:id',
      name: 'jobCopy',
      component: () => import('@/views/job/JobUpsertView.vue'),
      props: route => ({
        id: route.params.id,
        operateType: DataOperationType.copy,
      })
    },
    {
      path: '/job/edit/:id',
      name: 'jobEdit',
      component: () => import('@/views/job/JobUpsertView.vue'),
      props: route => ({
        id: route.params.id,
        operateType: DataOperationType.update,
      })
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
