import { createRouter, createWebHistory } from 'vue-router'
import { DataOperationType } from "@/composables/common"

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
      path: '/spark-data-calc-array',
      name: 'sparkDataCalcArray',
      component: () => import('@/views/data-calc/SparkDataCalcArrayView.vue')
    },
    {
      path: '/spark-data-calc-group',
      name: 'sparkDataCalcGroup',
      component: () => import('@/views/data-calc/SparkDataCalcGroupView.vue')
    },
    {
      path: '/flink-data-calc-array',
      name: 'flinkDataCalcArray',
      component: () => import('@/views/data-calc/FlinkDataCalcArrayView.vue')
    },
    {
      path: '/flink-data-calc-group',
      name: 'flinkDataCalcGroup',
      component: () => import('@/views/data-calc/FlinkDataCalcGroupView.vue')
    },
  ]
})

export default router
