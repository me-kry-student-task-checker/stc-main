import Vue from 'vue';
import App from './App.vue';
import {router} from './router/router.js';
import store from './vuex/store';
import useInterceptors from './helpers/interceptors';
import VueAuthImage from 'vue-auth-image';

import BootstrapVue from 'bootstrap-vue/dist/bootstrap-vue.esm.min.js';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../node_modules/bootstrap-vue/dist/bootstrap-vue.min.css';
import '@fortawesome/fontawesome-free/css/all.css';
import '@fortawesome/fontawesome-free/js/all.js';

Vue.config.productionTip = false;
Vue.use(BootstrapVue);
Vue.use(require('vue-moment'));
Vue.use(VueAuthImage);

useInterceptors();

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app');
