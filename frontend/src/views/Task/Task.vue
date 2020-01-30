<template lang="pug">
	div.container-fluid
		.row
			.col-lg-8
				router-link(:to="{name: 'course', params: {id: this.task.courseId}}").back << Back to Parent Course
				h3.title {{task.name}}
				p.desc {{task.description}}
			.col-lg-4
				div.panel
					button(v-if="showComp", @click="setDone(task.id)").panelBtn Mark as done
					div(v-if="task.done")
						i.fas.fa-check-circle.fa-3x.ml-2
		.row
			.col-lg-4
				button(:disabled="task.done || currentUser.role !== 'STUDENT'",
					@click="setComplete(task.id)", v-if="currentUser.role !== 'TEACHER'").taskControlBtn Complete
				div.list
					div(v-if="task.completedStudents.length !== 0")
						p(v-for="(completedStudent) in task.completedStudents").litem {{completedStudent.firstName}} {{completedStudent.lastName}} - {{completedStudent.email}}
					div(v-else)
						p Nobody completed the task yet!
			.col-lg-4
				button(:disabled="task.done || currentUser.role !== 'STUDENT'",
					@click="setHelp(task.id)", v-if="currentUser.role !== 'TEACHER'").taskControlBtn Request help
				div.list
					div(v-if="task.helpNeededStudents.length !== 0")
						p(v-for="(helpStudent) in task.helpNeededStudents").litem {{helpStudent.firstName}} {{helpStudent.lastName}} - {{helpStudent.email}}
					div(v-else)
						p Nobody needs help right now!
			.col-lg-4
				button(:disabled="!showComp", @click="setShowUpload").taskControlBtn Upload file
				div(v-if="showUpload")
					FileUpload(@upload-files="upload")
				div
					div(v-if="task.files.length !== 0").fList
						FileCard(v-for="(file) in task.files", :key="file.id", :file="file")
					div(v-else)
						p.emptyList No files uploaded for this task yet!
		.row
			.col-lg-12
				CommentSection(@send-comment="sendComment", :comments="task.comments")

</template>

<script>
	import {mapActions, mapState} from 'vuex';
	import FileCard from '../../components/FileCard';
	import CommentSection from '../../components/CommentSection';
	import FileUpload from '../../components/FileUpload';
	const POLL_INTERVAL_MS = 5000;
	export default {
		name: 'Task',
		components: {FileUpload, CommentSection, FileCard},
		data: function() {
			return {
				showUpload: false
			};
		},
		computed: {
			...mapState(['currentUser']),
			...mapState('tasks', ['task']),
			showComp: function() {
				switch (this.currentUser.role) {
					case 'ADMIN': return true;
					case 'TEACHER': return true;
					case 'STUDENT': return false;
					default: return false;
				}
			}
		},
		created() {
			let id = this.$route.params.id;
			this.getTask(id);
			setInterval(() => {
				this.getTask(id);
			}, POLL_INTERVAL_MS);
		},
		methods: {
			...mapActions('tasks', ['getTask', 'setComplete', 'setDone', 'setHelp']),
			...mapActions('feedback', ['createTaskComment']),
			...mapActions('file', ['uploadFiles']),
			sendComment(comment) {
				comment.commentToSend.taskId = this.task.id;
				this.createTaskComment(comment);
			},
			setShowUpload() {
				this.showUpload = !this.showUpload;
			},
			upload(files) {
				let service = 'TASK';
				let tagId = this.task.id;
				this.uploadFiles({files: files, service: service, tagId: tagId});
			}
		}
	};
</script>

<style lang="scss" scoped>
	.back {
		color: black;
		font-size: 20px;
		margin-left: 50px;
		&:hover {
			color: $grey-font;
			cursor: pointer;
			text-decoration: none;
		}
	}
	.title {
		margin-top: 20px;
		margin-left: 10px;
	}
	.desc {
		margin-top: 10px;
		margin-left: 20px;
		overflow-wrap: break-word;
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
	}
	.taskControlBtn {
		background: $secondary;
		border-radius: 5px;
		color: $light;
		font-size: 18px;
		min-height: 40px;
		width: 100%;
		&:hover {
			background: #333333;
		}
		&:disabled {
			background: grey;
		}
	}
	.list {
		border: 1px solid black;
		display: flex;
		flex-direction: column;
		flex-wrap: wrap;
		margin: 5px;
		padding: 10px 10px 5px;
	}
	.fList {
		border: 1px solid black;
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		margin: 5px;
		padding: 10px 10px 5px;
	}
	.litem {
		background: $bg;
		border-radius: 5px;
		color: $light;
		cursor: default;
		margin-top: -3px;
		margin-bottom: 10px;
		padding: 5px;
		text-align: center;
		width: 100%;
	}
</style>
