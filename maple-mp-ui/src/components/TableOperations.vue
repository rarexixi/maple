<script setup lang="ts">
const {
  disabled = 0,
  canEnable = true,
  canDisable = true,
  canEdit = true,
  canCopy = true,
  canDel = true,
} = defineProps<{
  disabled?: number
  canEnable?: boolean
  canDisable?: boolean
  canEdit?: boolean
  canCopy?: boolean
  canDel?: boolean
}>()

const emit = defineEmits<{
  (e: 'enable'): void
  (e: 'disable'): void
  (e: 'edit'): void
  (e: 'copy'): void
  (e: 'del'): void
}>()

</script>

<template>
  <div class="table-operations">
    <slot name="before" />
    <a-popconfirm v-if="(disabled === 1 && canEnable) || (disabled !== 1 && canDisable)"
                  :title="`确定${disabled === 1 ? '启用' : '禁用'}吗？`"
                  @confirm="() => disabled === 1 ? emit('enable') : emit('disable')">
      <a :class="disabled ? 'enable' : 'disable'">
        <check-outlined />
        <stop-outlined />
      </a>
    </a-popconfirm>
    <a v-if="canEdit" @click="() => emit('edit')" class="text-primary">
      <edit-outlined />
    </a>
    <a v-if="canCopy" @click="() => emit('copy')" class="text-primary">
      <copy-outlined />
    </a>
    <a-popconfirm v-if="canDel" title="确定删除吗？" @confirm="() => emit('del')">
      <a class="text-danger">
        <delete-outlined />
      </a>
    </a-popconfirm>
    <slot name="after" />
  </div>
</template>

<style scoped>

</style>