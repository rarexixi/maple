<!--https://github.com/vueComponent/ant-design-vue/tree/cb3c002e17f0f4f5b3e8d01846069da0e2645aff/components/form/demo-->
<template>
  <span>
    <a-input type="text" :value="value.number" style="width: 100px" @change="onNumberChange" />
    <a-select :value="value.currency" :options="[{ value: 'rmb', label: 'RMB' }, { value: 'dollar', label: 'Dollar' }]"
              style="width: 80px; margin: 0 8px" @change="onCurrencyChange"></a-select>
  </span>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import type { PropType } from "vue";
import {Form} from "ant-design-vue";

export type Currency = 'rmb' | 'dollar';

interface PriceValue {
  number: number;
  currency: Currency;
}
export default defineComponent({
  props: {
    value: { type: Object as PropType<PriceValue>, isRequired: true },
  },
  emits: ['update:value'],
  setup(props, { emit }) {
    const formItemContext = Form.useInjectFormItemContext();
    const triggerChange = (changedValue: { number?: number; currency?: Currency }) => {
      emit('update:value', { ...props.value, ...changedValue });
      formItemContext.onFieldChange();
    };
    const onNumberChange = (e: InputEvent) => {
      const newNumber = parseInt((e.target as any).value || '0', 10);
      triggerChange({ number: newNumber });
    };
    const onCurrencyChange = (newCurrency: Currency) => {
      triggerChange({ currency: newCurrency });
    };

    return {
      onNumberChange,
      onCurrencyChange,
    };
  },
});
</script>