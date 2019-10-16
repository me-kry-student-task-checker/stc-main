<template lang="pug">
	div.userCard
		h1.name {{fullName}}
		p.email {{user.email}}
</template>

<script>
	export default {
		name: 'UserCard',
		props: {
			user: {
				type: Object,
				required: true
			}
		},
		computed: {
			fullName: function() {
				return this.user.firstName + ' ' + this.user.lastName;
			}
		}
	};
</script>

<style lang="scss" scoped>
	.userCard {
		background: white;
		border: 1px solid black;
		border-radius: 5px;
		height: 50px;
		max-width: 200px;
		padding: 10px;
	}
	.name {
		font-size: 15px;
	}
	.email {
		color: $grey-font;
		font-size: 13px;
		line-height: 2px;
		margin-left: 5px;
	}
</style>
