import axios from 'axios';

export default {
	create(course) {
		return axios.post('/api/course/create', course);
	},
	getAll() {
		return axios.get('/api/course/getAll');
	},
	get(id) {
		return axios.get('/api/course/get/' + id);
	},
	inviteStudents(courseId, students) {
		return axios.post('/api/user/assignStudentsToCourse', {courseId: courseId, studentEmails: students});
	}
};
