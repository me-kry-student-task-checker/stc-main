import api from '../../api/course-api.js';
import {router} from '../../router/router';

export default {
	namespaced: true,
	actions: {
		createCourse({commit}, course) {
			api.create(course).then((response) => {
				router.push({name: 'course', params: {id: response.data.id}});
				console.log('Save successful', course);
			}).catch((error) => {
				console.error('Save unsuccessful', error, course);
			});
		},
		getCourses({commit}) {
			api.getAll().then((response) => {
				commit('setCourses', response.data);
				console.log('Get all courses successful');
			}).catch((error) => {
				console.error('Get all courses unsuccessful', error);
			});
		},
		getCourse({commit}, id) {
			api.get(id).then((response) => {
				commit('setCourse', response.data);
				console.log('Get course successful', id);
			}).catch((error) => {
				console.error('Get course unsuccessful', error);
			});
		},
		sendInvites({commit}, params) {
			api.inviteStudents(params.courseId, params.students).then((response) => {
				console.log('Invites successfully sent', response);
			}).catch((error) => {
				console.error('Invite action encountered an error', error);
			});
		}
	},
	mutations: {
		setCourses(state, courses) {
			state.courses = courses;
		},
		setCourse(state, course) {
			state.course = course;
		}
	},
	state: {
		courses: [],
		course: {
			id: 0,
			name: '',
			description: '',
			creator: {},
			students: [],
			tasks: [],
			comments: [],
			files: []
		}
	}
};
