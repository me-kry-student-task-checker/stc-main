<template lang="pug">
	div.container-fluid
		.row
			.col-lg-8
				p.title {{course.name}}
			.col-lg-4
				div.panel
					router-link(:to="{name: 'tasks', params: {id: this.course.id}}")
						button.panelBtn View tasks
					button(v-if="showTeacherContent", @click="inviteModal").panelBtn Assign students
					button(v-if="showTeacherContent", @click="setShowUpload").panelBtn Upload files
		.row
			.col-lg-12
				div(v-if="showUpload")
					FileUpload(@upload-files="upload")
		.row
			.col-lg-12
				b-button-group.panel
					b-button(@click="toggleCollapse('desc')",
						v-bind:class="{ collSelected: visible === 'desc' }").collBtn Course Description
					b-button(@click="toggleCollapse('students')",
						v-bind:class="{ collSelected: visible === 'students' }").collBtn Students on the Course
					b-button(@click="toggleCollapse('files')",
						v-bind:class="{ collSelected: visible === 'files' }").collBtn Uploaded files
		.row
			.col-lg-12
				b-collapse(:visible="visible === 'desc'")#desc
					b-card
						b-card-text {{course.description}}
				b-collapse(:visible="visible === 'students'")#students
					b-card.users
						div(v-if="course.students.length !== 0").taskList
							UserCard(v-for='(student) in course.students', :key="student.email", :user='student')
						div(v-else)
							p.emptyList No students are assigned to this course yet!
				b-collapse(:visible="visible === 'files'")#files
					b-card
						b-card-text
						div(v-if="course.files.length !== 0").files
							FileCard(v-for="(file) in course.files", :key="file.id", :file="file")
						div(v-else)
							p.emptyList No files uploaded for this course yet!
		div(v-if="showTeacherContent").row
			.col-lg-12
				b-button-group.panel
					b-button(@click="toggleCollapse('task')",
						v-bind:class="{ collSelected: visible === 'task' }").collBtn Add task
		.row
			.col-lg-12
				b-collapse(:visible="visible === 'task'")#desc
					b-card
						TaskEditor(@new-task="addTask")
		.row
			.col-lg-12
				CommentSection(@send-comment="sendComment", :comments="course.comments")
		InviteStudentsModal(:users="invitableStudents", @invite-students="invite", :key="invitableStudents.length")
</template>

<script>
	import {mapActions, mapState} from 'vuex';
	import UserCard from '../../components/UserCard';
	import TaskEditor from '../../components/TaskEditor';
	import InviteStudentsModal from '../../components/InviteStudentsModal';
	import FileCard from '../../components/FileCard';
	import CommentSection from '../../components/CommentSection';
	import FileUpload from '../../components/FileUpload';

	export default {
		name: 'Course',
		components: {
			FileUpload,
			CommentSection,
			FileCard,
			UserCard,
			TaskEditor,
			InviteStudentsModal
		},
		data: function() {
			return {
				visible: 'init',
				showUpload: false
			};
		},
		computed: {
			...mapState(['currentUser']),
			...mapState('course', ['course']),
			...mapState('user', ['invitableStudents']),
			showTeacherContent: function() {
				switch (this.currentUser.role) {
					case 'TEACHER': return true;
					case 'STUDENT': return false;
					default: return false;
				}
			}
		},
		created() {
			let id = this.$route.params.id;
			this.getCourse(id);
			this.getInvitableStudents(id);
		},
		methods: {
			...mapActions('course', ['getCourse', 'sendInvites']),
			...mapActions('tasks', ['createTask']),
			...mapActions('feedback', ['createCourseComment']),
			...mapActions('user', ['getInvitableStudents']),
			...mapActions('file', ['uploadFiles']),
			toggleCollapse(coll) {
				if (this.visible !== coll) {
					this.visible = coll;
				} else {
					this.visible = 'init';
				}
			},
			sendComment(comment) {
				comment.commentToSend.courseId = this.course.id;
				this.createCourseComment(comment);
			},
			inviteModal() {
				this.$bvModal.show('studentInviteModal');
			},
			setShowUpload() {
				this.showUpload = !this.showUpload;
			},
			upload(files) {
				let service = 'COURSE';
				let tagId = this.course.id;
				this.uploadFiles({files: files, service: service, tagId: tagId});
			},
			addTask(task) {
				task.courseId = this.course.id;
				this.createTask(task);
			},
			invite(students) {
				let id = this.$route.params.id;
				this.sendInvites({courseId: id, students: students});
			}
		}
	};
</script>

<style lang="scss" scoped>
	.title {
		font-size: 30px;
		margin: 30px;
	}
	.panel {
		display: flex;
		flex-direction: row;
		justify-content: space-evenly;
		margin: 30px;
	}
	.panelBtn {
		background: $secondary;
		border-radius: 5px;
		color: $light;
		font-size: 18px;
		min-height: 40px;
		&:hover {
			background: #333333;
		}
		&:disabled {
			background: grey;
		}
	}
	.collBtn {
		background: #00aed6;
	}
	.collSelected {
		background: #5a6268;
	}
	.card {
		background: $card-bg;
		border: none;
		margin-left: 35px;
		margin-right: 35px;
	}
	.users {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
	}
	.files {
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
	}
</style>
