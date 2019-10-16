import api from '../../api/auth-api.js';
import {router} from '../../router/router.js';

export default {
	namespaced: true,
	actions: {
		loginUser({commit, dispatch}, credentials) {
			api.login(credentials).then((response) => {
				if (response.headers.authorization) {
					// store user details and jw token in local storage to keep user logged in between page refreshes
					localStorage.setItem('access_token', response.headers.authorization);
					dispatch('getCurrentUser', null, {root: true});
					console.log('Login successful');
					router.push({name: 'home'});
				} else {
					throw new Error('There is no token in the response!');
				}
			}).catch((error) => {
				console.error('Login unsuccessful', error);
				commit('setLoginError');
			});
		},
		logout() {
			// remove user from local storage to log user out
			localStorage.removeItem('access_token');
			location.reload();
		},
		registerUser({commit}, registerForm) {
			api.register(registerForm).then((response) => {
				if (response.status !== 401) {
					console.log('Registration successful', response, registerForm);
					commit('setShowConfirmAcc');
				} else {
					throw new Error('Invalid registration form data!');
				}
			}).catch((error) => {
				console.error('Registration unsuccessful', error, registerForm);
				commit('setRegistrationError');
			});
		}
	},
	mutations: {
		setLoginError(state) {
			state.status = {loginError: true, registrationError: false, showConfirmAcc: false};
		},
		setRegistrationError(state) {
			state.status = {loginError: false, registrationError: true, showConfirmAcc: false};
		},
		setShowConfirmAcc(state) {
			state.status = {loginError: false, registrationError: false, showConfirmAcc: true};
		}
	},
	state: {
		status: {
			loginError: false,
			registrationError: false,
			showConfirmAcc: false
		}
	}
};
