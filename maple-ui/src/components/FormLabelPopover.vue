<script lang="ts">
import { defineComponent } from "vue";

export default defineComponent({
  props: {
    label: {
      type: String,
      isRequired: true
    },
    tips: {
      type: String,
      isRequired: true
    },
  }
})
</script>

<template>
  <span>{{ label }}</span>
  <a-popover placement="right">
    <template #content>
      <p>{{ tips }}</p>
    </template>
    <info-circle-outlined />
  </a-popover>
</template>