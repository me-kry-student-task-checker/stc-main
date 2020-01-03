<template lang="pug">
	div
		router-link(:to="{name: 'task', params: {id: this.body.id}}")
			b-card.card(:header="body.name" footer="Click on the card to see more >>")
				.content
					b-card-text.points Done:
						span.vars {{body.done}}
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
		}
	};
</script>

<style lang="scss" scoped>
	.card {
		background: $card-bg;
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
