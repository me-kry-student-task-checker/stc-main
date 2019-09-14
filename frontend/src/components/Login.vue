<template lang="pug">
	div.loginTab
		h3.loginHeader Login
		input(type="text", placeholder="Email", v-model="credential.email",
			@keydown="emitCredentials").input
		input(type="password", placeholder="Password", v-model="credential.password",
			@keydown="emitCredentials").input
</template>

<script>
	export default {
		name: 'Login',
		data: function() {
			return {
				credential: {
					email: '',
					password: ''
				}
			};
		},
		methods: {
			emitCredentials() {
				this.$emit('credentialChange', this.credential);
			}
		}
	};
</script>

<style lang="scss" scoped>
	.loginTab {
		@include center;
		align-items: center;
		display: flex;
		flex-direction: column;
	}
	.loginHeader {
		font-size: 35px;
		margin-bottom: 10px;
		color: $light;
		cursor: default;
	}
	.input {
		background-color: $primary;
		border: none;
		border-radius: 40px;
		font-size: 18px;
		height: 50px;
		margin: 12px 18px 0 18px;
		outline: none;
		padding-left: 40px;
		width: 450px;
	}
</style>
