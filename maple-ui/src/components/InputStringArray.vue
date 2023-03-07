<script lang="ts">
import {defineComponent, ref, toRefs, watch} from "vue"

export default defineComponent({
  props: {
    value: {type: Array, isRequired: true},
    separator: {type: String, isRequired: false, default: ','},
  },
  emits: ['update:value'],
  setup(props, {emit}) {
    const {value} = toRefs(props)
    const arr = ref((value.value || []).join(','))

    watch(value, () => arr.value = ((value.value || []).join(',')))

    const onValueBlur = (e: InputEvent) => {
      const newValue: string = (e.target as any).value || '';
      emit('update:value', newValue.split(props.separator).map(item => item.trim()))
    }
    return {
      arr,
      onValueBlur
    }
  },
})
</script>

<template>
  <a-textarea v-model:value="arr" @blur="onValueBlur" :auto-size="{ minRows: 2, maxRows: 20 }"/>
</template>