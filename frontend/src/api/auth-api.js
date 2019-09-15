import axios from './config/axios-instance';

export default {
	login(credentials) {
		return axios.post('/api/user/auth', credentials);
	},
	register(registerForm) {
		return axios.post('/api/user/register', registerForm);
	}
};
