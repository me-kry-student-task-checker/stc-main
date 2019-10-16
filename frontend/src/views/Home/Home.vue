<template lang="pug">
	div
		h3.header Your courses
		div(v-if="courses.length !== 0").courseList
			CourseCard(v-for="(course) in courses", :key="course.id", :body='course')
		div(v-else)
			p.emptyList No courses for you, Sir!
</template>

<script>
	import {mapActions, mapState} from 'vuex';
	import CourseCard from '../../components/CourseCard.vue';

	export default {
		name: 'Home',
		components: {
			CourseCard
		},
		computed: {
			...mapState('course', ['courses'])
		},
		created() {
			this.getCourses();
		},
		methods: {
			...mapActions('course', ['getCourses'])
		}
	};
</script>

<style lang="scss" scoped>
	.header {
		border-bottom: 1px solid black;
		font-size: 40px;
		margin: 40px;
		padding-bottom: 20px;
		text-align: center;
	}
	.courseList {
		margin: 20px;
	}
	.emptyList {
		font-size: 20px;
		font-style: italic;
		text-align: center;
	}
</style>
