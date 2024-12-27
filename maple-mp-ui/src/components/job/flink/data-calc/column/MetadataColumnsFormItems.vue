<script setup lang="ts">
import { onMounted, reactive, ref } from "vue"

import type { MetadataColumn } from "@/composables/flink-jobs"
import { useFlinkConnectorAvailableMetadataStore } from "@/stores/sys-conf";

const metadataColumns = defineModel<MetadataColumn[]>('metadataColumns')

const { metadataType, datasourceTypes = [] } = defineProps<{
  metadataType?: string,
  datasourceTypes: string[],
}>()

const customColumns = ref<boolean>(false)

const metadataColumnsState = reactive<{
  selectedRowKeys: string[];
}>({
  selectedRowKeys: [],
});

const metadataColumnsData = ref<any[]>([])

const { conf: metadataColumnsConf } = useFlinkConnectorAvailableMetadataStore()

onMounted(() => {
  if (metadataType && metadataColumnsConf.value[metadataType]) {
    metadataColumnsData.value = metadataColumnsConf.value[metadataType].map((c: any) => JSON.parse(JSON.stringify(c)))
    metadataColumnsState.selectedRowKeys = metadataColumns.value?.map((item: any) => item.name) || []
  }
})

function onMetadataColumnsSelectChange(changedRowKeys: string[], selectedRows: any[]) {
  metadataColumnsState.selectedRowKeys = changedRowKeys
  metadataColumns.value = selectedRows
}

const columnsTableProps = [
  { title: '字段名', dataIndex: 'name', key: 'name', },
  { title: '类型', dataIndex: 'dataType', key: 'dataType' },
  { title: '元数据KEY', dataIndex: 'metadataKey', key: 'metadataKey' },
  { title: '只读', dataIndex: 'virtual', key: 'virtual', width: 48 },
  { title: '说明', dataIndex: 'comment', key: 'comment' },
]

const subFormLabelCol = {
  style: {
    width: 'auto'
  }
}
</script>

<template>
  <a-flex-br v-if="customColumns" />
  <a-flex>
    <div style="width: 125px; margin-bottom: 14px; text-align: right">
      元数据字段<span style="margin-inline-start: 2px;margin-inline-end: 8px;">:</span>
    </div>
    <div class="form-item-body" :style="{minWidth: customColumns ? '595px' : '235px', flex: 1}">
      <a-button type="default" @click="() => customColumns = !customColumns">
        {{ customColumns ? '隐藏配置' : '配置字段' }}
      </a-button>
      <div style="width: 100%; overflow-x: auto;" v-if="customColumns">
        <a-table :columns="columnsTableProps" :data-source="metadataColumnsData" row-key="name"
                 :row-selection="{selectedRowKeys: metadataColumnsState.selectedRowKeys, onChange: onMetadataColumnsSelectChange}"
                 :pagination="false" v-if="customColumns">
          <template #bodyCell="{ column, text, record }">
            <template v-if="'name' === column.dataIndex">
              <a-input v-model:value="record.name" />
            </template>
            <template v-if="'virtual' === column.dataIndex">
              <a-checkbox v-model:checked="record.virtual" />
            </template>
          </template>
        </a-table>
      </div>
    </div>
  </a-flex>
  <a-flex-br v-if="customColumns" />
</template>

