module.exports = {
	devServer: {
		proxy: {
			'/api': {
				target: 'http://localhost:8060'
			}
		}
	},
	css: {
		loaderOptions: {
			scss: {
				additionalData: `
					@import "@/assets/variables/_custom-variables.scss";
					@import "@/assets/mixins/_mixins.scss";
				`
			}
		}
	},
	chainWebpack: (config) => {
		const svgRule = config.module.rule('svg');

		svgRule.uses.clear();

		svgRule
			.use('vue-svg-loader')
			.loader('vue-svg-loader');
	}
};
