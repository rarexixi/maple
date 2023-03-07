<script lang="ts">
import {defineComponent, reactive} from "vue"
import PriceInput from "@/components/price-input.vue"
import type { Currency } from "@/components/price-input.vue"
import InputStringArray from "@/components/InputStringArray.vue"
import InputStringMap from "@/components/InputStringMap.vue"

export default defineComponent({
  components: {InputStringMap, InputStringArray, PriceInput},
  setup() {
    const formState = reactive({
      price: {
        number: 0,
        currency: 'rmb' as Currency,
      },
      arr: ['hello', 'world'],
      map: {'a ':'b','c':'d'}
    })

    return {
      formState
    }
  }
})

</script>

<template>
  {{formState}}
  <a-row :gutter="[16,16]">
    <a-col :span="24">
      <price-input v-model:value="formState.price"/>
    </a-col>
    <a-col :span="24">
      <input-string-array v-model:value="formState.arr"/>
    </a-col>
    <a-col :span="24">
      <input-string-map v-model:value="formState.map"/>
    </a-col>
    <a-col :xs="2" :sm="4" :md="6" :lg="8" :xl="10">Col</a-col>
    <a-col :xs="20" :sm="16" :md="12" :lg="8" :xl="4">Col</a-col>
    <a-col :xs="2" :sm="4" :md="6" :lg="8" :xl="10">Col</a-col>
  </a-row>
</template>

<style scoped>

</style>