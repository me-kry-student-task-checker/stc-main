<template lang="pug">
	div
		div.commentPanel
			b-form-textarea(type="text", placeholder="Type your comment here!",
				v-model="commentToSend.text", no-resize, rows="3", max-rows="8").form-control.commentInput
			button(@click="$emit('send-comment', {commentToSend: commentToSend, files: files})").commentBtn
				i.fa.fa-envelope.fa-2x.mt-1
			button(@click="uploadClick").commentBtn
				i.fa.fa-file.fa-2x.mt-1
		div(v-if="showUpload").uploadPanel
			b-form-file(size="lg", :multiple="true", v-model="files", accept="image/*, .pdf, .txt, .c, .java, .html, .css",
				no-drop, :file-name-formatter="formatNames").fileForm
			button(@click="files = []").resetBtn
				i.fa.fa-undo.fa-2x.mt-1
		div(v-if="comments.length !== 0").commentList
			CommentCard(v-for="(comment) in comments", :key="comment.id", :comment="comment")
		div(v-else).commentList
			p No comments yet! Be the first!
</template>

<script>
	// TODO implement drag and drop and extend acceptable media types
	import CommentCard from './CommentCard';
	export default {
		name: 'CommentSection',
		components: {CommentCard},
		props: {
			comments: {
				type: Array,
				required: true
			}
		},
		data: function() {
			return {
				commentToSend: {
					text: ''
				},
				files: [],
				showUpload: false
			};
		},
		methods: {
			uploadClick() {
				this.showUpload = !this.showUpload;
				this.files = [];
			},
			formatNames(files) {
				return `${files.length} files selected`;
			}
		}
	};
</script>

<style lang="scss" scoped>
	.commentPanel {
		align-items: center;
		display: flex;
		flex-direction: row;
		justify-content: center;
		margin-top: 50px;
	}
	.commentInput {
		margin-right: 20px;
		max-width: 1000px;
		width: 100%;
	}
	.commentBtn {
		background: $secondary;
		border-radius: 5px;
		color: $light;
		margin-right: 10px;
	}
	.resetBtn {
		background: $secondary;
		border-radius: 5px;
		color: $light;
		margin-top: 15px;
		margin-right: 10px;
	}
	.uploadPanel {
		align-items: center;
		display: flex;
		justify-content: center;
	}
	.fileForm {
		margin-top: 20px;
		margin-right: 20px;
		max-width: 1000px;
	}
	.commentList {
		align-items: center;
		display: flex;
		flex-direction: column;
		justify-content: center;
		margin-top: 50px;
	}
</style>
