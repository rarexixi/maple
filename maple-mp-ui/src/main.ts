import 'ant-design-vue/dist/reset.css'
import '@/assets/styles/main.css'
import '@/assets/styles/antv-override.less'

import { createApp } from 'vue'
import { createPinia } from 'pinia'
import Antd from 'ant-design-vue'
import * as AntdIcons from '@ant-design/icons-vue'

import App from './App.vue'
import router from './router'

import AInputNumberRanger from "@/components/ant-ext/AInputNumberRanger.vue"
import AFlexBr from "@/components/ant-ext/AFlexBr.vue"

const app = createApp(App)

app.use(Antd)
app.use(createPinia())
app.use(router)

app.component('AInputNumberRanger', AInputNumberRanger);
app.component('AFlexBr', AFlexBr);

for (const iconName in AntdIcons) {
    if (!iconName.endsWith('Outlined')) {
        continue;
    }
    app.component(iconName, AntdIcons[iconName]);
}

app.mount('#app')
