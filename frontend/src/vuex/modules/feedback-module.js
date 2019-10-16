import api from '../../api/feedback-api.js';

export default {
	namespaced: true,
	actions: {
		createCourseComment({dispatch}, comment) {
			api.createCourseComment(comment.commentToSend).then((response) => {
				console.log('Create course comment successful');
				if (comment.files.length !== 0) {
					let service = 'FEEDBACK';
					let tagId = response.data.id;
					let params = {files: comment.files, service: service, tagId: tagId};
					dispatch('file/uploadFiles', params, {root: true});
				} else {
					dispatch('course/getCourse', comment.commentToSend.courseId, {root: true});
				}
			}).catch((error) => {
				console.error('Create course comment unsuccessful', error);
			});
		},
		createTaskComment({dispatch}, comment) {
			api.createTaskComment(comment.commentToSend).then((response) => {
				console.log('Create course comment successful');
				if (comment.files.length !== 0) {
					let service = 'FEEDBACK';
					let tagId = response.data.id;
					let params = {files: comment.files, service: service, tagId: tagId};
					dispatch('file/uploadFiles', params, {root: true});
				} else {
					dispatch('tasks/getTask', comment.commentToSend.taskId, {root: true});
				}
			}).catch((error) => {
				console.error('Create course comment unsuccessful', error);
			});
		}
	},
	mutations: {},
	state: {}
};
