import type { ColumnProps } from "ant-design-vue/lib/table"
import { computed, ref, unref } from "vue"

type Key = ColumnProps['key']

export function getSelection() {
  const selectedRowKeys = ref<Key[]>([])
  const selectedRows = ref<any[]>([])
  const selected = computed(() => selectedRowKeys.value.length > 0)
  const onSelectChange = (changeableRowKeys: Key[], changeableRows: any[]) => {
    selectedRowKeys.value = changeableRowKeys
    selectedRows.value = changeableRows
  }

  const emptySelected = () => {
    selectedRowKeys.value = []
  }

  const rowSelection = computed(() => {
    return {
      selectedRowKeys: unref(selectedRowKeys),
      onChange: onSelectChange,
      hideDefaultSelections: true,
    }
  })

  return {
    rowSelection,
    selected,
    selectedRowKeys,
    selectedRows,
    emptySelected
  }
}
