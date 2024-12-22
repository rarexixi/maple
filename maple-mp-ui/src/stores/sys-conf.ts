import { defineStore } from "pinia"
import { getArrayConf } from "@/composables/requests"
import { computed } from "vue"

function getStore(storeId: string, configKey: string, valueField: string = "value", labelField: string = "label") {
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

export const useDatabaseTypesStore = getStore('database-types', 'database_types', 'value', 'name')

export const useClusterCategoriesStore = getStore('cluster-categories', 'cluster_categories')

export const useEngineCategoriesStore = getStore('engine-categories', 'engine_categories')

export const useJobTypesStore = getStore('job-types', 'job_run_types', 'typeCode', 'typeName')

export const useDatabaseTypesOfFlinkJdbcSupportedStore = getStore('db-types-of-flink-jdbc-supported', 'db_types_of_flink_jdbc_supported')

export const useDatabaseTypesOfSparkJdbcSupportedStore = getStore('db-types-of-spark-jdbc-supported', 'db_types_of_spark_jdbc_supported')

export const useSparkStorageLevelsStore = getStore('spark-storage-levels', 'spark_storage_levels')

export const useSparkFileSerializersStore = getStore('spark-file-serializers', 'spark_file_serializers')