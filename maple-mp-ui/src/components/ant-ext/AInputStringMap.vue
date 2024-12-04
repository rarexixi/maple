<script setup lang="ts">
import { computed, ref, watch } from "vue"

const {value, separator = '\n'} = defineProps<{
  value: string[],
  separator?: string
}>()

const emit = defineEmits<{
  (e: 'update:value', data: any): void
}>()

const getMapValue = (): string => {
  const arr: string[] = [] as string[]
  let val = value || {};
  for (let key of Object.keys(val)) {
    arr.push(key + "=" + val[key])
  }
  return arr.join(separator)
}

const map = ref(getMapValue())
watch(() => value, () => map.value = getMapValue())

const toJson = (s: string) => {
  let json: any = {}
  s.split(separator).forEach(line => {
    let option = line.split('=').map(item => item.trim())
    if (option[0] !== '') json[option[0]] = option[1] || ''
  })
  return json
}

const onValueBlur = (e: InputEvent) => {
  const newValue = (e.target as any).value || ''
  emit('update:value', toJson(newValue));
}

</script>

<template>
  <a-textarea v-model:value="map" @blur="onValueBlur"
              :placeholder="`key1=value1${separator}key2=value2${separator}...`"
              :auto-size="{ minRows: 3, maxRows: 20 }" />
</template>