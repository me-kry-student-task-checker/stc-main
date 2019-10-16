import store from '../vuex/store.js';
import Home from '../views/Home/Home.vue';
import Auth from '../views/Auth/Auth.vue';
import Editor from '../views/Editor/Editor.vue';
import Course from '../views/Course/Course.vue';
import Tasks from '../views/Tasks/Tasks.vue';
import Task from '../views/Task/Task.vue';

export const routes = [
	{
		path: '/',
		name: 'home',
		component: Home
	},
	{
		path: '/auth',
		name: 'auth',
		component: Auth,
		beforeEnter: (to, from, next) => {
			let token = localStorage.getItem('access_token');
			if (token) {
				next('/');
			}
			next();
		}
	},
	{
		path: '/add',
		name: 'add',
		component: Editor,
		beforeEnter: (to, from, next) => {
			let role = store.state.currentUser.role;
			if (role === 'STUDENT') {
				next('/');
			}
			next();
		}
	},
	{
		path: '/course/:id',
		name: 'course',
		component: Course
	},
	{
		path: '/tasks/:id',
		name: 'tasks',
		component: Tasks
	},
	{
		path: '/task/:id',
		name: 'task',
		component: Task
	}
];
