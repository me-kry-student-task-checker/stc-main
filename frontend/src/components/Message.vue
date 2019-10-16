<template lang="pug">
	b-alert(dismissible, v-model="show",
		v-bind:class="{ error: type === 'error', success: type === 'success' }").msg {{text}}
</template>

<script>
	export default {
		name: 'Message',
		props: {
			text: {
				type: String,
				required: true
			},
			show: {
				type: Boolean,
				default: true
			},
			type: {
				type: String,
				required: true
			}
		}
	};
</script>

<style lang="scss" scoped>
	.msg {
		border-radius: 5px;
		border: none;
		color: $light;
		cursor: default;
		max-width: 450px;
		width: 100%;
		word-wrap: break-word;
	}
	.error {
		background: red;
	}
	.success {
		background: green;
	}
</style>
