<script setup lang="ts">
import { onMounted, reactive, ref, watch } from "vue"

import type { PhysicalColumn } from "@/composables/flink-jobs"

const physicalColumns = defineModel<PhysicalColumn[]>('physicalColumns')
const { columns } = defineProps<{
  columns: PhysicalColumn[]
}>()

const customColumns = ref<boolean>(false)

const physicalColumnsState = reactive<{
  selectedRowKeys: string[];
}>({
  selectedRowKeys: [],
});

function onPhysicalColumnsSelectChange(changedRowKeys: string[], selectedRows: any[]) {
  physicalColumnsState.selectedRowKeys = changedRowKeys
  physicalColumns.value = selectedRows
}

const isInit = ref(false)

onMounted(() => {
  physicalColumnsState.selectedRowKeys = physicalColumns.value?.map((item: any) => item.name) || []
  isInit.value = true
})

watch(() => columns, (newColumns) => {
  if (isInit) {
    physicalColumnsState.selectedRowKeys = columns.map((item: any) => item.name)
    physicalColumns.value = columns
  }
})

const columnsTableProps = [
  { title: '字段名', dataIndex: 'name', key: 'name' },
  { title: '类型', dataIndex: 'dataType', key: 'dataType' },
  { title: '可空', dataIndex: 'nullable', key: 'nullable', width: 48 },
  { title: '注释', dataIndex: 'comment', key: 'comment' },
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
      物理字段<span style="margin-inline-start: 2px;margin-inline-end: 8px;">:</span>
    </div>
    <div class="form-item-body" :style="{minWidth: customColumns ? '595px' : '235px', flex: 1}">
      <a-button type="default" @click="() => customColumns = !customColumns">
        {{ customColumns ? '隐藏配置' : '配置字段' }}
      </a-button>
      <div style="width: 100%; overflow-x: auto;" v-if="customColumns">
        <a-table :columns="columnsTableProps" :data-source="columns" row-key="name"
                 :row-selection="{selectedRowKeys: physicalColumnsState.selectedRowKeys, onChange: onPhysicalColumnsSelectChange}"
                 :pagination="false" v-if="customColumns">
          <template #bodyCell="{ column, text, index, record }">
            <template v-if="'dataType' === column.dataIndex">
              <a-input v-model:value="record.dataType" />
            </template>
          </template>
        </a-table>
      </div>
    </div>
  </a-flex>
  <a-flex-br v-if="customColumns" />
</template>

