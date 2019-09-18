<template lang="pug">
	div.padding
		div(v-if="showRegister")
			registration(@registerFormChange="setRegisterForm")
			div.controls
				button(@click="loginUser(credentials)").buttons Sign up
				button(@click="changeTab").buttons Back to login
		div(v-else)
			login(@credentialChange="setCredentials")
			div.controls
				button(@click="registerUser(registerForm)").buttons
					i.fa.fa-sign-in 		Sign in
				button(@click="changeTab").buttons Register a new account
</template>

<script>
	import {mapActions} from 'vuex';
	import Login from '../../components/Login.vue';
	import Registration from '../../components/Registration.vue';

	export default {
		name: 'Auth',
		components: {
			Login, Registration
		},
		data: function() {
			return {
				showRegister: false,
				credentials: {},
				registerForm: {}
			};
		},
		methods: {
			...mapActions('auth', ['loginUser', 'registerUser']),
			changeTab() {
				this.showRegister = !this.showRegister;
				this.credentials = {};
				this.registerForm = {};
			},
			setCredentials(value) {
				this.credentials = value;
			},
			setRegisterForm(value) {
				this.registerForm = value;
			}
		}
	};
</script>

<style lang="scss" scoped>
	.padding {
		background: $bg;
		position: fixed;
		width: 100%;
		height: 100%;
		left: 0;
		top: 0;
		z-index: 10;
		padding-top: 13%;
	}
	.controls {
		@include center;
		align-items: center;
		display: flex;
		margin-top: 20px;
		flex-direction: column;
	}
	.buttons {
		background: $secondary;
		border: none;
		border-radius: 40px;
		color: $light;
		font-size: 20px;
		height: 50px;
		margin: 10px 18px 0 18px;
		outline: none;
		width: 450px;
		&:hover {
			background: #333333;
		}
	}
</style>
