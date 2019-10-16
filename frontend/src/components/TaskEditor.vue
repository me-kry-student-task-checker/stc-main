<template lang="pug">
	div.container-fluid
		.row
			.col-lg-8
				.editTab.form-group
					input(type="text", placeholder="Name", v-model="newTask.name", autofocus).input.form-control
					b-form-textarea(type="text", placeholder="Description", v-model="newTask.description",
						no-resize, max-rows="10").input.form-control
			.col-lg-4.buttonPanel
					button(@click="$emit('new-task', newTask)").button Create new task for this Course
</template>

<script>
	export default {
		name: 'TaskEditor',
		data: function() {
			return {
				newTask: {
					id: null,
					name: '',
					description: ''
				}
			};
		}
	};
</script>

<style lang="scss" scoped>
	.editTab {
		margin: 10px 0 0;
	}
	.buttonPanel {
		align-items: center;
		display: flex;
		justify-content: center;
	}
	.button {
		background: $secondary;
		border-radius: 5px;
		color: $light;
		font-size: 22px;
		max-width: 400px;
		min-height: 60px;
		padding: 10px;
		width: 100%;
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
