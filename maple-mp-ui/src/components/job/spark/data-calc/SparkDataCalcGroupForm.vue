<script setup lang="ts">
import { onBeforeMount, onMounted, onBeforeUpdate, reactive } from "vue"
import { v4 as uuidv4 } from 'uuid'

import jobs from "@/composables/jobs"
import type { validateFunction } from "@/composables/models"

import FileSource from "@/components/job/spark/data-calc/source/FileSource.vue"
import JdbcSource from "@/components/job/spark/data-calc/source/JdbcSource.vue"
import DorisSource from "@/components/job/spark/data-calc/source/DorisSource.vue"
import StarRocksSource from "@/components/job/spark/data-calc/source/StarRocksSource.vue"
import ManagedJdbcSource from "@/components/job/spark/data-calc/source/ManagedJdbcSource.vue"
import SqlTransformation from "@/components/job/spark/data-calc/transformation/SqlTransformation.vue"
import FileSink from "@/components/job/spark/data-calc/sink/FileSink.vue"
import JdbcSink from "@/components/job/spark/data-calc/sink/JdbcSink.vue"
import DorisSink from "@/components/job/spark/data-calc/sink/DorisSink.vue"
import StarRocksSink from "@/components/job/spark/data-calc/sink/StarRocksSink.vue"
import ManagedJdbcSink from "@/components/job/spark/data-calc/sink/ManagedJdbcSink.vue"
import HiveSink from "@/components/job/spark/data-calc/sink/HiveSink.vue"

import AddPlugin from "@/components/job/spark/data-calc/AddPlugin.vue"
import PluginOperations from "@/components/job/spark/data-calc/PluginOperations.vue"
import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"

interface SparkDataCalcConf {
  variables: any
  sources: Array<any>
  transformations: Array<any>
  sinks: Array<any>
}

interface PageConfigItem {
  expand: boolean
  key: string
}

interface PageConfig {
  sources: Array<PageConfigItem>
  transformations: Array<PageConfigItem>
  sinks: Array<PageConfigItem>
}

interface ValidateFuncConfig {
  sources: Array<validateFunction>
  transformations: Array<validateFunction>
  sinks: Array<validateFunction>
}

const {jobConf} = defineProps<{
  jobConf: SparkDataCalcConf,
}>()

const pageConfig = reactive<PageConfig>({
  sources: [],
  transformations: [],
  sinks: [],
})

const validateFunc = reactive<ValidateFuncConfig>({sources: [], transformations: [], sinks: [],})

const getTargetPlugins = (type: string) => {
  if (type === 'source')
    return jobConf.sources
  if (type === 'transformation')
    return jobConf.transformations
  return jobConf.sinks
}

const getTargetPluginPageConfig = (type: string) => {
  if (type === 'source')
    return pageConfig.sources
  if (type === 'transformation')
    return pageConfig.transformations
  return pageConfig.sinks
}

const getValidates = (type: string) => {
  if (type === 'source')
    return validateFunc.sources
  if (type === 'transformation')
    return validateFunc.transformations
  return validateFunc.sinks
}

let initialized = false
const initPageConf = () => {
  if (initialized) return
  initialized = true

  function newPageConfig(): PageConfigItem {
    return {expand: true, key: uuidv4()}
  }

  if (jobConf.sources) pageConfig.sources.push(...jobConf.sources.map((_) => newPageConfig()))
  if (jobConf.transformations) pageConfig.transformations.push(...jobConf.transformations.map((_) => newPageConfig()))
  if (jobConf.sinks) pageConfig.sinks.push(...jobConf.sinks.map((_) => newPageConfig()))
}
onBeforeMount(() => initPageConf())
onMounted(() => initPageConf())
onBeforeUpdate(() => initPageConf())

const addPlugin = (type: string, name: string, index: number = -1) => {
  console.log(type, name)
  let plugin = jobs.SparkDataCalcModels[type][name]()
  let plugins = getTargetPlugins(type)
  let pluginPageConfig = getTargetPluginPageConfig(type)
  if (index < 0 || index >= plugins.length) {
    pluginPageConfig.push({expand: true, key: uuidv4()});
    plugins.push(plugin);
  } else {
    pluginPageConfig.splice(index, 0, {expand: true, key: uuidv4()});
    plugins.splice(index, 0, plugin);
  }
}

const delPlugin = (type: string, index: number = -1) => {
  let plugins = getTargetPlugins(type)
  let pluginPageConfig = getTargetPluginPageConfig(type)
  let validates = getValidates(type);
  if (index >= 0 && index < plugins.length) {
    plugins.splice(index, 1)
    pluginPageConfig.splice(index, 1)
    validates.splice(index, 1)
  }
}

const pushValidateFun = (type: string, validateFun: validateFunction, index: number) => {
  getValidates(type).splice(index, 0, validateFun)
}

async function validate(setValidated: (success: boolean) => void) {
  async function validate(validates: validateFunction[]) {
    for (let validate of validates) {
      await validate(setValidated)
    }
  }

  await validate(validateFunc.sources)
  await validate(validateFunc.transformations)
  await validate(validateFunc.sinks)
}

defineExpose({
  validate
})
</script>

<template>
  <a-typography-title :level="5">全局变量</a-typography-title>
  <a-input-string-map v-model:value="jobConf.variables" />

  <a-divider />

  <a-typography-title :level="4">输入</a-typography-title>
  <template v-for="(item, index) in jobConf.sources" :key="pageConfig.sources[index].key">
    <AddPlugin plugin-type="source" @add="(name: string) => addPlugin('source', name, index)" />
    <a-card :class="pageConfig.sources[index].expand ? 'card-open' : 'card-close'">
      <a-flex :justify="'space-between'" :align="'center'" class="card-header">
        <span>
          <PluginOperations v-model:expand="pageConfig.sources[index].expand" :index="index"
                            @delete="() => delPlugin('source', index)" />
          注册表名：{{ item.config.resultTable }}
        </span>
        <span>
          {{ item.name }} -
          <a-tag color="green">source</a-tag>
        </span>
      </a-flex>
      <!--<component :is="`${item.name.replace('_', '-')}-source`" v-model:value="item.config" :name="`source_${index}`"
        v-show="pageConfig.sources[index].expand" />-->
      <DorisSource :ref="`source_${index}`" v-if="item.name == 'doris'" :value="item.config"
                   :name="`source_${index}`" v-show="pageConfig.sources[index].expand"
                   @push-validated="(validateFun: validateFunction) => pushValidateFun('source', validateFun, index)" />
      <FileSource :ref="`source_${index}`" v-else-if="item.name == 'file'" :value="item.config"
                  :name="`source_${index}`" v-show="pageConfig.sources[index].expand"
                  @push-validated="(validateFun: validateFunction) => pushValidateFun('source', validateFun, index)" />
      <JdbcSource :ref="`source_${index}`" v-else-if="item.name == 'jdbc'" :value="item.config"
                  :name="`source_${index}`" v-show="pageConfig.sources[index].expand"
                  @push-validated="(validateFun: validateFunction) => pushValidateFun('source', validateFun, index)" />
      <ManagedJdbcSource :ref="`source_${index}`" v-else-if="item.name == 'managed_jdbc'" :value="item.config"
                         :name="`source_${index}`" v-show="pageConfig.sources[index].expand"
                         @push-validated="(validateFun: validateFunction) => pushValidateFun('source', validateFun, index)" />
      <StarRocksSource :ref="`source_${index}`" v-else-if="item.name == 'star_rocks'" :value="item.config"
                       :name="`source_${index}`" v-show="pageConfig.sources[index].expand"
                       @push-validated="(validateFun: validateFunction) => pushValidateFun('source', validateFun, index)" />
    </a-card>
  </template>
  <AddPlugin plugin-type="source" @add="name => addPlugin('source', name)" />

  <a-divider />

  <a-typography-title :level="4">转换</a-typography-title>
  <template v-for="(item, index) in jobConf.transformations" :key="pageConfig.transformations[index].key">
    <AddPlugin plugin-type="transformation"
               @add="(name: string) => addPlugin('transformation', name, index)" />
    <a-card :class="pageConfig.transformations[index].expand ? 'card-open' : 'card-close'">
      <a-flex :justify="'space-between'" :align="'center'" class="card-header">
        <span>
          <PluginOperations v-model:expand="pageConfig.transformations[index].expand" :index="index"
                            @delete="() => delPlugin('transformation', index)" />
          注册表名：{{ item.config.resultTable }}
        </span>
        <span>
          {{ item.name }} -
          <a-tag color="orange">transform</a-tag>
        </span>
      </a-flex>
      <!--<component :is="`${item.name.replace('_', '-')}-transformation`" v-model:value="item.config"
        :name="`transformation_${index}`" v-show="pageConfig.transformations[index].expand" />-->
      <SqlTransformation :ref="`transformation_${index}`" v-if="item.name == 'sql'" v-model:value="item.config"
                         :name="`transformation_${index}`" v-show="pageConfig.transformations[index].expand"
                         @push-validated="(validateFun: validateFunction) => pushValidateFun('transformation', validateFun, index)" />
    </a-card>
  </template>
  <AddPlugin plugin-type="transformation" @add="(name: string) => addPlugin('transformation', name)" />

  <a-divider />

  <a-typography-title :level="4">输出</a-typography-title>
  <template v-for="(item, index) in jobConf.sinks" :key="pageConfig.sinks[index].key">
    <AddPlugin plugin-type="sink" @add="name => addPlugin('sink', name, index)" />
    <a-card :class="pageConfig.sinks[index].expand ? 'card-open' : 'card-close'">
      <a-flex :justify="'space-between'" :align="'center'" class="card-header">
        <span>
          <PluginOperations v-model:expand="pageConfig.sinks[index].expand" :index="index"
                            @delete="() => delPlugin('sink', index)" />
          <template v-if="item.name === 'file'">
            写入路径: {{ item.config.path }}
          </template>
          <template v-else>
            输出表名: {{ item.config.targetDatabase }}.{{ item.config.targetTable }}
          </template>
        </span>
        <span>
          {{ item.name }} -
          <a-tag color="blue">sink</a-tag>
        </span>
      </a-flex>
      <!--<component :is="`${item.name.replace('_', '-')}-sink`" v-model:value="item.config" :name="`sink_${index}`"
        v-show="pageConfig.sinks[index].expand" />-->
      <DorisSink :ref="`sink_${index}`" v-if="item.name == 'doris'" v-model:value="item.config"
                 :name="`sink_${index}`" v-show="pageConfig.sinks[index].expand"
                 @push-validated="(validateFun: validateFunction) => pushValidateFun('sink', validateFun, index)" />
      <FileSink :ref="`sink_${index}`" v-else-if="item.name == 'file'" v-model:value="item.config"
                :name="`sink_${index}`" v-show="pageConfig.sinks[index].expand"
                @push-validated="(validateFun: validateFunction) => pushValidateFun('sink', validateFun, index)" />
      <HiveSink :ref="`sink_${index}`" v-else-if="item.name == 'hive'" v-model:value="item.config"
                :name="`sink_${index}`" v-show="pageConfig.sinks[index].expand"
                @push-validated="(validateFun: validateFunction) => pushValidateFun('sink', validateFun, index)" />
      <JdbcSink :ref="`sink_${index}`" v-else-if="item.name == 'jdbc'" v-model:value="item.config"
                :name="`sink_${index}`" v-show="pageConfig.sinks[index].expand"
                @push-validated="(validateFun: validateFunction) => pushValidateFun('sink', validateFun, index)" />
      <ManagedJdbcSink :ref="`sink_${index}`" v-else-if="item.name == 'managed_jdbc'" v-model:value="item.config"
                       :name="`sink_${index}`" v-show="pageConfig.sinks[index].expand"
                       @push-validated="(validateFun: validateFunction) => pushValidateFun('sink', validateFun, index)" />
      <StarRocksSink :ref="`sink_${index}`" v-else-if="item.name == 'star_rocks'" v-model:value="item.config"
                     :name="`sink_${index}`" v-show="pageConfig.sinks[index].expand"
                     @push-validated="(validateFun: validateFunction) => pushValidateFun('sink', validateFun, index)" />
    </a-card>
  </template>
  <AddPlugin plugin-type="sink" @add="(name: string) => addPlugin('sink', name)" />
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