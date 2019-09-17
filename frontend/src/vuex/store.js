import Vue from 'vue';
import Vuex from 'vuex';
import auth from '../views/Auth/auth-module.js';

Vue.use(Vuex);

export default new Vuex.Store({
	// Global store components
	actions: {},
	mutations: {},
	state: {},
	modules: {
		auth
	}
});
