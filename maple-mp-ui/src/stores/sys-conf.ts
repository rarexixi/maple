import { defineStore } from "pinia"
import { getArrayConf, getConf } from "@/composables/requests"
import { computed } from "vue"

function getArrayStore(storeId: string, configKey: string, valueField: string = "value", labelField: string = "label") {
  return defineStore(storeId, () => {
    const conf = getArrayConf(configKey, valueField, labelField)
    const confArray = computed(() => conf.confArray)
    const confMap = computed(() => conf.confMap)
    const confOptions = computed(() => conf.confOptions)
    const confOptionMap = computed(() => conf.confOptionMap)
    const confInitialized = computed(() => conf.confInitialized)
    return { confArray, confMap, confOptions, confOptionMap, confInitialized }
  })
}

function getStore(storeId: string, configKey: string) {
  return defineStore(storeId, () => {
    let confResult = getConf(configKey);
    const conf = computed(() => confResult.conf)
    const confInitialized = computed(() => confResult.confInitialized)
    return { conf, confInitialized }
  })
}

export const useDatabaseTypesStore = getArrayStore('database_types', 'database_types', 'value', 'name')

export const useClusterCategoriesStore = getArrayStore('cluster_categories', 'cluster_categories')

export const useEngineCategoriesStore = getArrayStore('engine_categories', 'engine_categories')

export const useJobTypesStore = getArrayStore('job_run_types', 'job_run_types', 'typeCode', 'typeName')

export const useDatabaseTypesOfFlinkJdbcSupportedStore = getArrayStore('db_types_of_flink_jdbc_supported', 'db_types_of_flink_jdbc_supported')

export const useDatabaseTypesOfSparkJdbcSupportedStore = getArrayStore('db_types_of_spark_jdbc_supported', 'db_types_of_spark_jdbc_supported')

export const useSparkStorageLevelsStore = getArrayStore('spark_storage_levels', 'spark_storage_levels')

export const useSparkFileSerializersStore = getArrayStore('spark_file_serializers', 'spark_file_serializers')

export const useFlinkConnectorAvailableMetadataStore = getStore('flink_connector_available_metadata', 'flink_connector_available_metadata')