<script setup lang="ts">
import { computed, reactive, ref, toRaw, watch } from "vue"
import { request } from "@/utils/request-utils"

import jobs from "@/composables/jobs"
import { useBreadcrumbStore } from "@/stores/breadcrumbs"

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
import SampleData from "@/assets/sample-data"

const {setBreadcrumb} = useBreadcrumbStore()
setBreadcrumb([{text: '数据计算配置-分组方式'}])

const jobConf = reactive({
  ...SampleData.SampleConfig
})

const code = ref("")
const codeView = ref(false)
const pageConfig = reactive({
  sources: Array<any>(),
  transformations: Array<any>(),
  sinks: Array<any>()
} as any)

for (let config in SampleData.SampleConfig) {
  let arr = SampleData.SampleConfig[config]
  for (let i = 0; i < arr.length; i++) {
    pageConfig[config].push({expand: true})
  }
}

const getTargetPlugins = (type: string): any[] => {
  switch (type) {
    case 'source':
      return jobConf.sources
    case 'transformation':
      return jobConf.transformations
    case 'sink':
      return jobConf.sinks
    default:
      return []
  }
}

const getTargetPluginPageConfig = (type: string): any[] => {
  switch (type) {
    case 'source':
      return pageConfig.sources
    case 'transformation':
      return pageConfig.transformations
    case 'sink':
      return pageConfig.sinks
    default:
      return []
  }
}

const addPlugin = (type: string, name: string, index: number = -1) => {
  let plugin = jobs.SparkDataCalcModels[type][name]()
  let plugins = getTargetPlugins(type)
  let pluginPageConfig = getTargetPluginPageConfig(type)
  if (index < 0 || index >= plugins.length) {
    pluginPageConfig.push({expand: true});
    plugins.push(plugin);
  } else {
    pluginPageConfig.splice(index, 0, {expand: true});
    plugins.splice(index, 0, plugin);
  }
}

const delPlugin = (type: string, index: number = -1) => {
  let plugins = getTargetPlugins(type)
  let pluginPageConfig = getTargetPluginPageConfig(type)
  if (index >= 0 && index < plugins.length) {
    plugins.splice(index, 1)
    pluginPageConfig.splice(index, 1)
  }
}

const variables = reactive({})
const getCode = (showCode: boolean) => {
  if (showCode) {
    const requestConfig = {
      url: "/ftl/get-group-code",
      method: 'POST',
      data: toRaw(jobConf)
    }
    request(requestConfig).then(response => {
      code.value = response
    })
  } else {
    code.value = ''
  }
}

watch(codeView, () => getCode(codeView.value))

const previewCode = computed(() => codeView.value ? code.value : JSON.stringify(jobConf, null, 2))

</script>

<template>
  <a-row style="height: 100%; padding: 10px">
    <a-col :span="12" style="height: 100%; overflow: auto;">
      <a-typography-title :level="4">全局变量</a-typography-title>
      <a-input-string-map v-model:value="jobConf.variables" />

      <a-divider />

      <a-typography-title :level="4">输入</a-typography-title>
      <template v-for="(item, index) in jobConf.sources" :key="`source_${index}`">
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
          <DorisSource v-if="item.name == 'doris'" v-model:value="item.config" :name="`source_${index}`"
                       v-show="pageConfig.sources[index].expand" />
          <FileSource v-else-if="item.name == 'file'" v-model:value="item.config" :name="`source_${index}`"
                      v-show="pageConfig.sources[index].expand" />
          <JdbcSource v-else-if="item.name == 'jdbc'" v-model:value="item.config" :name="`source_${index}`"
                      v-show="pageConfig.sources[index].expand" />
          <ManagedJdbcSource v-else-if="item.name == 'managed_jdbc'" v-model:value="item.config"
                             :name="`source_${index}`"
                             v-show="pageConfig.sources[index].expand" />
          <StarRocksSource v-else-if="item.name == 'star_rocks'" v-model:value="item.config" :name="`source_${index}`"
                           v-show="pageConfig.sources[index].expand" />
        </a-card>
      </template>
      <AddPlugin plugin-type="source" @add="name => addPlugin('source', name)" />

      <a-divider />

      <a-typography-title :level="4">转换</a-typography-title>
      <template v-for="(item, index) in jobConf.transformations" :key="`transformation_${index}`">
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
          <SqlTransformation v-if="item.name == 'sql'" v-model:value="item.config"
                             :name="`transformation_${index}`" v-show="pageConfig.transformations[index].expand" />
        </a-card>
      </template>
      <AddPlugin plugin-type="transformation" @add="(name: string) => addPlugin('transformation', name)" />

      <a-divider />

      <a-typography-title :level="4">输出</a-typography-title>
      <template v-for="(item, index) in jobConf.sinks" :key="`sink_${index}`">
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
              <a-tag  color="blue">sink</a-tag>
            </span>
          </a-flex>
          <!--<component :is="`${item.name.replace('_', '-')}-sink`" v-model:value="item.config" :name="`sink_${index}`"
            v-show="pageConfig.sinks[index].expand" />-->
          <DorisSink v-if="item.name == 'doris'" v-model:value="item.config" :name="`source_${index}`"
                     v-show="pageConfig.sinks[index].expand" />
          <FileSink v-else-if="item.name == 'file'" v-model:value="item.config" :name="`source_${index}`"
                    v-show="pageConfig.sinks[index].expand" />
          <HiveSink v-else-if="item.name == 'hive'" v-model:value="item.config" :name="`source_${index}`"
                    v-show="pageConfig.sinks[index].expand" />
          <JdbcSink v-else-if="item.name == 'jdbc'" v-model:value="item.config" :name="`source_${index}`"
                    v-show="pageConfig.sinks[index].expand" />
          <ManagedJdbcSink v-else-if="item.name == 'managed_jdbc'" v-model:value="item.config" :name="`source_${index}`"
                           v-show="pageConfig.sinks[index].expand" />
          <StarRocksSink v-else-if="item.name == 'star_rocks'" v-model:value="item.config" :name="`source_${index}`"
                         v-show="pageConfig.sinks[index].expand" />
        </a-card>
      </template>
      <AddPlugin plugin-type="sink" @add="(name: string) => addPlugin('sink', name)" />
    </a-col>
    <a-col :span="12" style="height: 100%">
      <a-radio-group v-model:value="codeView">
        <a-radio-button :value="false">JSON</a-radio-button>
        <a-radio-button :value="true">Scala</a-radio-button>
      </a-radio-group>
      <a-button type="link" v-if="codeView" @click="() => getCode(true)">
        <template #icon>
          <reload-outlined />
        </template>
      </a-button>
      <pre v-html="previewCode" style="height: calc(100% - 24px); width: 100%; overflow: auto" />
    </a-col>
  </a-row>
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