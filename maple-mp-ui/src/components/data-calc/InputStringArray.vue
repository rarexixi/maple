<script setup lang="ts">
import { ref, watch } from "vue"

const {value, separator} = defineProps<{
  value: string[],
  separator: string
}>()

const arr = ref((value.value || []).join(','))
watch(() => value, () => arr.value = ((value.value || []).join(',')))

const emit = defineEmits<{
  (e: 'update:value', arr: string[]): void
}>()

const onValueBlur = (e: InputEvent) => {
  const newValue: string = (e.target as any).value || '';
  emit('update:value', newValue.split(separator).map(item => item.trim()))
}
</script>

<template>
  <a-textarea v-model:value="arr" placeholder="value1,value2,value3,..." @blur="onValueBlur"
    :auto-size="{ minRows: 1, maxRows: 20 }" />
</template>