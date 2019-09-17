import Home from '../views/Home/Home.vue';
import Auth from '../views/Auth/Auth';

export const routes = [
	{
		path: '/',
		name: 'home',
		component: Home
	},
	{
		path: '/auth',
		name: 'auth',
		component: Auth
	}
];
