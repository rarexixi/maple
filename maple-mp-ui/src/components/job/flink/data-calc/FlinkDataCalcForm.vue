<script setup lang="ts">
import { onBeforeMount, onMounted, onBeforeUpdate, ref, reactive, defineAsyncComponent } from "vue"
import { v4 as uuidv4 } from 'uuid'

import jobs from "@/composables/flink-jobs"

import AddPlugin from "@/components/job/flink/data-calc/AddTypedPlugin.vue"
import PluginOperations from "@/components/job/flink/data-calc/PluginOperations.vue"
import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"
import type { validateFunction } from "@/composables/models"
import { useDatasourceStore } from "@/stores/sys-data";

interface FlinkDataCalcConf {
  variables: any
  plugins: Array<any>
}

interface PageConfigItem {
  expand: boolean
  key: string
}

const { jobConf } = defineProps<{
  jobConf: FlinkDataCalcConf,
}>()

const pageConfig = reactive<Array<PageConfigItem>>([])
const validateFunc = reactive<validateFunction[]>([])
const collapsed = ref<boolean>(false)

const { dataMap: datasourceMap } = useDatasourceStore()

let initialized = false
const initPageConf = () => {
  if (initialized || !jobConf.plugins) return
  initialized = true
  pageConfig.push(...jobConf.plugins.map((_) => ({ expand: true, key: uuidv4() })))
}
onBeforeMount(() => initPageConf())
onMounted(() => initPageConf())
onBeforeUpdate(() => initPageConf())

const addPlugin = (type: string, name: string, index: number = -1) => {
  let plugin = jobs.FlinkDataCalcModels[type][name]()
  let plugins = jobConf.plugins
  if (index < 0 || index >= plugins.length) {
    pageConfig.push({ expand: true, key: uuidv4() });
    plugins.push(plugin);
  } else {
    pageConfig.splice(index, 0, { expand: true, key: uuidv4() });
    plugins.splice(index, 0, plugin);
  }
}

const delPlugin = (index: number = -1) => {
  let plugins = jobConf.plugins
  if (index >= 0 && index < plugins.length) {
    plugins.splice(index, 1)
    pageConfig.splice(index, 1)
    validateFunc.splice(index, 1)
  }
}

let pluginMap: any = {
  'mysql-cdc-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/MysqlCdcSource.vue")),
  'postgres-cdc-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/PostgresCdcSource.vue")),
  'oracle-cdc-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/OracleCdcSource.vue")),
  'sqlserver-cdc-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/SqlServerCdcSource.vue")),
  'db2-cdc-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/Db2CdcSource.vue")),
  'oceanbase-cdc-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/OceanBaseCdcSource.vue")),
  'kafka-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/KafkaSource.vue")),
  'upsert-kafka-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/UpsertKafkaSource.vue")),
  'doris-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/DorisSource.vue")),
  'starrocks-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/StarRocksSource.vue")),
  'tidb-cdc-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/TidbCdcSource.vue")),
  'jdbc-source': defineAsyncComponent(() => import("@/components/job/flink/data-calc/source/JdbcSource.vue")),
  'sql-transformation': defineAsyncComponent(() => import("@/components/job/flink/data-calc/transformation/SqlTransformation.vue")),
  'kafka-sink': defineAsyncComponent(() => import("@/components/job/flink/data-calc/sink/KafkaSink.vue")),
  'upsert-kafka-sink': defineAsyncComponent(() => import("@/components/job/flink/data-calc/sink/UpsertKafkaSink.vue")),
  'doris-sink': defineAsyncComponent(() => import("@/components/job/flink/data-calc/sink/DorisSink.vue")),
  'starrocks-sink': defineAsyncComponent(() => import("@/components/job/flink/data-calc/sink/StarRocksSink.vue")),
  'jdbc-sink': defineAsyncComponent(() => import("@/components/job/flink/data-calc/sink/JdbcSink.vue")),
}

function getPlugin(jobType: string, type: string) {
  return pluginMap[jobType + '-' + type]
}

const pushValidateFun = (validateFun: validateFunction, index: number) => {
  validateFunc.splice(index, 0, validateFun)
}

async function validate(setValidated: (success: boolean) => void) {
  for (let validate of validateFunc) {
    await validate(setValidated)
  }
}

function getTableIdentifier(conf: any) {
  let table = conf.rdbmsTable
  if (!table || !(table.tableName)) return ''
  let tableIdentifier = table.tableName
  if (!!(table.schemaName)) {
    tableIdentifier = table.schemaName + "." + tableIdentifier
  }
  if (!!(table.databaseName)) {
    tableIdentifier = table.databaseName + "." + tableIdentifier
  }
  if (!!(conf.datasourceId)) {
    tableIdentifier = datasourceMap[conf.datasourceId].name + " / " + tableIdentifier
  }
  return tableIdentifier
}

function getPluginTitle(item: any) {
  if (item.type === 'sink') {
    if (item.name === 'file') {
      return '写入路径: ' + item.config.path
    } else if (item.name === 'kafka' || item.name === 'upsert-kafka') {
      return '写入主题: ' + item.config.topic
    } else {
      return '输出表名: ' + getTableIdentifier(item.config)
    }
  } else {
    return '注册表名：' + item.config.resultTable
  }
}

function getAnchorItem(item: any, index: number) {
  return {
    title: getPluginTitle(item),
    href: `#${pageConfig[index].key}`
  }
}

defineExpose({
  validate
})
</script>

<template>
  <a-typography-title :level="5">全局变量</a-typography-title>
  <a-input-string-map v-model:value="jobConf.variables" />
  <a-flex justify="flex-end" align="center" style="padding-left: 5px; margin-top: 5px">
    <a-switch v-model:checked="collapsed" />
    <span>快速导航</span>
  </a-flex>
  <a-flex>
    <div>
      <template v-for="(item, index) in jobConf.plugins" :key="pageConfig[index].key">
        <AddPlugin @add="(type: string, name: string) => addPlugin(type, name, index)" />
        <a-card :class="pageConfig[index].expand ? 'card-open' : 'card-close'">
          <a-flex :justify="'space-between'" :align="'center'" class="card-header">
            <div :id="pageConfig[index].key">
              <PluginOperations v-model:expand="pageConfig[index].expand" :index="index"
                                @delete="() => delPlugin(index)" />
              {{ getPluginTitle(item) }}
            </div>
            <div>
              {{ item.name }} -
              <a-tag color="green" v-if="item.type == 'source'">{{ item.type }}</a-tag>
              <a-tag color="orange" v-else-if="item.type == 'transformation'">{{ item.type }}</a-tag>
              <a-tag color="blue" v-else-if="item.type == 'sink'">{{ item.type }}</a-tag>
            </div>
          </a-flex>
          <component :is="getPlugin(item.name, item.type)" v-model:value="item.config"
                     :name="`${item.type}_${index}`" v-show="pageConfig[index].expand"
                     @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
        </a-card>
      </template>
      <AddPlugin @add="(type: string, name: string) => addPlugin(type, name)" />
    </div>
    <div style="width: 300px; padding-left: 5px" v-if="collapsed">
      <a-anchor :offset-top="64" :items="jobConf.plugins.map(getAnchorItem)" />
    </div>
  </a-flex>
</template>

<style lang="less" scoped>
pre {
  font-size: 12px;
}

textarea {
  font-family: "SFMono-Regular", Consolas, "Liberation Mono", Menlo, Courier,
  monospace;
  font-size: 12px;
}
</style>