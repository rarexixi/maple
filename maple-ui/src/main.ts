import {createApp} from "vue"
import App from './App.vue'
import router from './router'
import Antd, {message, Button, Input, InputNumber, RadioGroup, Textarea, Switch, Select} from 'ant-design-vue'
import * as AntdIcons from '@ant-design/icons-vue'

import 'ant-design-vue/dist/antd.css'
import '@/assets/styles/antv-override.less'
import '@/assets/main.css'

[Input].forEach((element: any) => element.props.size.default = 'small')
const app = createApp(App)

app.use(Antd)
app.use(router)
app.use(Antd, {size: 'small'});

app.mount('#app')
app.config.globalProperties.$message = message

for (const iconName in AntdIcons) {
    if (!iconName.endsWith('Outlined')) {
        continue;
    }
    app.component(iconName, AntdIcons[iconName]);
}
