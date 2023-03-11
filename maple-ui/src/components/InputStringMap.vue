<script lang="ts">
import { defineComponent, ref, toRefs, watch } from "vue"

export default defineComponent({
  props: {
    value: { type: Object, isRequired: true },
    separator: { type: String, isRequired: false, default: '\n' },
  },
  emits: ['update:value'],
  setup(props, { emit }) {
    const { value } = toRefs(props)

    const getMapValue = (): string => {
      const arr: string[] = [] as string[]
      let val = value.value || {};
      for (let key of Object.keys(val)) {
        arr.push(key + "=" + val[key])
      }
      return arr.join(props.separator)
    }

    const map = ref(getMapValue())
    watch(value, () => map.value = getMapValue())

    const toJson = (s: string) => {
      let json: any = {}
      s.split(props.separator).forEach(line => {
        let option = line.split('=').map(item => item.trim())
        if (option[0] !== '') json[option[0]] = option[1] || ''
      })
      return json
    }

    const onValueBlur = (e: InputEvent) => {
      const newValue = (e.target as any).value || ''
      emit('update:value', toJson(newValue));
    }
    return {
      map,
      onValueBlur
    }
  },
})
</script>

<template>
  <a-textarea v-model:value="map" :placeholder="'key1=value1\nkey2=value2\n...'" @blur="onValueBlur" :auto-size="{ minRows: 2, maxRows: 20 }" />
</template>