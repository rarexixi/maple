<script setup lang="ts">
import AInputStringMap from "@/components/ant-ext/AInputStringMap.vue";
import { useTemplateRef } from "vue";
import type { FormInstance } from "ant-design-vue";
import common from "@/composables/common";

const conf = defineModel<any>()

const removeConfOption = (index: number) => {
  conf.value.forbiddenConfs.splice(index, 1);
};
const addConfOption = () => {
  conf.value.forbiddenConfs.push({
    name: '',
    replaceParameter: '',
    description: '',
  });
};

const subFormLabelCol = {
  style: {
    width: 'auto'
  }
}

defineExpose({
  validate: common.getFormValidateFun(useTemplateRef<FormInstance>("formRef")),
})
</script>

<template>
  <a-form ref="formRef" :model="conf" :label-col="{ span: 3 }">
    <a-form-item ref="envs" label="环境变量" name="envs">
      <a-input-string-map v-model:value="conf.envs" type="text" />
    </a-form-item>
    <h3>
      <span style="font-weight: bold">用户禁止的配置</span>
      <a-button type="dashed" @click="addConfOption" style="margin-left: 10px">
        <PlusOutlined />
        添加配置项
      </a-button>
    </h3>
    <div style="width: 100%; overflow-x: auto;">
      <table class="config-keys-table" style="text-align: left;">
        <thead>
        <tr>
          <th style="position: sticky; left: 0; z-index: 2"></th>
          <th class="forbidden-conf-name" style="position: sticky; left: 32px; z-index: 2">配置名称</th>
          <th class="forbidden-conf-replace-parameter">替换参数</th>
          <th class="forbidden-conf-description">描述</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(confOption, index) in conf.forbiddenConfs">
          <td style="position: sticky; left: 0; z-index: 2;">
            <a-form-item style="width: 30px">
              <a-button type="link" @click="() => removeConfOption(index)" danger>
                <template #icon>
                  <MinusCircleOutlined />
                </template>
              </a-button>
            </a-form-item>
          </td>
          <td style="position: sticky; left: 32px; z-index: 2;">
            <a-form-item label="" :name="['forbiddenConfs', index, 'name']" :label-col="subFormLabelCol"
                         :rules="[{ required: true, message: '配置不能为空', trigger: 'blur' }]">
              <a-input v-model:value="confOption.name" class="forbidden-conf-name" />
            </a-form-item>
          </td>
          <td>
            <a-form-item label="" :name="['forbiddenConfs', index, 'replaceParameter']" :label-col="subFormLabelCol">
              <a-input v-model:value="confOption.replaceParameter" class="forbidden-conf-replace-parameter" />
            </a-form-item>
          </td>
          <td>
            <a-form-item label="" :name="['forbiddenConfs', index, 'description']" :label-col="subFormLabelCol">
              <a-input v-model:value="confOption.desc" class="forbidden-conf-desc" />
            </a-form-item>
          </td>
        </tr>
        </tbody>
      </table>
    </div>
  </a-form>
</template>

<style scoped>
.forbidden-conf-name {
  width: 170px
}

.forbidden-conf-replace-parameter {
  width: 120px
}

.forbidden-conf-desc {
  width: 170px
}
</style>