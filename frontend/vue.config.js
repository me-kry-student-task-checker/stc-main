module.exports = {
	css: {
		loaderOptions: {
			scss: {
				data: `
					@import "@/assets/variables/_custom-variables.scss";
					@import "@/assets/mixins/_mixins.scss";
				`
			}
		}
	}
};
