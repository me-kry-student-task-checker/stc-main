import axios from 'axios';
import {authHeader} from './auth-header.js';
import {router} from '../router/router.js';

export default function execute() {
	axios.interceptors.request.use(function(request) {
		request.headers['Authorization'] = authHeader();
		return request;
	}, function(error) {
		location.reload();
		return error;
	});

	axios.interceptors.response.use(function(response) {
		return response;
	}, function(error) {
		if (error.response.status === 401 && !(router.currentRoute.path === '/auth')) {
			localStorage.removeItem('access_token');
			location.reload();
		}
		return error;
	});
}
