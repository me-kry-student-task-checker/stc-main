<template lang="pug">
	div.panel
		b-form-file(size="lg", :multiple="true", v-model="files", accept="image/*, .pdf, .txt, .c, .java, .html, .css",
			no-drop, :file-name-formatter="formatNames").fileForm
		button(@click="files = []").panelBtn Reset
		button(:disabled="files.length === 0", @click="$emit('upload-files', files)").panelBtn Upload
</template>

<script>
	// TODO implement drag and drop and extend acceptable media types
	// TODO msg on un/successful upload
	export default {
		name: 'FileUpload',
		data: function() {
			return {
				files: []
			};
		},
		methods: {
			formatNames(files) {
				return `${files.length} files selected`;
			}
		}
	};
</script>

<style lang="scss" scoped>
	.panel {
		display: flex;
		justify-content: center;
	}
	.fileForm {
		max-width: 1500px;
	}
	.panelBtn {
		background: $secondary;
		border-radius: 5px;
		color: $light;
		font-size: 18px;
		min-height: 40px;
		&:hover {
			background: #333333;
		}
		&:disabled {
			background: grey;
		}
	}
</style>
