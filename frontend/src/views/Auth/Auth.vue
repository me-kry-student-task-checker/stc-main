<template lang="pug">
	div.contentPane
		.container
			.row
				.col-lg-12
					div(v-if="showRegister")
						registration(@registerFormChange="setRegisterForm")
						div.controlsAndMsg.form-group
							button(@click="registerUser(registerForm) && changeTab()").buttons.form-control Sign up
							button(@click="changeTab").buttons.form-control Back to login
					div(v-else)
						login(@credentialChange="setCredentials")
						div.controlsAndMsg.form-group
							button(@click="loginUser(credentials)").buttons.form-control Sign in
							button(@click="changeTab").buttons.form-control Registration
						div.controlsAndMsg
							message(:text="'Something is messed up with your registration!'"
								:show="status.registrationError" :type="'error'")
							message(:text="'Please try again!'" :show="status.loginError" :type="'error'")
							message( :text="'Please confirm your account via the email you will receive shortly!'"
								:show="status.showConfirmAcc" :type="'success'")
</template>

<script>
	import {mapActions, mapState} from 'vuex';
	import Login from '../../components/Login.vue';
	import Registration from '../../components/Registration.vue';
	import Message from '../../components/Message';

	export default {
		name: 'Auth',
		components: {
			Login, Registration, Message
		},
		data: function() {
			return {
				showRegister: false,
				credentials: {},
				registerForm: {}
			};
		},
		computed: {
			...mapState('auth', ['status'])
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
	.contentPane {
		@include center;
		background: $bg;
		height: 100%;
	}
	.controlsAndMsg {
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
		max-width: 450px;
		&:hover {
			background: #333333;
		}
	}
</style>
