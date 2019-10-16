import axios from 'axios';

export default {
	whoAmI() {
		return axios.get('/api/user/me');
	},
	getNonAssignedStudents(courseId) {
		return axios.get('/api/user/student/notassigned/' + courseId);
	}
};
