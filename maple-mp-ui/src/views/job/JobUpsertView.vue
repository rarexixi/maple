<script lang="ts" setup>
import { minimatch } from "minimatch"
import { computed, onBeforeMount, onBeforeUpdate, onMounted, reactive } from "vue"
import { useRouter } from "vue-router"

import common, { DataOperationType } from '@/composables/common'
import jobs from '@/composables/jobs'
import { listSearch } from "@/composables/requests"
import { JobApis, ClusterEngineApis, ClusterApis } from "@/composables/service-apis"
import { useJobTypesStore } from "@/stores/sys-conf"
import type { OperateCallback } from "@/composables/table-operations"
import { getSingleDataOperations2 } from "@/composables/table-operations"

import UpsertCard from "@/components/UpsertCard.vue"
import JobUpsertForm from "@/components/job/JobUpsertForm.vue"

import { useBreadcrumbStore } from "@/stores/breadcrumbs"
// 设置面包屑
const { setBreadcrumb } = useBreadcrumbStore()
setBreadcrumb([{ text: '作业' }, { text: getTitle() }])

interface Props {
  id?: any;
  jobType?: string;
  operateType: number;
}

const {
  id,
  jobType,
  operateType
} = defineProps<Props>()

const clusterSearchParams = reactive<any>({
  deleted: 0
})
const {
  dataMap: clusterOptionMap
} = listSearch(ClusterApis.list(), clusterSearchParams, undefined, common.convertToOptions('id', 'name'), common.setOptionMap('id', 'name'))

const engineSearchParams = reactive<any>({})
const {
  dataList: engineList
} = listSearch(ClusterEngineApis.list(), engineSearchParams, undefined)

const {} = useJobTypesStore()

const {
  confOptions: jobTypeOptions,
  confOptionMap: jobTypeOptionMap,
  confMap: jobTypeMap
} = useJobTypesStore()

function getTitle() {
  if (operateType === DataOperationType.copy)
    return '复制作业'
  else if (operateType === DataOperationType.update)
    return '编辑作业'
  return `添加作业`
}

const router = useRouter()

const callback: OperateCallback = {
  detail: (response: any, copyPk: boolean) => {
    if (copyPk) {
      detail.id = response.id
    }
    detail.jobName = response.jobName
    detail.description = response.description
    setJobConf(response.runConf, response.jobConf, response.jobType)
    detail.jobType = response.jobType
    detail.engineId = response.engineId
    detail.owner = response.owner
  },
  resetDetail: (detail: any) => {
    detail.id = undefined
    detail.jobName = ''
    detail.description = ''
    detail.jobType = ''
    detail.engineId = undefined
    detail.owner = ''
    detail.runConf = {}
    detail.jobConf = {}
  },
  afterSave() {
    router.push('/job')
  },
}

const {
  detail,
  get: getDetail,
  upsert,
} = getSingleDataOperations2(JobApis, callback)

function setJobConf(runConf: any, jobConf: any, jobType?: string) {
  if (!jobType) return

  let getDefaultJobConf = jobs.JobConf[jobType]
  let defaultJobConf = getDefaultJobConf ? getDefaultJobConf() : {}
  detail.jobConf = { ...defaultJobConf, ...jobConf }

  let jobTypeDetail = jobTypeMap[jobType]
  if (jobTypeDetail) {
    let getDefaultRunConf = jobs.JobRunTypes[jobTypeDetail.engineType]
    let defaultRunConf = getDefaultRunConf ? getDefaultRunConf() : {}
    detail.runConf = { ...defaultRunConf, ...runConf }
  }
}

function versionMatch(patterns: string[], version: string) {
  for (let pattern of patterns) {
    if (minimatch(version, pattern)) {
      return true
    }
  }
  return false
}

const jobTypeDetail = computed(() => jobTypeMap[detail.jobType])

const engineOptions = computed(() => {
  let result = new Map<string, any>()

  if (!jobTypeDetail.value)
    return []

  for (let engine of engineList.value) {
    if (jobTypeDetail.value.engineType !== engine.name || !versionMatch(jobTypeDetail.value.engineVersions, engine.version)) {
      continue
    }
    if (result.has(engine.clusterId)) {
      result.get(engine.clusterId).push({ label: `${engine.name} ${engine.version}`, value: engine.id })
    } else {
      result.set(engine.clusterId, [{ label: `${engine.name} ${engine.version}`, value: engine.id }])
    }
  }
  return Array.from(result).map(([clusterId, engines]) => ({ label: clusterOptionMap[clusterId], options: engines }))
})


let initialized = false
const initPageConf = () => {
  if (initialized) return
  initialized = true

  if (operateType === DataOperationType.create) {
    detail.jobType = jobType
    setJobConf({}, {}, jobType)
  } else {
    getDetail({ id: id }, operateType === DataOperationType.update)
  }
}
onBeforeMount(() => initPageConf())
onMounted(() => initPageConf())
onBeforeUpdate(() => initPageConf())

</script>

<template>
  <UpsertCard back-url="/job">
    <template #title>
      <template v-if="detail.id === undefined">
        <a-typography-text>{{ jobTypeOptionMap[detail.jobType] }}</a-typography-text>
      </template>
      <template v-else>
        <a-typography-text>{{ detail.jobName }}</a-typography-text>
        <a-typography-text type="secondary">({{ jobTypeOptionMap[detail.jobType] }})</a-typography-text>
      </template>
    </template>
    <JobUpsertForm ref="detailFormRef" v-model="detail"
                   :engine-options="engineOptions"
                   :job-type="jobTypeDetail"
                   @save="upsert(operateType, false)">
      <template #buttons>
        <a-button type="primary" @click="upsert(operateType)">保存并返回</a-button>
      </template>
    </JobUpsertForm>
  </UpsertCard>
</template>

<style scoped></style>