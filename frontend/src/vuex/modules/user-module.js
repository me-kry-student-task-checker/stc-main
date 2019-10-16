import api from '../../api/user-api.js';

export default {
	namespaced: true,
	actions: {
		getInvitableStudents({commit}, courseId) {
			api.getNonAssignedStudents(courseId).then((response) => {
				commit('setInvitableStudents', response.data);
				console.log('Invitable student get successful', courseId);
			}).catch((error) => {
				console.error('Invitable student ge unsuccessful', error);
			});
		}
	},
	mutations: {
		setInvitableStudents(state, invitableStudents) {
			state.invitableStudents = invitableStudents;
		}
	},
	state: {
		invitableStudents: []
	}
};
