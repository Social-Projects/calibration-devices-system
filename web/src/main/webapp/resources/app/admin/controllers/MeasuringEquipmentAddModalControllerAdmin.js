angular
    .module('adminModule')
    .controller(
    'MeasuringEquipmentAddModalControllerAdmin',
    [
        '$rootScope',
        '$scope',
        '$translate',
        '$modalInstance',
        '$filter',
        'MeasuringEquipmentServiceAdmin',
        'calibrationModule',
        function ($rootScope, $scope, $translate, $modalInstance, $filter,
                  measuringEquipmentServiceAdmin, calibrationModule) {

            /**
             * Initialize all variables
             */
            $scope.init = function () {

                $scope.addCalibrationModuleFormData = {};

                $scope.addCalibrationModuleFormData.organizationCode = '';
                $scope.addCalibrationModuleFormData.condDesignation = '';
                $scope.addCalibrationModuleFormData.serialNumber = '';
                $scope.addCalibrationModuleFormData.employeeFullName = '';
                $scope.addCalibrationModuleFormData.telephone = '';
                $scope.addCalibrationModuleFormData.workDate = '';
                $scope.addCalibrationModuleFormData.moduleType = '';
                $scope.addCalibrationModuleFormData.email = '';
                $scope.addCalibrationModuleFormData.calibrationType = '';

                $scope.calendar = {};
                $scope.calendar.isOpen = false;
                $scope.open = function ($event) {
                    $event.preventDefault();
                    $event.stopPropagation();
                    $scope.calendar.isOpen = true;
                };

                $scope.minDate = null; //$scope.minDate ? null : new Date();
                $scope.maxDate = new Date(2100, 5, 22);

                moment.locale('uk');
                $scope.dateOptions = {
                    formatYear: 'yyyy',
                    startingDay: 1,
                    showWeeks: 'false',
                };

                $scope.deviceTypeData = [
                    {id: 'WATER', label: $filter('translate')('WATER')},
                    {id: 'THERMAL', label: $filter('translate')('THERMAL')},
                    {id: 'ELECTRICAL', label: $filter('translate')('ELECTRICAL')},
                    {id: 'GASEOUS', label: $filter('translate')('GASEOUS')}
                ];

                $scope.moduleTypeData = [
                    {id: 'INSTALLATION_FIX', label: $filter('translate')('INSTALLATION_FIX')},
                    {id: 'INSTALLATION_PORT', label: $filter('translate')('INSTALLATION_PORT')}
                ];

                if (calibrationModule !== undefined) {
                    $scope.addCalibrationModuleFormData.deviceType = {
                        id: calibrationModule.deviceType,
                        label: $filter('translate')(calibrationModule.deviceType)
                    };

                    $scope.addCalibrationModuleFormData.moduleType = {
                        id: calibrationModule.moduleType,
                        label: $filter('translate')(calibrationModule.moduleType)
                    };
                    $scope.addCalibrationModuleFormData.moduleId = calibrationModule.moduleId;
                    $scope.addCalibrationModuleFormData.organizationCode = calibrationModule.organizationCode;
                    $scope.addCalibrationModuleFormData.condDesignation = calibrationModule.condDesignation;
                    $scope.addCalibrationModuleFormData.serialNumber = calibrationModule.serialNumber;
                    $scope.addCalibrationModuleFormData.employeeFullName = calibrationModule.employeeFullName;
                    $scope.addCalibrationModuleFormData.telephone = calibrationModule.telephone;
                    $scope.addCalibrationModuleFormData.workDate = calibrationModule.workDate;
                    $scope.addCalibrationModuleFormData.email = calibrationModule.email;
                    $scope.addCalibrationModuleFormData.calibrationType = calibrationModule.calibrationType;
                } else {
                    $scope.addCalibrationModuleFormData.deviceType = undefined;
                    $scope.addCalibrationModuleFormData.organizationCode = '';
                    $scope.addCalibrationModuleFormData.condDesignation = '';
                    $scope.addCalibrationModuleFormData.serialNumber = '';
                    $scope.addCalibrationModuleFormData.employeeFullName = '';
                    $scope.addCalibrationModuleFormData.telephone = '';
                    $scope.addCalibrationModuleFormData.workDate = '';
                    //$scope.addCalibrationModuleFormData.moduleType = '';
                    $scope.addCalibrationModuleFormData.moduleType = undefined;
                    $scope.addCalibrationModuleFormData.email = '';
                    $scope.addCalibrationModuleFormData.calibrationType = '';
                }
            };

            $scope.init();

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            /**
             * Resets form
             */
            $scope.resetAddCalibrationModuleForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.addCalibrationModuleForm.$setPristine();
                $scope.addCalibrationModuleForm.$setUntouched();
                $scope.addCalibrationModuleFormData = {};
            };

            /**
             * Closes the modal window
             */
            $rootScope.closeModal = function (close) {
                $scope.resetAddCalibrationModuleForm();
                if (close === true) {
                    $modalInstance.close();
                }
                $modalInstance.dismiss();
            };

            /**
             * Validates calibration module form before saving
             */
            $scope.onAddCalibrationModuleFormSubmit = function () {
                $scope.$broadcast('show-errors-check-validity');
                if ($scope.addCalibrationModuleForm.$valid) {
                    $scope.addCalibrationModuleFormData.deviceType = "THERMAL";//$scope.addCalibrationModuleFormData.deviceType.id;
                    $scope.addCalibrationModuleFormData.moduleType = "INSTALLATION_FIX";//$scope.addCalibrationModuleFormData.moduleType.id;
                    saveCalibrationModule();
                }
            };

            /**
             * Saves calibration module
             */
            function saveCalibrationModule() {
                //console.log($scope.addCalibrationModuleFormData);
                if (calibrationModule === undefined) {
                    var a = measuringEquipmentServiceAdmin.saveCalibrationModule($scope.addCalibrationModuleFormData)
                        .then(function (result) {
                            if (result == 201) {
                                $scope.closeModal(true);
                                //$scope.resetAddCalibrationModuleForm();
                                $rootScope.onTableHandling();
                            }
                        });
                } else {
                    measuringEquipmentServiceAdmin.editCalibrationModule($scope.addCalibrationModuleFormData, calibrationModule.moduleId)
                        .then(function (result) {
                                console.log("else");
                            if (result == 200) {
                                $scope.closeModal(true);
                                $scope.resetAddCalibrationModuleForm();
                                $rootScope.onTableHandling();
                            }
                        });
                }
            };

            $scope.clearDate = function () {
                calibrationModule.workDate = null;
            };


            $scope.CATEGORY_DEVICE_CODE = /^[\u0430-\u044f\u0456\u0457\u0454a-z\d]{13}$/;
            $scope.PHONE_REGEX = /^[1-9]\d{8}$/;
            $scope.EMAIL_REGEX = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
            $scope.FIRST_LAST_NAME_REGEX = /^([A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20})$/;
            $scope.MIDDLE_NAME_REGEX = /^[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}$/;
        }
    ]);