module.exports = {
	devServer: {
		proxy: {
			'/api': {
				target: 'http://192.168.99.102:8060'
			}
		}
	},
	css: {
		loaderOptions: {
			scss: {
				data: `
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
