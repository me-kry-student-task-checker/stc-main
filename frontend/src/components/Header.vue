<template lang="pug">
	div
		b-navbar(toggleable='lg', type='dark', variant='info', sticky=true).navbar
			b-navbar-brand(href='#')
				router-link.logo(:to="{ name: 'home'}")
					HeaderLogo.logo
			b-navbar-toggle(target='nav-collapse')
			b-collapse#nav-collapse(is-nav='')
				b-navbar-nav.ml-auto
					div(v-if="showAdd")
						b-nav-item(@click="toAdd()")
							i.fas.fa-plus.mr-2
							| Add course
					b-nav-item-dropdown(right='')
						template(v-slot:button-content='')
							em {{fullName}} | {{user.role}}
						b-dropdown-item(@click="logout()") Sign Out
</template>

<script>
	import FixedHeader from 'vue-fixed-header';
	import HeaderLogo from '../assets/img/logo.svg';
	import {mapActions} from 'vuex';

	export default {
		name: 'Header',
		components: {
			FixedHeader, HeaderLogo
		},
		props: {
			user: {
				type: Object,
				required: true
			}
		},
		computed: {
			fullName: function() {
				return this.user.firstName + ' ' + this.user.lastName;
			},
			showAdd: function() {
				switch (this.user.role) {
					case 'ADMIN': return true;
					case 'TEACHER': return true;
					case 'STUDENT': return false;
					default: return false;
				}
			}
		},
		methods: {
			...mapActions('auth', ['logout']),
			toAdd() {
				this.$router.push({name: 'add'});
			}
		}
	};
</script>

<style lang="scss" scoped>
	.navbar {
		background: $bg;
		z-index: 10;
	}
	.navbar.vue-fixed-header--isFixed {
		position: fixed;
		left: 0;
		top: 0;
		width: 100%;
	}
	.logo {
		fill: white;
	}
	.link {
		&:hover {
			text-decoration: none;
		}
	}
</style>
