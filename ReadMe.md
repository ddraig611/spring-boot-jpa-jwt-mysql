2. Spring Boot + Spring Security + JWT + MySQL + React Full Stack Ddraig app

2.1 Installing create-react-app
		npm install -g create-react-app
2.2 Creating the app
		create-react-app polling-app-client
3.2 Installing Additional Dependencies
			cd jwtMysql-app-client
		- And Design: An excellent react based user interface library for designing the user interface.
			npm install react-app-rewired babel-plugin-import react-app-rewire-less --save-dev
		- React Router: Client side routing solution for react apps.			
			npm install antd react-router-dom --save
2.4 Configuring Ant Design
		- Using react-app-rewired to customize default webpack config, We’ll use react-app-rewired to enable customization. Open package.json file and replace the following scripts
		
		"scripts": {
		  "start": "react-scripts start",
		  "build": "react-scripts build",
		  "test": "react-scripts test --env=jsdom",
		  "eject": "react-scripts eject"
		}
		- Overriding configurations with config-overrides.js
		
			const { injectBabelPlugin } = require('react-app-rewired');
			const rewireLess = require('react-app-rewire-less');

			module.exports = function override(config, env) {
				config = injectBabelPlugin(['import', { libraryName: 'antd', style: true }], config);
				config = rewireLess.withLoaderOptions({
				  modifyVars: {
					  "@layout-body-background": "#FFFFFF",
					  "@layout-header-background": "#FFFFFF",
					  "@layout-footer-background": "#FFFFFF"
				  },
				  javascriptEnabled: true
				})(config, env);
				return config;
			};
2.5 setup fabric office ui
	
	npm install react react-dom
	npm install redux redux-thunk react-redux
	npm install fabric fabricjs-react
	npm install office-ui-fabric-react
	
2.6 setup materia date picker
	npm install @material-ui/core
	
2.7 setup canvas paint 
	npm i react-canvas-paint
	
2.8 Create the package.json file
	npm init --y
			
2.9 Running App
		npm start
		