<script setup lang="ts">
import { onBeforeMount, onMounted, onBeforeUpdate, reactive } from "vue"
import { v4 as uuidv4 } from 'uuid'

import jobs from "@/composables/jobs"

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

import AddPlugin from "@/components/job/spark/data-calc/AddTypedPlugin.vue"
import PluginOperations from "@/components/job/spark/data-calc/PluginOperations.vue"
import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"
import type { validateFunction } from "@/composables/models";

interface SparkDataCalcConf {
  variables: any
  plugins: Array<any>
}

interface PageConfigItem {
  expand: boolean
  key: string
}

const {jobConf} = defineProps<{
  jobConf: SparkDataCalcConf,
}>()

const pageConfig = reactive<Array<PageConfigItem>>([])

const validateFunc = reactive<validateFunction[]>([])

let initialized = false
const initPageConf = () => {
  if (initialized) return
  initialized = true
  pageConfig.push(...jobConf.plugins.map((_) => ({expand: true, key: uuidv4()})))
}
onBeforeMount(() => initPageConf())
onMounted(() => initPageConf())
onBeforeUpdate(() => initPageConf())

const addPlugin = (type: string, name: string, index: number = -1) => {
  let plugin = jobs.SparkDataCalcModels[type][name]()
  let plugins = jobConf.plugins
  if (index < 0 || index >= plugins.length) {
    pageConfig.push({expand: true, key: uuidv4()});
    plugins.push(plugin);
  } else {
    pageConfig.splice(index, 0, {expand: true, key: uuidv4()});
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

const pushValidateFun = (validateFun: validateFunction, index: number) => {
  validateFunc.splice(index, 0, validateFun)
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
    <AddPlugin @add="(type: string, name: string) => addPlugin(type, name)" />
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
              输出表名: {{ item.config.targetDatabase }}.{{ item.config.targetTable }}
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
      <!--<component :is="`${item.name.replace('_', '-')}-${item.type}`" v-model:value="item.config" :name="`${item.type}_${index}`"
        v-show="pageConfig[index].expand" />-->
      <template v-if="item.type == 'source'">
        <DorisSource :ref="`source_${index}`" v-if="item.name == 'doris'" :value="item.config"
                     :name="`source_${index}`" v-show="pageConfig[index].expand"
                     @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
        <FileSource :ref="`source_${index}`" v-else-if="item.name == 'file'" :value="item.config"
                    :name="`source_${index}`" v-show="pageConfig[index].expand"
                    @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
        <JdbcSource :ref="`source_${index}`" v-else-if="item.name == 'jdbc'" :value="item.config"
                    :name="`source_${index}`" v-show="pageConfig[index].expand"
                    @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
        <ManagedJdbcSource :ref="`source_${index}`" v-else-if="item.name == 'managed_jdbc'" :value="item.config"
                           :name="`source_${index}`" v-show="pageConfig[index].expand"
                           @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
        <StarRocksSource :ref="`source_${index}`" v-else-if="item.name == 'star_rocks'" :value="item.config"
                         :name="`source_${index}`" v-show="pageConfig[index].expand"
                         @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
      </template>
      <template v-else-if="item.type == 'transformation'">
        <SqlTransformation :ref="`transformation_${index}`" v-if="item.name == 'sql'" v-model:value="item.config"
                           :name="`transformation_${index}`" v-show="pageConfig[index].expand"
                           @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
      </template>
      <template v-else-if="item.type == 'sink'">
        <DorisSink :ref="`sink_${index}`" v-if="item.name == 'doris'" v-model:value="item.config"
                   :name="`sink_${index}`" v-show="pageConfig[index].expand"
                   @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
        <FileSink :ref="`sink_${index}`" v-else-if="item.name == 'file'" v-model:value="item.config"
                  :name="`sink_${index}`" v-show="pageConfig[index].expand"
                  @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
        <HiveSink :ref="`sink_${index}`" v-else-if="item.name == 'hive'" v-model:value="item.config"
                  :name="`sink_${index}`" v-show="pageConfig[index].expand"
                  @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
        <JdbcSink :ref="`sink_${index}`" v-else-if="item.name == 'jdbc'" v-model:value="item.config"
                  :name="`sink_${index}`" v-show="pageConfig[index].expand"
                  @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
        <ManagedJdbcSink :ref="`sink_${index}`" v-else-if="item.name == 'managed_jdbc'" v-model:value="item.config"
                         :name="`sink_${index}`" v-show="pageConfig[index].expand"
                         @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
        <StarRocksSink :ref="`sink_${index}`" v-else-if="item.name == 'star_rocks'" v-model:value="item.config"
                       :name="`sink_${index}`" v-show="pageConfig[index].expand"
                       @push-validated="(validateFun: validateFunction) => pushValidateFun(validateFun, index)" />
      </template>
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