<template lang="pug">
	div.regTab.form-group
		h3.regHeader Register
		input(type="text", placeholder="Email", v-model="registerForm.email",
			@keydown="emitRegisterForm" autocomplete="off").input.form-control
		input(type="text", placeholder="First Name", v-model="registerForm.firstName",
			@keydown="emitRegisterForm" autocomplete="off").input.form-control
		input(type="text", placeholder="Last Name", v-model="registerForm.lastName",
			@keydown="emitRegisterForm" autocomplete="off").input.form-control
		select( v-model="registerForm.role", @change="emitRegisterForm").input.form-control
			option(value="" disabled selected) Select a role!
			option(value="ROLE_Student") Student
			option(value="ROLE_Teacher") Teacher
		input(type="password", placeholder="Password", v-model="registerForm.password",
			@keydown="emitRegisterForm" autocomplete="off").input.form-control
		input(type="password", placeholder="Confirm Password", v-model="registerForm.passwordConfirm",
			@keydown="emitRegisterForm" autocomplete="off").input.form-control
</template>

<script>
	export default {
		name: 'Registration',
		data: function() {
			return {
				registerForm: {
					email: '',
					password: '',
					passwordConfirm: '',
					firstName: '',
					lastName: '',
					role: ''
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
		max-width: 450px;
	}
</style>
