<template lang="pug">
	div
		p(@click="$router.go(-1)").back << Back to Course
		h3.header Course tasks
		div(v-if="tasks.length !== 0").taskList
			TaskCard(v-for='(task) in tasks', :key="task.id", :body='task')
		div(v-else)
			p.emptyList No tasks for you, Sir!
</template>

<script>
	import {mapActions, mapState} from 'vuex';
	import TaskCard from '../../components/TaskCard.vue';

	export default {
		name: 'Tasks',
		components: {
			TaskCard
		},
		computed: {
			...mapState('tasks', ['tasks'])
		},
		created() {
			let id = this.$route.params.id;
			this.getAllTasks(id);
		},
		methods: {
			...mapActions('tasks', ['getAllTasks'])
		}
	};
</script>

<style lang="scss" scoped>
	.back {
		font-size: 20px;
		margin-left: 50px;
		margin-top: 25px;
		&:hover {
			color: $grey-font;
			cursor: pointer;
		}
	}
	.header {
		border-bottom: 1px solid black;
		font-size: 40px;
		margin: 40px;
		padding-bottom: 20px;
		text-align: center;
	}
	.taskList {
		margin: 20px;
	}
	.emptyList {
		font-size: 20px;
		font-style: italic;
		text-align: center;
	}
</style>
