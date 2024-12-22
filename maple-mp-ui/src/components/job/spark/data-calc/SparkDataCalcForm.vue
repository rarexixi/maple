<script setup lang="ts">
import { onBeforeMount, onMounted, onBeforeUpdate, reactive, defineAsyncComponent } from "vue"
import { v4 as uuidv4 } from 'uuid'

import jobs from "@/composables/spark-jobs"

import AddPlugin from "@/components/job/spark/data-calc/AddTypedPlugin.vue"
import PluginOperations from "@/components/job/spark/data-calc/PluginOperations.vue"
import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"
import type { validateFunction } from "@/composables/models"

interface SparkDataCalcConf {
  variables: any
  plugins: Array<any>
}

interface PageConfigItem {
  expand: boolean
  key: string
}

const { jobConf } = defineProps<{
  jobConf: SparkDataCalcConf,
}>()

const pageConfig = reactive<Array<PageConfigItem>>([])

const validateFunc = reactive<validateFunction[]>([])

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
  let plugin = jobs.SparkDataCalcModels[type][name]()
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

const pluginMap: any = {
  'file-source': defineAsyncComponent(() => import("@/components/job/spark/data-calc/source/FileSource.vue")),
  'jdbc-source': defineAsyncComponent(() => import("@/components/job/spark/data-calc/source/JdbcSource.vue")),
  'doris-source': defineAsyncComponent(() => import("@/components/job/spark/data-calc/source/DorisSource.vue")),
  'starrocks-source': defineAsyncComponent(() => import("@/components/job/spark/data-calc/source/StarRocksSource.vue")),
  'sql-transformation': defineAsyncComponent(() => import("@/components/job/spark/data-calc/transformation/SqlTransformation.vue")),
  'file-sink': defineAsyncComponent(() => import("@/components/job/spark/data-calc/sink/FileSink.vue")),
  'jdbc-sink': defineAsyncComponent(() => import("@/components/job/spark/data-calc/sink/JdbcSink.vue")),
  'doris-sink': defineAsyncComponent(() => import("@/components/job/spark/data-calc/sink/DorisSink.vue")),
  'starrocks-sink': defineAsyncComponent(() => import("@/components/job/spark/data-calc/sink/StarRocksSink.vue")),
  'hive-sink': defineAsyncComponent(() => import("@/components/job/spark/data-calc/sink/HiveSink.vue")),
}

function getPlugin(jobType: string, type: string) {
  console.log(pluginMap[jobType + '-' + type])
  return pluginMap[jobType + '-' + type]
}

const pushValidateFun = (validateFun: validateFunction, index: number) => {
  validateFunc.splice(index, 0, validateFun)
}

function getTableIdentifier(table: any) {
  if (!table || !(table.tableName)) return ''
  let tableIdentifier = table.tableName
  if (!!(table.schemaName)) {
    tableIdentifier = table.schemaName + "." +tableIdentifier
  }
  if (!!(table.databaseName)) {
    tableIdentifier = table.databaseName + "." +tableIdentifier
  }
  return tableIdentifier
}

async function validate(setValidated: (success: boolean) => void) {
  for (let validate of validateFunc) {
    await validate(setValidated)
  }
}

defineExpose({
  validate
})
</script>

<template>
  <a-typography-title :level="5">全局变量</a-typography-title>
  <a-input-string-map v-model:value="jobConf.variables" />

  <a-divider />

  <template v-for="(item, index) in jobConf.plugins" :key="pageConfig[index].key">
    <AddPlugin @add="(type: string, name: string) => addPlugin(type, name, index)" />
    <a-card :class="pageConfig[index].expand ? 'card-open' : 'card-close'">
      <a-flex :justify="'space-between'" :align="'center'" class="card-header">
        <div>
          <PluginOperations v-model:expand="pageConfig[index].expand" :index="index"
                            @delete="() => delPlugin(index)" />
          <template v-if="item.type == 'sink'">
            <template v-if="item.name === 'file'">
              写入路径: {{ item.config.path }}
            </template>
            <template v-else>
              输出表名: {{ getTableIdentifier(item.config.targetTable) }}
            </template>
          </template>
          <template v-else>
            注册表名：{{ item.config.resultTable }}
          </template>
        </div>
        <span>
          {{ item.name }} -
          <a-tag color="green" v-if="item.type == 'source'">{{ item.type }}</a-tag>
          <a-tag color="orange" v-else-if="item.type == 'transformation'">{{ item.type }}</a-tag>
          <a-tag color="blue" v-else-if="item.type == 'sink'">{{ item.type }}</a-tag>
        </span>
      </a-flex>
      <component :is="getPlugin(item.name, item.type)" v-model:value="item.config"
                 :name="`${item.type}_${index}`" v-show="pageConfig[index].expand"
                 @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
    </a-card>
  </template>
  <AddPlugin @add="(type: string, name: string) => addPlugin(type, name)" />
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