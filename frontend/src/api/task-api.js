import axios from 'axios';

export default {
	create(newTask) {
		return axios.post('/api/task/create', newTask);
	},
	getAllTasks(courseId) {
		return axios.get('/api/task/getAll/' + courseId);
	},
	getOneTask(taskId) {
		return axios.get('/api/task/get/' + taskId);
	},
	setDone(taskId) {
		return axios.patch('/api/task/setDone/' + taskId);
	},
	setComplete(taskId) {
		return axios.patch('/api/task/setComplete/' + taskId);
	},
	setHelp(taskId) {
		return axios.patch('/api/task/toggleHelp/' + taskId);
	}
};
