<script setup lang="ts">
import common from "@/composables/common"

const preQueries = defineModel<string[]>("preQuires")

const labelCols = common.Layout.labelCols

</script>

<template>
  <a-form-item :label-col="labelCols.l125" class="form-item-360">
    <template #label>
      <a-tooltip title="预先要执行的SQL语句，一般为delete或者truncate语句" placement="topLeft">
        <QuestionCircleOutlined />
        <span>PreSQL</span>
      </a-tooltip>
    </template>
    <a-button type="dashed" @click="() => preQueries?.push('')">
      <PlusOutlined />
      添加预执行SQL
    </a-button>
  </a-form-item>
  <template v-for="(_, index) in preQueries" :key="index">
    <a-flex-br />
    <a-form-item :name="['preQueries', index]" :label-col="labelCols.l125" :rules="[{required: true, message: '请输入SQL语句'}]"
                 class="form-item-720">
      <template #label>
        <MinusCircleOutlined @click="() => preQueries?.splice(index, 1)" />
      </template>
      <a-textarea v-model:value="preQueries[index]" placeholder="SQL语句" />
    </a-form-item>
  </template>
</template>