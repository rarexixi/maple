<script lang="ts">
import {defineComponent, reactive, ref, toRaw, watch} from "vue"
import JdbcSource from "@/components/source/JdbcSource.vue"
import ManagedJdbcSource from "@/components/source/ManagedJdbcSource.vue"
import FileSource from "@/components/source/FileSource.vue"
import SqlTransformation from "@/components/transformation/SqlTransformation.vue"
import HiveSink from "@/components/sink/HiveSink.vue"
import JdbcSink from "@/components/sink/JdbcSink.vue"
import ManagedJdbcSink from "@/components/sink/ManagedJdbcSink.vue"
import FileSink from "@/components/sink/FileSink.vue"
import AddPlugin from "@/components/AddPlugin.vue"
import InputStringMap from "@/components/InputStringMap.vue"
import SampleData from "@/assets/sample-data"
import {MinusOutlined} from "@ant-design/icons-vue";
import {request} from "@/utils/request-utils";


export default defineComponent({
  components: {
    InputStringMap,
    AddPlugin,
    JdbcSource,
    ManagedJdbcSource,
    FileSource,
    SqlTransformation,
    FileSink,
    JdbcSink,
    ManagedJdbcSink,
    HiveSink,
    MinusOutlined
  },
  setup() {
    const mapleConfig = reactive({
      ...SampleData.SampleConfig
    })

    console.log(SampleData.SampleConfig)

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
        pluginPageConfig.push({expand: true});
      } else {
        plugins.splice(index, 0, plugin);
        pluginPageConfig.splice(index, 0, {expand: true});
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
    watch(codeView, () => {
      if (codeView.value) {
        const requestConfig = {
          url: "/ftl/get-code",
          method: 'POST',
          data: toRaw(mapleConfig)
        }
        request(requestConfig).then(response => {
          code.value = response
        })
      }
    })
    return {
      variables,
      mapleConfig,
      code,
      codeView,
      pageConfig,
      pluginNames,
      addPlugin,
      delPlugin
    }
  }
})

</script>

<template>
  <div style="width: 100%; overflow: hidden;">
    <a-row :gutter="[16,16]">
      <a-col :span="12" style="height: calc(100vh - 80px); overflow: auto">
        <a-typography-title :level="3">输入</a-typography-title>
        <template v-for="(item, index) in mapleConfig.sources">
          <a-card>
            <template #title>
              {{index + 1}}
              <add-plugin :types="pluginNames.sources" btn-text="插入" @add="name => addPlugin('source', name, index)"/>
              <a-tooltip title="删除">
                <a-button @click="delPlugin('source', index)" type="link" danger>
                  <template #icon>
                    <minus-outlined/>
                  </template>
                </a-button>
              </a-tooltip>
              <template v-if="item.name === 'jdbc'">
                jdbc ｜ 注册表名：{{ item.config.resultTable }}
              </template>
              <template v-if="item.name === 'managed_jdbc'">
                managed_jdbc ｜ 注册表名：{{ item.config.resultTable }}
              </template>
              <template v-else-if="item.name === 'file'">
                file ｜ 注册表名：{{ item.config.path }}
              </template>
            </template>
            <template #extra>
              <a-switch v-model:checked="pageConfig.sources[index].expand" checked-children="展开" un-checked-children="隐藏"/>
            </template>
            <template v-if="item.name === 'jdbc'">
              <jdbc-source v-model:value="item.config" v-show="pageConfig.sources[index].expand"/>
            </template>
            <template v-if="item.name === 'managed_jdbc'">
              <managed-jdbc-source v-model:value="item.config" v-show="pageConfig.sources[index].expand"/>
            </template>
            <template v-else-if="item.name === 'file'">
              <file-source v-model:value="item.config" v-show="pageConfig.sources[index].expand"/>
            </template>
          </a-card>
        </template>
        <add-plugin :types="pluginNames.sources" @add="name => addPlugin('source', name)"/>

        <a-divider/>

        <a-typography-title :level="3">转换</a-typography-title>
        <template v-for="(item, index) in mapleConfig.transformations">
          <a-card>
            <template #title>
              {{index + 1}}
              <add-plugin :types="pluginNames.transformations" btn-text="插入" @add="name => addPlugin('transformation', name, index)"/>
              <a-tooltip title="删除">
                <a-button @click="delPlugin('transformation', index)" type="link" danger>
                  <template #icon>
                    <minus-outlined/>
                  </template>
                </a-button>
              </a-tooltip>
              <template v-if="item.name === 'sql'">
                sql ｜ 注册表名：{{ item.config.targetDatabase }}.{{ item.config.targetTable }}
              </template>
            </template>
            <template #extra>
              <a-switch v-model:checked="pageConfig.transformations[index].expand" checked-children="展开" un-checked-children="隐藏"/>
            </template>
            <template v-if="item.name === 'sql'">
              <sql-transformation v-model:value="item.config" v-show="pageConfig.transformations[index].expand"/>
            </template>
          </a-card>
        </template>
        <add-plugin :types="pluginNames.transformations" @add="name => addPlugin('transformation', name)"/>

        <a-divider/>

        <a-typography-title :level="3">输出</a-typography-title>
        <template v-for="(item, index) in mapleConfig.sinks">
          <a-card>
            <template #title>
              {{index + 1}}
              <add-plugin :types="pluginNames.sinks" btn-text="插入" @add="name => addPlugin('sink', name, index)"/>
              <a-tooltip title="删除">
                <a-button @click="delPlugin('sink', index)" type="link" danger>
                  <template #icon>
                    <minus-outlined/>
                  </template>
                </a-button>
              </a-tooltip>
              <template v-if="item.name === 'hive'">
                hive ｜ 输出表名: {{ item.config.targetDatabase }}.{{ item.config.targetTable }}
              </template>
              <template v-else-if="item.name === 'jdbc'">
                jdbc ｜ 输出表名: {{ item.config.targetDatabase }}.{{ item.config.targetTable }}
              </template>
              <template v-else-if="item.name === 'managed_jdbc'">
                managed_jdbc ｜ 输出表名: {{ item.config.targetDatabase }}.{{ item.config.targetTable }}
              </template>
              <template v-else-if="item.name === 'file'">
                file ｜ 写入路径: {{ item.config.path }}
              </template>
            </template>
            <template #extra>
              <a-switch v-model:checked="pageConfig.sinks[index].expand" checked-children="展开" un-checked-children="隐藏"/>
            </template>
            <template v-if="item.name === 'hive'">
              <hive-sink v-model:value="item.config" v-show="pageConfig.sinks[index].expand"/>
            </template>
            <template v-else-if="item.name === 'jdbc'">
              <jdbc-sink v-model:value="item.config" v-show="pageConfig.sinks[index].expand"/>
            </template>
            <template v-else-if="item.name === 'managed_jdbc'">
              <managed-jdbc-sink v-model:value="item.config" v-show="pageConfig.sinks[index].expand"/>
            </template>
            <template v-else-if="item.name === 'file'">
              <file-sink v-model:value="item.config" v-show="pageConfig.sinks[index].expand"/>
            </template>
          </a-card>
        </template>
        <add-plugin :types="pluginNames.sinks" @add="name => addPlugin('sink', name)"/>
      </a-col>
      <a-col :span="12">
        <a-typography-title :level="3">
          生成对象
          <a-switch v-model:checked="codeView" checked-children="代码" un-checked-children="JSON"/>
        </a-typography-title>
        <textarea v-if="codeView" v-html="code" style="background-color: #ffffff; padding: 10px; height: calc(100vh - 120px); width: 100%; overflow: auto"/>
        <textarea v-else v-html="JSON.stringify(mapleConfig, null, 4)" style="background-color: #ffffff; padding: 10px; height: calc(100vh - 120px); width: 100%; overflow: auto"/>
      </a-col>
    </a-row>
  </div>
</template>

<style lang="less" scoped>

pre {
  font-size: 12px;
}

:deep(.ant-card-body) {
  padding: 14px 8px 0 !important;
}

.ant-card {
  margin-bottom: 10px;
}

textarea {
  font-family: 'SFMono-Regular', Consolas, 'Liberation Mono', Menlo, Courier, monospace;
  font-size: 12px;
}
</style>