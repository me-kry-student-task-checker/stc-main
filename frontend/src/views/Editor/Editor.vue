<template lang="pug">
	div.container
		.row
			.col-lg-12
				.editTab.form-group
					h3.editHeader Add a new Course
					input(type="text", placeholder="Name", v-model="newCourse.name", autofocus).input.form-control
					b-form-textarea(type="text", placeholder="Description", v-model="newCourse.description",
						no-resize, max-rows="10").input.form-control
					.buttonPanel
						button(@click="createCourse(newCourse)").button Create new course
</template>

<script>
	import {mapActions} from 'vuex';

	export default {
		name: 'CourseEditor',
		data: function() {
			return {
				newCourse: {
					id: null,
					name: '',
					description: ''
				}
			};
		},
		methods: {
			...mapActions('course', ['createCourse'])
		}
	};
</script>

<style lang="scss" scoped>
	.editTab {
		margin-top: 50px;
	}
	.editHeader {
		font-size: 35px;
		margin-bottom: 18px;
		color: black;
	}
	.buttonPanel {
		display: flex;
		justify-content: center;
	}
	.button {
		background: $secondary;
		border: none;
		border-radius: 40px;
		color: $light;
		font-size: 20px;
		height: 50px;
		margin: 15px 18px 0 18px;
		outline: none;
		width: 500px;
		&:hover {
			background: #333333;
		}
	}
	.input {
		background-color: $primary;
		border-color: $bg;
		border-radius: 5px;
		border-width: 2px;
		font-size: 18px;
		height: 50px;
		margin: 12px 18px 0 18px;
		outline: none;
		padding-left: 40px;
		width: 100%;
	}
</style>
