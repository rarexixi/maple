<script setup lang="ts">
import { ref } from "vue"

import ParamsMap from "@/components/ParamsMap.vue"
import common from "@/composables/common"

const value = defineModel<any>("value")
const { name } = defineProps<{
  name: string | number | (string | number)[]
}>()

const showOptions = ref<boolean>(false)

const labelCols = common.Layout.labelCols

</script>

<template>
  <a-form-item :name="name" label="参数" :label-col="labelCols.l125"
               :class="showOptions ? 'form-item-360' : 'form-item-720'">
    <params-map v-model:value="value.options" v-model:show-options="showOptions" />
  </a-form-item>
</template>

<style scoped>
</style>