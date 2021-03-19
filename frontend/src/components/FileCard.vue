<template lang="pug">
	div(@click="$bvModal.show(getId)").imgIcon
		i.fa.fa-file.fa-2x.m-2
		div.details
			p.details {{getFileType}} file
			p.details {{file.uploadDate | moment('DD/MM/YYYY') }}
			p.details {{file.uploadedBy}}

		b-modal(:id="getId", hide-footer, :title="file.name", size="lg")
			div(v-if="getFileType === 'IMAGE'")
				b-img(fluid-grow, thumbnail, v-auth-image="file.downloadLink", :alt="file.name")
			//div(v-else-if="getFileType === 'TEXT'")

			//div(v-else-if="getFileType === 'PDF'")

			// a(:href="file.downloadLink") Download Link
</template>

<script>
	// TODO incorporate file type changes
	// TODO file reader for text files, download button maybe in center of image
	export default {
		name: 'FileCard',
		props: {
			file: {
				type: Object,
				required: true
			}
		},
		computed: {
			getId: function() {
				return 'imageModal' + this.$vnode.key;
			},
			getFileType: function() {
				if (this.file.contentType.includes('image')) {
					return 'IMAGE';
				} else if (this.file.contentType.includes('text')) {
					return 'TEXT';
				} else if (this.file.contentType.includes('application/pdf')) {
					return 'PDF';
				} else return 'UNKNOWN';
			}
		}
	};
</script>

<style lang="scss" scoped>
	.imgIcon {
		display: flex;
		flex-direction: column;
		align-items: center;
		max-width: 150px;
		overflow: hidden;
		text-align: center;
		width: 100%;
		&:hover {
			background: $grey-font;
			cursor: pointer;
		}
	}
	.details {
		line-height: 8px;
	}
</style>
