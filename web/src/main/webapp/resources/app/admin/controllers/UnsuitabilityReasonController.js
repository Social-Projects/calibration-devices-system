/**
 * Created by Sonka on 23.11.2015.
 */
angular
    .module('adminModule')
    .controller(
    'UnsuitabilityReasonController',
    [
        '$rootScope',
        '$scope',
        '$modal',
        '$http',
        'DevicesService',
        'ngTableParams',
        '$translate',
        '$timeout',
        '$filter',
        'toaster',
        function ($rootScope, $scope, $modal, $http, UnsuitabilityReasonService, ngTableParams, $translate, $timeout, $filter, toaster) {
            $scope.totalItems = 0;
            $scope.currentPage = 1;
            $scope.itemsPerPage = 5;
            $scope.pageContent = [];

            //for devices kinds
            $scope.selectedDeviceType = {
                name: null
            }

            $scope.deviceTypeData = [
                               {
                    id: 'WATER',
                    label: $filter('translate')('WATER')
                },
                {
                    id: 'THERMAL',
                    label: $filter('translate')('THERMAL')
                }
            ];

            /**
             * Localization of multiselect for type of devices category
             */
            $scope.setTypeDataLanguage = function () {
                $scope.deviceTypeData[0].label = $filter('translate')('WATER');
                $scope.deviceTypeData[1].label = $filter('translate')('THERMAL');
            };

            $scope.setTypeDataLanguage();

            $scope.clearAll = function () {
                $scope.selectedDeviceType.name = null;
                $scope.tableParams.filter({});
            };

            $scope.doSearch = function () {
                $scope.tableParams.reload();
            };

            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {
                    id: 'desc'
                }
            }, {
                total: 0,
                filterDelay: 10000,
                getData: function ($defer, params) {

                    var sortCriteria = Object.keys(params.sorting())[0];
                    var sortOrder = params.sorting()[sortCriteria];

                    if ($scope.selectedDeviceType.name != null) {
                        params.filter().deviceType = $scope.selectedDeviceType.name.id;
                    }
                    else {
                        params.filter().deviceType = null; //case when the filter is cleared with a button on the select
                    }

                    UnsuitabilityReasonService.getPage(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
                        .success(function (result) {
                            $scope.resultsCount = result.totalItems;
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                }
            });
            /**
             * Updates the table with device.
             */
            $rootScope.onTableHandling = function () {
                $scope.tableParams.reload();
            };

            $rootScope.onTableHandling();

            $scope.isFilter = function () {
                var obj = $scope.tableParams.filter();
                for (var i in obj) {
                    if (obj.hasOwnProperty(i) && obj[i]) {
                        return true;
                    }
                }
                return false;
            };
            /**
             * Opens modal window for adding new category of counters.
             */
            $scope.openAddCategoryCounterModal = function() {
                var addCategoryCounter = $modal.open({
                    animation : true,
                    controller : 'CategoryDeviceAddModalController',
                    templateUrl : 'resources/app/admin/views/modals/device-category-add-modal.html',
                    size: 'md'
                });
                /**
                 * executes when modal closing
                 */
                addCategoryCounter.result.then(function () {
                    toaster.pop('success',$filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_ADDED_CATEGORY'));
                });
            };

            /**
             * Opens modal window for editing category of counter.
             */
            $scope.openEditCategoryCounterModal = function(
                deviceId) {
                $rootScope.categoryId = deviceId;
                UnsuitabilityReasonService.getDeviceCategoryById(
                    $rootScope.categoryId).then(
                    function(data) {
                        $rootScope.countersCategory = data;
                        console.log($rootScope.countersCategory);

                        var deviceDTOModal = $modal
                            .open({
                                animation : true,
                                controller : 'CategoryDeviceEditModalController',
                                templateUrl : 'resources/app/admin/views/modals/device-category-edit-modal.html',
                                size: 'md'
                            });
                        deviceDTOModal.result.then(function () {
                            toaster.pop('info', $filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_EDITED_CATEGORY'));
                        });
                    });

            };

            /**
             * Remove devices category by id
             * @param id
             */
            $scope.deleteDeviceCategory = function (id) {
                $rootScope.deviceCategoryId = id;
                console.log($rootScope.deviceCategoryId);
                devicesService.deleteDeviceCategory(id).then(function (status) {
                    if (status == 409){
                        toaster.pop('info', $filter('translate')('INFORMATION'), $filter('translate')('ERROR_DELETED_CATEGORY'));
                    } else {
                        toaster.pop('info', $filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_DELETED_CATEGORY'));
                    }
                });
                $timeout(function() {
                    console.log('delete with timeout');
                    $rootScope.onTableHandling();
                }, 700);
            };

        }]);
