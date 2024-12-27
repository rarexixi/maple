<script setup lang="ts">
import { ref } from "vue"

import type { ComputedColumn } from "@/composables/flink-jobs"

const computedColumns = defineModel<ComputedColumn[]>('computedColumns')

const customColumns = ref<boolean>(false)

const removeColumn = (index: number) => {
  if (index !== -1) {
    computedColumns.value?.splice(index, 1);
  }
}

const addColumn = () => {
  computedColumns.value?.push({
    name: '',
    expression: '',
    comment: ''
  })
}

const columnsTableProps = [
  { title: '', key: 'action' },
  { title: '字段名', dataIndex: 'name', key: 'name' },
  { title: '表达式', dataIndex: 'expression', key: 'dataType' },
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
      计算字段<span style="margin-inline-start: 2px;margin-inline-end: 8px;">:</span>
    </div>
    <div class="form-item-body" :style="{minWidth: customColumns ? '595px' : '235px', flex: 1}">
      <a-button type="default" @click="() => customColumns = !customColumns">
        {{ customColumns ? '隐藏配置' : '配置字段' }}
      </a-button>
      <a-button type="dashed" @click="addColumn" style="margin-left: 10px" v-show="customColumns">
        <PlusOutlined />
        添加字段
      </a-button>
      <div style="width: 100%; overflow-x: auto;" v-if="customColumns">
        <a-table :columns="columnsTableProps" :data-source="computedColumns" row-key="name"
                 :pagination="false" v-if="customColumns" class="table-form-items">
          <template #bodyCell="{ column, text, index, record }">
            <template v-if="column.key === 'action'">
              <a-button type="link" @click="() => removeColumn(index)" danger>
                <template #icon>
                  <MinusCircleOutlined />
                </template>
              </a-button>
            </template>
            <template v-if="'name' === column.dataIndex">
              <a-form-item :name="['computedColumns', index, 'name']" :label-col="subFormLabelCol"
                           :rules="[{ required: true, message: '请输入字段名', trigger: 'blur' }]">
                <a-input v-model:value="record.name" />
              </a-form-item>
            </template>
            <template v-if="'expression' === column.dataIndex">
              <a-form-item :name="['computedColumns', index, 'expression']" :label-col="subFormLabelCol"
                           :rules="[{ required: true, message: '请输入字段表达式', trigger: 'blur' }]">
                <a-input v-model:value="record.expression" />
              </a-form-item>
            </template>
            <template v-if="'comment' === column.dataIndex">
              <a-form-item :name="['physicalColumns', index, 'comment']" :label-col="subFormLabelCol">
                <a-input v-model:value="record.comment" />
              </a-form-item>
            </template>
          </template>
        </a-table>
      </div>
    </div>
  </a-flex>
  <a-flex-br v-if="customColumns" />
</template>

<style scoped>
.table-form-items {
  margin-bottom: 14px;
}

.table-form-items :deep(.ant-table-cell) {
  vertical-align: top !important;
}
</style>
