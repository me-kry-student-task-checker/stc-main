import axios from 'axios';

export default {
	login(credentials) {
		return axios.post('/api/user/auth', credentials);
	},
	register(registerForm) {
		return axios.post('/api/user/registration', registerForm);
	}
};
