import Vue from 'vue';
import Vuex from 'vuex';

import userApi from '../api/user-api.js';

import auth from './modules/auth-module.js';
import course from './modules/course-module.js';
import tasks from './modules/tasks-module.js';
import feedback from './modules/feedback-module.js';
import user from './modules/user-module.js';
import file from './modules/file-module.js';

Vue.use(Vuex);

export default new Vuex.Store({
	// Global store
	actions: {
		getCurrentUser({commit}) {
			userApi.whoAmI().then((response) => {
				if (response.data) {
					commit('setCurrentUser', response.data);
					console.log('Who am I successful', response.data);
				}
			}).catch((error) => {
				localStorage.removeItem('access_token');
				console.error('Who am I unsuccessful', error);
			});
		}
	},
	mutations: {
		setCurrentUser(state, user) {
			state.currentUser = user;
		}
	},
	state: {
		currentUser: {
			email: '',
			firstName: '',
			lastName: '',
			role: '',
			enabled: ''
		}
	},
	modules: {
		auth,
		course,
		tasks,
		feedback,
		user,
		file
	}
});
