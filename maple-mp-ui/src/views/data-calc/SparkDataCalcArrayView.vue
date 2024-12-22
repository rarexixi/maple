<script setup lang="ts">
import { computed, reactive, ref, toRaw, watch } from "vue"
import { request } from "@/utils/request-utils"

import jobs from "@/composables/spark-jobs"
import { useBreadcrumbStore } from "@/stores/breadcrumbs"

import FileSource from "@/components/job/spark/data-calc/source/FileSource.vue"
import JdbcSource from "@/components/job/spark/data-calc/source/JdbcSource.vue"
import DorisSource from "@/components/job/spark/data-calc/source/DorisSource.vue"
import StarRocksSource from "@/components/job/spark/data-calc/source/StarRocksSource.vue"
import SqlTransformation from "@/components/job/spark/data-calc/transformation/SqlTransformation.vue"
import FileSink from "@/components/job/spark/data-calc/sink/FileSink.vue"
import JdbcSink from "@/components/job/spark/data-calc/sink/JdbcSink.vue"
import DorisSink from "@/components/job/spark/data-calc/sink/DorisSink.vue"
import StarRocksSink from "@/components/job/spark/data-calc/sink/StarRocksSink.vue"
import HiveSink from "@/components/job/spark/data-calc/sink/HiveSink.vue"

import AddPlugin from "@/components/job/spark/data-calc/AddTypedPlugin.vue"
import PluginOperations from "@/components/job/spark/data-calc/PluginOperations.vue"
import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue"

import SampleData from "@/assets/sample-data"

const {setBreadcrumb} = useBreadcrumbStore()
setBreadcrumb([{text: '数据计算配置-数组方式'}])

const jobConf = reactive({
  ...SampleData.SampleArrayConfig
})

const code = ref("")
const codeView = ref(false)
const pageConfig = reactive(Array<any>())

for (let i = 0; i < jobConf.plugins.length; i++) {
  pageConfig.push({expand: true})
}

const addPlugin = (type: string, name: string, index: number = -1) => {
  let plugin = jobs.SparkDataCalcModels[type][name]()
  let plugins = jobConf.plugins
  if (index < 0 || index >= plugins.length) {
    pageConfig.push({expand: true});
    plugins.push(plugin);
  } else {
    pageConfig.splice(index, 0, {expand: true});
    plugins.splice(index, 0, plugin);
  }
}

const delPlugin = (index: number = -1) => {
  let plugins = jobConf.plugins
  if (index >= 0 && index < plugins.length) {
    plugins.splice(index, 1)
    pageConfig.splice(index, 1)
  }
}

const getCode = (showCode: boolean) => {
  if (showCode) {
    const requestConfig = {
      url: "/ftl/get-array-code",
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
      <template v-for="(item, index) in jobConf.plugins" :key="`${item.type}_${index}`">
        <AddPlugin @add="(type: string, name: string) => addPlugin(type, name, index)" />
        <a-card :class="pageConfig[index].expand ? 'card-open' : 'card-close'">
          <a-flex :justify="'space-between'" :align="'center'" class="card-header">
            <span>
              <PluginOperations v-model:value="pageConfig[index]" :index="index"
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
            </span>
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
            <DorisSource v-if="item.name == 'doris'" v-model:value="item.config" :name="`source_${index}`"
                         v-show="pageConfig[index].expand" />
            <FileSource v-else-if="item.name == 'file'" v-model:value="item.config" :name="`source_${index}`"
                        v-show="pageConfig[index].expand" />
            <JdbcSource v-else-if="item.name == 'jdbc'" v-model:value="item.config" :name="`source_${index}`"
                        v-show="pageConfig[index].expand" />
            <StarRocksSource v-else-if="item.name == 'starrocks'" v-model:value="item.config" :name="`source_${index}`"
                             v-show="pageConfig[index].expand" />
          </template>
          <template v-else-if="item.type == 'transformation'">
            <SqlTransformation v-if="item.name == 'sql'" v-model:value="item.config"
                               :name="`transformation_${index}`" v-show="pageConfig[index].expand" />
          </template>
          <template v-else-if="item.type == 'sink'">
            <DorisSink v-if="item.name == 'doris'" v-model:value="item.config" :name="`source_${index}`"
                       v-show="pageConfig[index].expand" />
            <FileSink v-else-if="item.name == 'file'" v-model:value="item.config" :name="`source_${index}`"
                      v-show="pageConfig[index].expand" />
            <HiveSink v-else-if="item.name == 'hive'" v-model:value="item.config" :name="`source_${index}`"
                      v-show="pageConfig[index].expand" />
            <JdbcSink v-else-if="item.name == 'jdbc'" v-model:value="item.config" :name="`source_${index}`"
                      v-show="pageConfig[index].expand" />
            <StarRocksSink v-else-if="item.name == 'starrocks'" v-model:value="item.config" :name="`source_${index}`"
                           v-show="pageConfig[index].expand" />
          </template>
        </a-card>
      </template>
      <AddPlugin @add="(type: string, name: string) => addPlugin(type, name)" />
    </a-col>
    <a-col :span="12" style="height: 100%">
      <a-radio-group v-model:value="codeView">
        <a-radio-button :value="false">JSON</a-radio-button>
        <a-radio-button :value="true">Scala</a-radio-button>
      </a-radio-group>
      <a-button type="link" v-if="codeView" @click="() => getCode(true)">
        <template #icon>
          <ReloadOutlined />
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