module.exports = {
	root: true,
	env: {
		node: true
	},
	'extends': [
		'plugin:vue/recommended',
		'@vue/standard'
	],
	plugins: [
		'vue'
	],
	rules: {
		// 'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
		// 'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
		'semi': ['error', 'always'],
		'no-tabs': 0,
		'indent': ['error', 'tab'],
		'no-trailing-spaces': ['error', {'skipBlankLines': true}], // So WebStorm doesn't cry every time you hit space
		'space-before-function-paren': ['error', 'never'],
		'vue/order-in-components': ['warn', {
			order: [
				'el',
				'name',
				'parent',
				'functional',
				['delimiters', 'comments'],
				['components', 'directives', 'filters'],
				'extends',
				'mixins',
				'inheritAttrs',
				'model',
				['props', 'propsData'],
				'data',
				'computed',
				'watch',
				'LIFECYCLE_HOOKS',
				'methods',
				['template', 'render'],
				'renderError'
			]
		}],
		'max-len': [
			'error',
			{
				code: 200,
				ignoreTrailingComments: false,
				ignoreUrls: true
			}
		],
		'max-lines': [
			'error',
			{
				max: 200,
				skipComments: true,
				skipBlankLines: true
			}
		],
		'object-curly-spacing': [2, 'never'],
		'vue/script-indent': ['error', 'tab', {
			baseIndent: 1,
			switchCase: 1
		}],
		'comma-dangle': ['error', {
			'arrays': 'never',
			'objects': 'never',
			'imports': 'never',
			'exports': 'never',
			'functions': 'never'
		}]
	},
	overrides: [
		{
			files: ['*.vue'],
			rules: {
				'vue/require-default-prop': 0,
				'indent': 'off'
			}
		}
	],
	parserOptions: {
		parser: 'babel-eslint'
	}
};
