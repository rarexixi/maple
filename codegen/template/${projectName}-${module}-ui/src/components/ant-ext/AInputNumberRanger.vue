<script setup lang="ts">
const min = defineModel<number>('min')
const max = defineModel<number>('max')
const {placeholders = ['最小值', '最大值'], split = '~'} = defineProps<{
  placeholders?: string[] | undefined,
  split?: string | undefined
}>()
</script>

<template>
  <a-input-group compact class="input-group">
    <a-input
        v-model:value.number="min"
        class="input-left"
        :placeholder="placeholders[0]"
    />
    <a-input
        :value="split"
        class="input-split"
        disabled
    />
    <a-input
        v-model:value.number="max"
        class="input-right"
        style=""
        :placeholder="placeholders[1]"
    />
  </a-input-group>
</template>

<style scoped>
.input-group .input-left {
  width: 100px;
  text-align: center;
  border-start-start-radius: 4px;
  border-end-start-radius: 4px;
}

.input-group .input-split {
  width: 1.5rem;
  text-align: center;
  background-color: #ffffff;
  border-left: 0;
  pointer-events: none;
}

.input-group .input-right {
  border-left-width: 0;
  width: 100px;
  text-align: center;
  border-inline-end-width: 1px;
  border-start-end-radius: 4px;
  border-end-end-radius: 4px;
}

.input-group .input-right:hover,
.input-group .input-right:focus {
  border-left-width: 1px;
}
</style>