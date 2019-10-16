<template lang="pug">
	div.commentCard
		b-card
			div.titleLine
				h2.sender  {{comment.authorId}}
				p.date {{comment.createDate | moment('DD/MM/YYYY') }}
			p.text {{comment.text}}
			div(v-if="comment.files.length !== 0").files
				FileCard(v-for="(file) in comment.files", :key="file.id", :file="file")
</template>

<script>
	import FileCard from './FileCard';
	export default {
		name: 'CommentCard',
		components: {FileCard},
		props: {
			comment: {
				type: Object,
				required: true
			}
		}
	};
</script>

<style lang="scss" scoped>
	.commentCard {
		max-width: 1000px;
		width: 100%;
	}
	.titleLine {
		display: flex;
		flex-direction: row;
	}
	.sender {
		margin-right: 10px;
	}
	.date {
		color: $grey-font;
	}
	.text {
		margin-left: 30px;
		overflow-wrap: break-word;
	}
	.files {
		border-top: 1px solid black;
		display: flex;
		flex-direction: row;
		flex-wrap: wrap;
		padding-top: 5px;
	}
</style>
