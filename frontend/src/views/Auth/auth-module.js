import api from '../../api/auth-api.js';

export default {
	namespaced: true,
	actions: {
		loginUser({commit}, credentials) {
			api.login(credentials).then((response) => {
				console.log('Login successful', credentials);
			}).catch((error) => {
				console.error('Login unsuccessful', error, credentials);
			});
		},
		registerUser({commit}, registerForm) {
			api.register(registerForm).then((response) => {
				console.log('Registration successful', registerForm);
			}).catch((error) => {
				console.error('Registration unsuccessful', error, registerForm);
			});
		}
	},
	mutations: {},
	state: {}
};
