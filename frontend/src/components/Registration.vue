<template lang="pug">
	div.regTab
		h3.regHeader Register
		input(type="text", placeholder="Email", v-model="registerForm.email",
			@keydown="emitRegisterForm").input
		input(type="password", placeholder="Password", v-model="registerForm.password",
			@keydown="emitRegisterForm").input
		input(type="text", placeholder="First Name", v-model="registerForm.firstName",
				@keydown="emitRegisterForm").input
		input(type="text", placeholder="Last Name", v-model="registerForm.lastName",
			@keydown="emitRegisterForm").input
</template>

<script>
	export default {
		name: 'Registration',
		data: function() {
			return {
				registerForm: {
					email: '',
					password: '',
					firstName: '',
					lastName: ''
				}
			};
		},
		methods: {
			emitRegisterForm() {
				this.$emit('registerFormChange', this.registerForm);
			}
		}
	};
</script>

<style lang="scss" scoped>
	.regTab {
		@include center;
		align-items: center;
		display: flex;
		flex-direction: column;
	}
	.regHeader {
		font-size: 35px;
		margin-bottom: 10px;
		color: $light;
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
