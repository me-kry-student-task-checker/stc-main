<template lang="pug">
	div(id="app")
		div(v-if="showHeader")
			Header(:user="currentUser")
		router-view
</template>

<script>
	import Header from './components/Header.vue';
	import {authHeader} from './helpers/auth-header';
	import {mapActions, mapState} from 'vuex';

	export default {
		name: 'App',
		components: {
			Header
		},
		computed: {
			...mapState(['currentUser']),
			showHeader() {
				return !(this.$route.path === '/auth');
			}
		},
		created() {
			let token = authHeader();
			if (token) {
				this.getCurrentUser();
			}
		},
		methods: {
			...mapActions(['getCurrentUser'])
		}
	};
</script>

<style lang="scss">
#app {
    font-family: "Poppins", sans-serif;
	height: 100vh;
}
</style>
