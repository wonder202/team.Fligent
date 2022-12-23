import { createApp } from 'vue'
import App from './App.vue'
import routes from './routes/index';
import stores from './stores/index';

import BootstrapVue3 from 'bootstrap-vue-3'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue-3/dist/bootstrap-vue-3.css'

import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app= createApp(App);
app.use(routes);
app.use(stores);
app.use(BootstrapVue3)
app.use(ElementPlus);
app.mount('#app');
window.Kakao.init('05b4ae7bd3bb1e351380f1a5f7b4285c');