require
		.config({
			paths : {
				angular : '../../assets/bower_components/angular/angular.min',

				angularBootstrap : '../../assets/bower_components/angular-bootstrap/ui-bootstrap-tpls.min',
				csrfInterceptor : '../../assets/bower_components/spring-security-csrf-token-interceptor/dist/spring-security-csrf-token-interceptor.min',
				angularTranslate : '../../assets/bower_components/angular-translate/angular-translate.min',
				angularTranslateLoaderStaticFiles : '../../assets/bower_components/angular-translate-loader-static-files/angular-translate-loader-static-files.min',
				angularUIRouter : '../../assets/bower_components/angular-ui-router/release/angular-ui-router.min',
				angularCookie : '../../assets/bower_components/angular-cookies/angular-cookies.min',
				angularTranslateStorageLocal : '../../assets/bower_components/angular-translate-storage-local/angular-translate-storage-local.min',
				angularTranslateStorageCookie : '../../assets/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.min',
				showErrors : '../../assets/bower_components/angular-bootstrap-show-errors/src/showErrors.min',
				ngTable : '../../assets/bower_components/ng-table/ng-table',
				chosen : '../../assets/angular-chosen-localytics/chosen',
				bootstrapAngularSwitch : '../../assets/bower_components/angular-bootstrap-switch/dist/angular-bootstrap-switch',
				angularjsScrollGlue :'../../assets/bower_components/angularjs-scroll-glue/src/scrollglue',
				angularSimpleChat:'../../assets/bower_components/angular-bootstrap-simple-chat/src/scripts/index',

				welcomeModule : 'welcomeModule'
			},
			shim : {
				angular : {
					exports : "angular"
				},
				angularBootstrap : {
					deps : [ 'angular' ]
				},
				csrfInterceptor : {
					deps : [ 'angular' ]
				},
				angularTranslate : {
					deps : [ 'angular' ]
				},
				angularCookie : {
					deps : [ 'angular' ]
				},
				angularTranslateStorageCookie : {
					deps : [ 'angular', 'angularTranslate', 'angularCookie' ]
				},
				angularTranslateStorageLocal : {
					deps : [ 'angular', 'angularTranslate',
							'angularTranslateStorageCookie' ]
				},
				angularTranslateLoaderStaticFiles : {
					deps : [ 'angular', 'angularTranslate' ]
				},
				angularUIRouter : {
					deps : [ 'angular' ]
				},
				showErrors : {
					deps : [ 'angular' ]
				},
				ngTable : {
					exports : "ngTable",
					deps : [ 'angular' ]
				},

				chosen : {
					exports : "chosen",
					deps : [ 'angular' ]
				},

				bootstrapAngularSwitch : {
					exports : "bootstrapAngularSwitch",
					deps : [ 'angular' ]
				},
				angularjsScrollGlue: {
					exports : "angularjsScrollGlue",
					deps : [ 'angular' ]
				},
				angularSimpleChat: {
					exports : "angularSimpleChat",
					deps : [ 'angular']
				},

				/*
				 * angularUiSelect : { exports : "ui.select" , deps : [
				 * 'angular' ] },
				 */
				welcomeModule : {
					deps : [ 'angular', 'csrfInterceptor', 'angularBootstrap',
							'angularTranslate', 'angularCookie',
							'angularTranslateStorageCookie',
							'angularTranslateStorageLocal',
							'angularTranslateLoaderStaticFiles',
							'angularUIRouter', 'showErrors', 'ngTable',
							'chosen','bootstrapAngularSwitch','angularjsScrollGlue','angularSimpleChat' ]
				}
			}
		});

require([ 'welcomeModule' ], function() {
	angular.bootstrap(document.getElementById('welcomeModule'),
			[ 'welcomeModule' ]);
});
