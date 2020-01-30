<template lang="pug">
	div
		router-link(:to="{name: 'task', params: {id: this.body.id}}").cardLink
			b-card.card(:header="body.name" footer="Click on the card to see more >>")
				.content
					b-card-text.points Closed:
						span.vars {{getStatus(body.done)}}
					b-card-text.points Description:
						span.vars {{body.description}}
					b-card-text.points Creation date:
						span.vars {{body.creationDate | moment('DD/MM/YYYY')}}
</template>

<script>
	export default {
		name: 'TaskCard',
		props: {
			body: {
				type: Object,
				required: true
			}
		},
		methods: {
			getStatus(done) {
				return done ? 'Yes' : 'No';
			}
		}
	};
</script>

<style lang="scss" scoped>
	.cardLink {
		text-decoration: none;
	}
	.card {
		background: $card-bg;
		color: black;
		margin: 20px;
		&:hover {
			background: lightgray;
			cursor: pointer;
		}
	}
	.content {
		margin: 5px;
	}
	.points {
		font-size: 20px;
	}
	.vars {
		font-size: 18px;
		font-style: italic;
		margin-left: 30px;
	}
	.card-header {
		font-size: 25px;
	}
	.card-footer {
		display: flex;
		justify-content: end;
	}
</style>
