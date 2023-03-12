<script lang="ts">
import { defineComponent, reactive, ref, toRaw, watch } from "vue"
import FileSource from "@/components/source/FileSource.vue"
import JdbcSource from "@/components/source/JdbcSource.vue"
import ManagedJdbcSource from "@/components/source/ManagedJdbcSource.vue"
import SqlTransformation from "@/components/transformation/SqlTransformation.vue"
import FileSink from "@/components/sink/FileSink.vue"
import JdbcSink from "@/components/sink/JdbcSink.vue"
import ManagedJdbcSink from "@/components/sink/ManagedJdbcSink.vue"
import HiveSink from "@/components/sink/HiveSink.vue"
import AddPlugin from "@/components/AddPlugin.vue"
import PluginOperations from "@/components/PluginOperations.vue"
import InputStringMap from "@/components/InputStringMap.vue"
import SampleData from "@/assets/sample-data"
import { request } from "@/utils/request-utils";


export default defineComponent({
  components: {
    InputStringMap,
    AddPlugin,
    PluginOperations,
    FileSource,
    JdbcSource,
    ManagedJdbcSource,
    SqlTransformation,
    FileSink,
    JdbcSink,
    ManagedJdbcSink,
    HiveSink,
  },
  setup() {
    const mapleConfig = reactive({
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
        pageConfig[config].push({ expand: true })
      }
    }

    const pluginNames = reactive({
      sources: ['jdbc', 'managed_jdbc', 'file'],
      transformations: ['sql'],
      sinks: ['hive', 'jdbc', 'managed_jdbc', 'file'],
    })

    const getTargetPlugins = (type: string): any[] => {
      switch (type) {
        case 'source':
          return mapleConfig.sources
        case 'transformation':
          return mapleConfig.transformations
        case 'sink':
          return mapleConfig.sinks
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
      let plugin = SampleData.PluginModels[type][name]()
      let plugins = getTargetPlugins(type)
      let pluginPageConfig = getTargetPluginPageConfig(type)
      if (index < 0 || index >= plugins.length) {
        plugins.push(plugin);
        pluginPageConfig.push({ expand: true });
      } else {
        plugins.splice(index, 0, plugin);
        pluginPageConfig.splice(index, 0, { expand: true });
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
          url: "/ftl/get-code",
          method: 'POST',
          data: toRaw(mapleConfig)
        }
        request(requestConfig).then(response => {
          code.value = response
        })
      } else {
        code.value = ''
      }
    }
    watch(codeView, () => getCode(codeView.value))
    return {
      variables,
      mapleConfig,
      code,
      codeView,
      getCode,
      pageConfig,
      pluginNames,
      addPlugin,
      delPlugin
    }
  }
})

</script>

<template>
  <a-breadcrumb separator="/">
    <a-breadcrumb-item>数据计算配置</a-breadcrumb-item>
  </a-breadcrumb>
  <a-row>
    <a-col :span="12" style="height: calc(100vh - 42px); padding: 5px; overflow: auto;">
      <a-typography-title :level="4">全局变量</a-typography-title>
      <input-string-map v-model:value="mapleConfig.variables" />

      <a-divider />

      <a-typography-title :level="4">输入</a-typography-title>
      <template v-for="(item, index) in mapleConfig.sources" :key="index">
        <add-plugin :types="pluginNames.sources" @add="(name: string) => addPlugin('source', name, index)" />
        <a-card :extra="item.name">
          <template #title>
            <plugin-operations v-model:value="pageConfig.sources[index]" :index="index"
              @delete="() => delPlugin('source', index)" />
            注册表名：{{ item.config.resultTable }}
          </template>
          <component :is="`${item.name.replace('_', '-')}-source`" v-model:value="item.config" :name="`source_${index}`"
            v-show="pageConfig.sources[index].expand" />
        </a-card>
      </template>
      <add-plugin :types="pluginNames.sources" @add="name => addPlugin('source', name)" />

      <a-divider />

      <a-typography-title :level="4">转换</a-typography-title>
      <template v-for="(item, index) in mapleConfig.transformations" :key="index">
        <add-plugin :types="pluginNames.transformations"
          @add="(name: string) => addPlugin('transformation', name, index)" />
        <a-card :extra="item.name">
          <template #title>
            <plugin-operations v-model:value="pageConfig.transformations[index]" :index="index"
              @delete="() => delPlugin('transformation', index)" />
            注册表名：{{ item.config.resultTable }}
          </template>
          <component :is="`${item.name.replace('_', '-')}-transformation`" v-model:value="item.config"
            :name="`transformation_${index}`" v-show="pageConfig.transformations[index].expand" />
        </a-card>
      </template>
      <add-plugin :types="pluginNames.transformations" @add="(name: string) => addPlugin('transformation', name)" />

      <a-divider />

      <a-typography-title :level="4">输出</a-typography-title>
      <template v-for="(item, index) in mapleConfig.sinks" :key="index">
        <add-plugin :types="pluginNames.sinks" @add="name => addPlugin('sink', name, index)" />
        <a-card :extra="item.name">
          <template #title>
            <plugin-operations v-model:value="pageConfig.sinks[index]" :index="index"
              @delete="() => delPlugin('sink', index)" />
            <template v-if="item.name === 'file'">
              写入路径: {{ item.config.path }}
            </template>
            <template v-else>
              输出表名: {{ item.config.targetDatabase }}.{{ item.config.targetTable }}
            </template>
          </template>
          <component :is="`${item.name.replace('_', '-')}-sink`" v-model:value="item.config" :name="`sink_${index}`"
            v-show="pageConfig.sinks[index].expand" />
        </a-card>
      </template>
      <add-plugin :types="pluginNames.sinks" @add="(name: string) => addPlugin('sink', name)" />
    </a-col>
    <a-col :span="12">
      <a-radio-group v-model:value="codeView" style="margin: 5px 0;">
        <a-radio-button :value="false">JSON</a-radio-button>
        <a-radio-button :value="true">Scala</a-radio-button>
      </a-radio-group>
      <a-button type="link" v-if="codeView" @click="() => getCode(true)">
        <template #icon><reload-outlined /></template>
      </a-button>
      <pre v-if="codeView" v-html="code" style="height: calc(100vh - 82px); width: 100%; overflow: auto" />
      <pre v-else v-html="JSON.stringify(mapleConfig, null, 4)"
        style="height: calc(100vh - 82px); width: 100%; overflow: auto" />
    </a-col>
  </a-row>
</template>

<style lang="less" scoped>
pre {
  font-size: 12px;
}

:deep(.ant-card-body) {
  padding: 0 8px !important;
}

:deep(.ant-card-body .ant-form) {
  margin-top: 14px;
}


textarea {
  font-family: "SFMono-Regular", Consolas, "Liberation Mono", Menlo, Courier,
    monospace;
  font-size: 12px;
}
</style>