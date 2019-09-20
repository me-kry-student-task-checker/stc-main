import Vue from 'vue';
import App from './App.vue';
import router from './router/router';
import store from './vuex/store';

import BootstrapVue from 'bootstrap-vue/dist/bootstrap-vue.esm.min.js';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import '../node_modules/bootstrap-vue/dist/bootstrap-vue.min.css';
import '../node_modules/font-awesome/scss/font-awesome.scss';

Vue.config.productionTip = false;

Vue.use(BootstrapVue);

new Vue({
	router,
	store,
	render: h => h(App)
}).$mount('#app');
