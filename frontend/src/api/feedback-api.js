import axios from 'axios';

export default {
	createCourseComment(comment) {
		return axios.post('/api/feedback/createCourseComment', comment);
	},
	createTaskComment(comment) {
		return axios.post('/api/feedback/createTaskComment', comment);
	}
};
