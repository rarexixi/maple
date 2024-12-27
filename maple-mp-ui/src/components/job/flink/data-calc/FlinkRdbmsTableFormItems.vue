<script setup lang="ts">
import { computed, reactive } from "vue"

import common from "@/composables/common"
import { getDatasourceOptions } from "@/composables/datasources"
import type { ComputedColumn, MetadataColumn, PhysicalColumn } from "@/composables/flink-jobs"
import type { RdbmsTable } from "@/composables/models"

import { useDatasourceStore } from "@/stores/sys-data"

import ConfOptionsForm from "@/components/job/ConfOptionsForm.vue"
import TableSelectFormItems from "@/components/datasource/TableSelectFormItems.vue"
import PhysicalColumnsFormItems from "@/components/job/flink/data-calc/column/SelectablePhysicalColumnsFormItems.vue";
import MetadataColumnsFormItems from "@/components/job/flink/data-calc/column/MetadataColumnsFormItems.vue";
import ComputedColumnsFormItems from "@/components/job/flink/data-calc/column/ComputedColumnsFormItems.vue"

const resultTable = defineModel<string>('resultTable')
const comment = defineModel<string>('comment')
const datasourceId = defineModel<number>('datasourceId')
const rdbmsTable = defineModel<RdbmsTable>('rdbmsTable', {
  required: true,
  default: () => ({ databaseName: '', schemaName: '', tableName: '' }),
})
const physicalColumns = defineModel<PhysicalColumn[]>('physicalColumns')
const metadataColumns = defineModel<MetadataColumn[]>('metadataColumns')
const computedColumns = defineModel<ComputedColumn[]>('computedColumns')
const pkColumns = defineModel<string[]>('pkColumns')
const wmColumn = defineModel<string>('wmColumn')
const wmDelaySeconds = defineModel<number>('wmDelaySeconds')
const partitionColumns = defineModel<string[]>('partitionColumns', {
  required: true,
  default: () => [],
})
const options = defineModel<any>('options', {
  required: true,
  default: () => ({}),
})

const { metadataType, datasourceTypes = [] } = defineProps<{
  metadataType?: string,
  datasourceTypes: string[],
}>()

const { dataList: datasourceList } = useDatasourceStore()
const datasourceOptions = computed(() => getDatasourceOptions(datasourceList.value, ...datasourceTypes))

const tableDetail = reactive<any>({
  columns: [],
  pkColumns: [],
})

const watermarkOptions = computed(() => {
  let allColumns = [...physicalColumns.value || [], ...metadataColumns.value || []]
  return allColumns.filter((column: any) => column.dataType?.startsWith('TIMESTAMP(3)'))
      .map((column: any) => {
        let label = column.comment ? `${column.name} (${column.comment})` : column.name
        return { label, value: column.name, }
      }) || []
})

const columnOptions = computed(() => physicalColumns.value?.map((column: any) => {
  return { label: column.name, value: column.name, }
}) || [])


const labelCols = common.Layout.labelCols

function getTable(table: any, isInit: boolean) {
  tableDetail.columns = (table.columns || []).map((row: any): PhysicalColumn => {
    return { name: row.columnName, dataType: row.flinkDataType, comment: row.columnComment, nullable: false }
  })
  tableDetail.pkColumns = table.pkColumns || []
  pkColumns.value = table.pkColumns.map((column: any) => column.columnName)

  if (!isInit) {
    physicalColumns.value = tableDetail.columns
  }
}

</script>

<template>
  <a-form-item name="resultTable" label="注册表名" class="form-item-360">
    <a-input v-model:value="resultTable" />
  </a-form-item>
  <a-form-item name="comment" label="说明" class="form-item-360">
    <a-input v-model:value="comment" />
  </a-form-item>
  <a-flex-br />
  <a-form-item name="datasourceId" label="数据源" class="form-item-360">
    <a-select v-model:value="datasourceId" :options="datasourceOptions" placeholder="请选择" />
  </a-form-item>
  <TableSelectFormItems v-model:database-name="rdbmsTable.databaseName"
                        v-model:schema-name="rdbmsTable.schemaName"
                        v-model:table-name="rdbmsTable.tableName"
                        :datasource-id="datasourceId" :validated-name-prefix="['rdbmsTable']"
                        :require-table="true" @change-table="getTable" />
  <a-flex-br />
  <PhysicalColumnsFormItems v-model:physical-columns="physicalColumns" :columns="tableDetail.columns"
                            v-if="tableDetail.columns.length > 0" />
  <MetadataColumnsFormItems v-model:metadata-columns="metadataColumns" :metadata-type="metadataType"
                            :datasource-types="datasourceTypes" />
  <ComputedColumnsFormItems v-model:computed-columns="computedColumns" />
  <a-form-item name="pkColumns" label="主键" class="form-item-360">
    <a-select v-model:value="pkColumns" mode="multiple" style="width: 100%" :options="columnOptions" />
  </a-form-item>
  <a-form-item name="wmDelaySeconds" label="watermark" :label-col="labelCols.l125"
               :rules="[{required: !!wmColumn}]" class="form-item-720" v-if="watermarkOptions.length > 0">
    <a-form-item-rest>
      <a-select v-model:value="wmColumn" :options="watermarkOptions" style="width: 40%" allow-clear />
    </a-form-item-rest>
    <a-input-number v-model:value="wmDelaySeconds" style="width: 30%" addon-before="延迟" addon-after="秒" />
  </a-form-item>
  <a-form-item name="partitionColumns" label="分区字段" class="form-item-360">
    <a-select v-model:value="partitionColumns" mode="multiple" :options="columnOptions" />
  </a-form-item>
  <slot name="definedOptions"></slot>
  <a-flex-br />
  <ConfOptionsForm name="options" v-model:value="options" />
</template>