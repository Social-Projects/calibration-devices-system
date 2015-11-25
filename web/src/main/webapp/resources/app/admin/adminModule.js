angular
    .module(
    'adminModule',
    ['spring-security-csrf-token-interceptor', 'ui.bootstrap',
        'ui.router', 'ui.bootstrap.showErrors', 'ngTable',
        'pascalprecht.translate', 'ngCookies', 'ui.select', 'ngSanitize', 'localytics.directives', 'daterangepicker', 'checklist-model','ngAnimate', 'toaster',
        'angular-loading-bar'])

    .config(
    [
        '$translateProvider',
        '$stateProvider',
        '$urlRouterProvider',
        'showErrorsConfigProvider',
        'cfpLoadingBarProvider',
        '$provide', '$httpProvider',
        function ($translateProvider, $stateProvider,
                  $urlRouterProvider, showErrorsConfigProvider,cfpLoadingBarProvider, $provide, $httpProvider) {

            $httpProvider.interceptors.push('responseObserver');

            showErrorsConfigProvider.showSuccess(true);
            cfpLoadingBarProvider.includeSpinner = false;
            cfpLoadingBarProvider.latencyThreshold = 500;

            /**
             * i18n configuration.
             */
            $translateProvider.useStaticFilesLoader({
                prefix: 'resources/assets/i18n/welcome-',
                suffix: '.json'
            });
            $translateProvider.useLocalStorage();
            $translateProvider
                .useSanitizeValueStrategy('escaped');
            $translateProvider.preferredLanguage('ukr');
            /**
             * Routing configuration.
             */
            $urlRouterProvider.otherwise('/');
            $stateProvider
                .state(
                'main',
                {
                    url: '/',
                    templateUrl: 'resources/app/admin/views/main-panel.html'
                })
                .state(
                'organizations',
                {
                    url: '/organizations',
                    templateUrl: 'resources/app/admin/views/organizations-panel.html'
                })
                .state(
                'agreements',
                {
                    url: '/agreements',
                    templateUrl: 'resources/app/admin/views/agreement-panel.html'
                })
                .state(
                'users',
                {
                    url: '/users',
                    templateUrl: 'resources/app/admin/views/users-panel.html'
                })
                .state(
                'sys-admins',
                {
                    url: '/sys-admins',
                    templateUrl: 'resources/app/admin/views/sys-admins-panel.html'
                })
                .state(
                'address',
                {
                    url: '/address',
                    templateUrl: 'resources/app/admin/views/address-panel.html'
                })
                .state(
                'device-category',
                {
                    url: '/device-category',
                    templateUrl: 'resources/app/admin/views/devices-panel.html'
                })
                .state(
                'counters-type',
                {
                    url: '/counters-type',
                    templateUrl: 'resources/app/admin/views/counters-type-panel.html'
                })
                .state(
                'settings',
                {
                    url: '/settings',
                    templateUrl: 'resources/app/admin/views/settings-panel.html'
                })
                .state(
                '403',
                {
                    url: '/403',
                    templateUrl: 'resources/app/admin/views/403.html'
                })
                .state(
                '404',
                {
                    url: '/404',
                    templateUrl: 'resources/app/admin/views/404.html'
                })
                .state("measuring-equipment-admin", {
                    url: '/calibration-module',
                    templateUrl: 'resources/app/admin/views/measurement-equipments.html',
                    controller: 'MeasuringEquipmentControllerAdmin'
                })
                .state("unsuitability-reasons", {
                    url: '/unsuitability-reasons',
                    templateUrl: 'resources/app/admin/views/unsuitability-reasons.html',
                    controller: 'UnsuitabilityReasonController'
                });;
            /*
             Extended ui-select-choices: added watch for ng-translate event called translateChangeEnd
             When translation of page will end, items of select (on the scope) will be changed too.
             Then we refresh the items of select to get them from scope.
             */
            $provide.decorator('uiSelectDirective', function( $delegate, $parse, $injector) {
                var some_directive = $delegate[ 0],
                    preCompile = some_directive.compile;

                some_directive.compile = function compile() {
                    var link = preCompile.apply( this, arguments );

                    return function( scope, element, attrs, controller ) {
                        link.apply( this, arguments );

                        var $select = controller[ 0 ];

                        var rootScope= $injector.get('$rootScope');

                        rootScope.$on('$translateChangeEnd', function(event){
                            scope.setTypeDataLanguage();
                            $select.refreshItems();
                        });

                    };
                };

                return $delegate;
            });
        }]);

angular.module('adminModule').run(function (paginationConfig) {
    paginationConfig.firstText = 'Перша';
    paginationConfig.previousText = 'Попередня';
    paginationConfig.nextText = 'Наступна';
    paginationConfig.lastText = 'Остання';
});

define(['controllers/TopNavBarController', 'controllers/MainPanelController',

    'controllers/MeasuringEquipmentControllerAdmin',
    'controllers/MeasuringEquipmentAddModalControllerAdmin',
    'controllers/MeasuringEquipmentEditModalControllerAdmin',
    'controllers/MeasuringEquipmentDisableModalControllerAdmin',

    'controllers/OrganizationPanelController',
    'controllers/OrganizationAddModalController',
    'controllers/OrganizationEditModalController',
    'controllers/OrganizationEditHistoryModalController',
    'controllers/DeviceController',
    'controllers/CategoryDeviceAddModalController',
    'controllers/CategoryDeviceEditModalController',
    'controllers/CounterTypePanelController',
    'controllers/CounterTypeAddController',
    'controllers/CounterTypeEditController',
    'controllers/SettingsController',
    'controllers/SysAdminsController',
    'controllers/SysAdminEditModalController',
    'controllers/SysAdminDeleteModalController',
    'controllers/UsersController',
    'controllers/SysAdminAddModalController',
    'controllers/InternationalizationController',
    'controllers/agreement/AgreementController',
    'controllers/agreement/AgreementAddController',
    'services/OrganizationService', 'services/StatisticService',
    'services/UserService', 'services/AddressService',
    'services/DeviceService', 'services/DevicesService',
    'services/CounterTypeService',
    'services/AgreementService',
    'services/SettingsService',
    'services/UsersService',
    'services/RoleService',
    'services/ResponseObserver',
    'services/MeasuringEquipmentServiceAdmin',
    'directives/unique',
    'controllers/CommonController',
    'controllers/UnsuitabilityReasonController',
    'services/UnsuitabilityReasonService'
], function () {
});
