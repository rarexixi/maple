<script setup lang="ts">
import { computed, onMounted, ref, watch } from "vue"

import { JdbcApis } from "@/composables/service-apis"

import { request } from "@/utils/request-utils"

import { useDatabaseTypesStore } from "@/stores/sys-conf"
import { useDatasourceStore } from "@/stores/sys-data"

const { confMap } = useDatabaseTypesStore()

const databaseName = defineModel<string>('databaseName')
const schemaName = defineModel<string>('schemaName')
const tableName = defineModel<string>('tableName')

interface PropModel {
  datasourceId?: number,
  requireTable?: boolean
  validatedNamePrefix: (string | number)[],
}

const { datasourceId, requireTable = false } = defineProps<PropModel>()

const { dataMap: datasourceMap } = useDatasourceStore()

const datasourceType = ref('')
const databaseOptions = ref([])
const schemaOptions = ref([])
const tableOptions = ref([])
const dataInitialized = ref(false)
const databaseConf = ref(null as any)

const databaseSelectable = computed(() => databaseConf.value?.selectCrossDatabase)
const schemaSelectable = computed(() => databaseConf.value?.hasSchema)

function getHeaders() {
  return {
    "maple-tag": `maple-jdbc-service:${datasourceType.value}`,
  }
}

function resetTable() {
  tableName.value = undefined
  tableOptions.value = []
}

function resetSchema() {
  schemaName.value = undefined
  schemaOptions.value = []
  resetTable()
}

function resetDatabase() {
  databaseName.value = undefined
  databaseOptions.value = []
  resetSchema()
}

async function getDatabases(_datasourceId?: number) {
  if (!_datasourceId || !databaseConf.value) return
  await request({ ...JdbcApis.dbs(_datasourceId), headers: getHeaders() }).then(response => {
    databaseOptions.value = response.map((db: any) => ({ value: db.databaseName, label: db.databaseName }))
  })
}

async function getSchemas(_datasourceId?: number, _databaseName?: string) {
  if (!datasourceId || !databaseConf.value) return
  let params = {
    databaseName: _databaseName
  }
  await request({ ...JdbcApis.schemas(_datasourceId), params, headers: getHeaders() }).then(response => {
    schemaOptions.value = response.map((db: any) => ({ value: db.schemaName, label: db.schemaName }))
  })
}

async function getTables(_databaseName?: string, _schemaName?: string) {
  if (!datasourceId || !databaseConf.value) return
  let params = {
    databaseName: _databaseName,
    schemaName: _schemaName
  }
  await request({ ...JdbcApis.tables(datasourceId), params, headers: getHeaders() }).then(response => {
    tableOptions.value = response.map((db: any) => {
      let tableComment = db.tableComment ? ` (${db.tableComment})` : ''
      return {
        value: db.tableName,
        label: `${db.tableName}${tableComment}`
      }
    })
  })
}

const emit = defineEmits<{
  (e: 'change-table', tableDetail: any): void
}>()

function getTable(_tableName: string) {
  if (!requireTable || !datasourceId || !databaseConf.value) return
  let params = {
    databaseName: databaseName.value,
    schemaName: schemaName.value,
    tableName: _tableName
  }
  request({ ...JdbcApis.table(datasourceId), params, headers: getHeaders() }).then(response => {
    emit('change-table', {
      datasource: datasourceId,
      database: databaseName.value,
      schema: schemaName.value,
      table: _tableName,
      ...response
    })
  })
}

function changeDatasource(val?: number) {
  resetDatabase()
  if (!val) return
  let datasourceDetail = datasourceMap[val]
  if (!datasourceDetail) return
  datasourceType.value = datasourceDetail.datasourceType
  databaseConf.value = confMap[datasourceDetail.datasourceType]
  if (databaseConf.value) {
    if (databaseConf.value.selectCrossDatabase) {
      getDatabases(val)
    } else {
      getSchemas(val, '')
    }
  }
}

function changeDatabase(val?: string) {
  resetSchema()
  if (!val) return
  if (schemaSelectable.value) {
    getSchemas(datasourceId, val)
  } else {
    getTables(val)
  }
}

function changeSchema(val?: string) {
  resetTable()
  if (!val) return
  getTables(databaseName.value, val)
}

function changeTable(val?: string) {
  if (!val) return
  getTable(val)
}

watch(() => datasourceId, changeDatasource)

onMounted(async () => {
  if (dataInitialized.value) return
  let datasourceDetail = datasourceMap[datasourceId]
  if (!datasourceDetail) return
  datasourceType.value = datasourceDetail.datasourceType
  databaseConf.value = confMap[datasourceDetail.datasourceType]
  if (databaseConf.value) {
    await getDatabases(datasourceId)
    await getSchemas(datasourceId, databaseName.value)
    await getTables(databaseName.value, schemaName.value)
    dataInitialized.value = true
  }
})

</script>

<template>
  <template v-if="databaseSelectable">
    <a-form-item :name="[...validatedNamePrefix, 'databaseName']" label="库" v-if="!!datasourceId"
                 :rules="[{ required: true }]" class="form-item-320">
      <a-select v-model:value="databaseName" :options="databaseOptions" @change="changeDatabase"
                placeholder="请选择 database" allow-clear />
    </a-form-item>
    <template v-if="schemaSelectable">
      <a-form-item :name="[...validatedNamePrefix, 'schemaName']" label="Schema" v-if="!!databaseName"
                   :rules="[{ required: true }]" class="form-item-320">
        <a-select v-model:value="schemaName" :options="schemaOptions" @change="changeSchema"
                  placeholder="请选择 schema" allow-clear />
      </a-form-item>
      <a-form-item :name="[...validatedNamePrefix, 'tableName']" label="表" v-if="!!schemaName"
                   :rules="[{ required: true }]" class="form-item-320">
        <a-select v-model:value="tableName" :options="tableOptions" @change="changeTable"
                  placeholder="请选择 table" allow-clear />
      </a-form-item>
    </template>
    <template v-else>
      <a-form-item :name="[...validatedNamePrefix, 'tableName']" label="表" v-if="!!databaseName"
                   :rules="[{ required: true }]" class="form-item-320">
        <a-select v-model:value="tableName" :options="tableOptions" @change="changeTable"
                  placeholder="请选择 table" allow-clear />
      </a-form-item>
    </template>
  </template>
  <template v-else>
    <a-form-item :name="[...validatedNamePrefix, 'schemaName']" label="Schema" v-if="!!datasourceId"
                 :rules="[{ required: true }]" class="form-item-320">
      <a-select v-model:value="schemaName" :options="schemaOptions" @change="changeSchema"
                placeholder="请选择 schema" allow-clear />
    </a-form-item>
    <a-form-item :name="[...validatedNamePrefix, 'tableName']" label="表" v-if="!!schemaName"
                 :rules="[{ required: true }]" class="form-item-320">
      <a-select v-model:value="tableName" :options="tableOptions" @change="changeTable"
                placeholder="请选择 table" allow-clear />
    </a-form-item>
  </template>
</template>

<style scoped>

</style>