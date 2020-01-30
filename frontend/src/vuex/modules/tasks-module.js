import api from '../../api/task-api.js';
import {router} from '../../router/router';

export default {
	namespaced: true,
	actions: {
		createTask({commit}, newTask) {
			api.create(newTask).then((response) => {
				router.push({name: 'task', params: {id: response.data.id}});
				console.log('Create task successful');
			}).catch((error) => {
				console.error('Create task unsuccessful', error);
			});
		},
		getAllTasks({commit}, courseId) {
			api.getAllTasks(courseId).then((response) => {
				commit('setTasks', response.data);
				console.log('Get tasks successful');
			}).catch((error) => {
				console.error('Get tasks unsuccessful', error);
			});
		},
		getTask({commit}, taskId) {
			api.getOneTask(taskId).then((response) => {
				commit('setTask', response.data);
				console.log('Get task successful', response.data);
			}).catch((error) => {
				console.error('Get task unsuccessful', error);
			});
		},
		setDone({commit, dispatch}, taskId) {
			api.setDone(taskId).then((response) => {
				dispatch('getTask', taskId);
				console.log('Set done successful', taskId);
			}).catch((error) => {
				console.error('Set done unsuccessful', error);
			});
		},
		setComplete({commit, dispatch}, taskId) {
			api.setComplete(taskId).then((response) => {
				dispatch('getTask', taskId);
				console.log('Set complete successful', taskId);
			}).catch((error) => {
				console.error('Set complete unsuccessful', error);
			});
		},
		setHelp({commit, dispatch}, taskId) {
			api.setHelp(taskId).then((response) => {
				dispatch('getTask', taskId);
				console.log('Set help successful', taskId);
			}).catch((error) => {
				console.error('Set help unsuccessful', error);
			});
		}
	},
	mutations: {
		setTasks(state, tasks) {
			state.tasks = tasks;
		},
		setTask(state, task) {
			state.task = task;
		}
	},
	state: {
		tasks: [],
		task: {
			id: 0,
			name: '',
			description: '',
			isDone: false,
			files: [],
			completedStudents: [],
			helpNeededStudents: [],
			comments: []
		}
	}
};
