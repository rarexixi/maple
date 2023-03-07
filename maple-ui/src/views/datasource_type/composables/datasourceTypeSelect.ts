import type { ColumnProps } from "ant-design-vue/lib/table"
import type { UnwrapRef } from "vue"
import { computed, ref, unref } from "vue"

type Key = ColumnProps['key']
export function getSelection(dataList: UnwrapRef<any[]>) {

    const selectedRowKeys = ref<Key[]>([])
    const selectedRows = ref<any[]>([])
    const onSelectChange = (changeableRowKeys: Key[], changeableRows: any[]) => {
        selectedRowKeys.value = changeableRowKeys
        selectedRows.value = changeableRows
    }

    const emptySelected = () => {
        selectedRowKeys.value = []
        selectedRows.value = []
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
        selectedRowKeys,
        selectedRows,
        emptySelected
    }
}
