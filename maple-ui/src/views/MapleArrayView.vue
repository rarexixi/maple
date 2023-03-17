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
import AddPlugin from "@/components/AddTypedPlugin.vue"
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
      ...SampleData.SampleArrayConfig
    })

    const code = ref("")
    const codeView = ref(false)
    const pageConfig = reactive(Array<any>())

    for (let i = 0; i < mapleConfig.plugins.length; i++) {
      pageConfig.push({ expand: true })
    }

    const addPlugin = (type: string, name: string, index: number = -1) => {
      let plugin = SampleData.PluginModels[type][name]()
      let plugins = mapleConfig.plugins
      if (index < 0 || index >= plugins.length) {
        plugins.push(plugin);
        pageConfig.push({ expand: true });
      } else {
        plugins.splice(index, 0, plugin);
        pageConfig.splice(index, 0, { expand: true });
      }
    }

    const delPlugin = (index: number = -1) => {
      let plugins = mapleConfig.plugins
      if (index >= 0 && index < plugins.length) {
        plugins.splice(index, 1)
        pageConfig.splice(index, 1)
      }
    }

    const variables = reactive({})
    const getCode = (showCode: boolean) => {
      if (showCode) {
        const requestConfig = {
          url: "/ftl/get-array-code",
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
      addPlugin,
      delPlugin
    }
  }
})

</script>

<template>
  <a-row>
    <a-col :span="12" style="height: calc(100vh - 42px); padding: 5px; overflow: auto;">
      <a-typography-title :level="4">全局变量</a-typography-title>
      <input-string-map v-model:value="mapleConfig.variables" />
      <a-divider />
      <template v-for="(item, index) in mapleConfig.plugins" :key="index">
          <add-plugin @add="(type: string, name: string) => addPlugin(type, name, index)" />
          <a-card :extra="`${item.name}-${item.type}`">
            <template #title>
              <template v-if="item.type == 'sink'">
                <plugin-operations v-model:value="pageConfig[index]" :index="index"
                  @delete="() => delPlugin(index)" />
                <template v-if="item.name === 'file'">
                  写入路径: {{ item.config.path }}
                </template>
                <template v-else>
                  输出表名: {{ item.config.targetDatabase }}.{{ item.config.targetTable }}
                </template>
              </template>
              <template v-else>
                <plugin-operations v-model:value="pageConfig[index]" :index="index"
                  @delete="() => delPlugin(index)" />
                注册表名：{{ item.config.resultTable }}
              </template>
            </template>
            <component :is="`${item.name.replace('_', '-')}-${item.type}`" v-model:value="item.config" :name="`${item.type}_${index}`"
              v-show="pageConfig[index].expand" />
          </a-card>
        </template>
      <add-plugin @add="(type: string, name: string) => addPlugin(type, name)" />
    </a-col>
    <a-col :span="12">
      <a-radio-group v-model:value="codeView" style="margin: 5px 0;">
        <a-radio-button :value="false">JSON</a-radio-button>
        <a-radio-button :value="true">Scala</a-radio-button>
      </a-radio-group>
      <a-button type="link" v-if="codeView" @click="() => getCode(true)">
        <template #icon><reload-outlined /></template>
      </a-button>
      <pre v-if="codeView" v-html="code" style="height: calc(100vh - 88px); width: 100%; overflow: auto" />
      <pre v-else v-html="JSON.stringify(mapleConfig, null, 4)"
        style="height: calc(100vh - 88px); width: 100%; overflow: auto" />
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