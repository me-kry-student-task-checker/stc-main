import Vue from 'vue';
import Router from 'vue-router';
import {routes} from './routes.js';

Vue.use(Router);

export const router = new Router({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
});

router.beforeEach((to, from, next) => {
	// redirect to login page if not logged in and trying to access a restricted page
	const publicPages = ['/auth'];
	const authRequired = !publicPages.includes(to.path);
	const loggedIn = localStorage.getItem('access_token');
	if (authRequired && !loggedIn) {
		return next('/auth');
	}
	next();
});
