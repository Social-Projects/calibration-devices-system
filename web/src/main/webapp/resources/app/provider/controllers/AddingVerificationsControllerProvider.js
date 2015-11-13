angular.module('employeeModule').controller('AddingVerificationsControllerProvider', ['$scope', '$state', '$http', '$log',
    'AddressServiceProvider', 'VerificationServiceProvider', '$stateParams',
    '$rootScope', '$location', '$window', '$modalInstance', 'DataReceivingServiceProvider', '$filter',

    function ($scope, $state, $http, $log, addressServiceProvider, verificationServiceProvider, $stateParams, $rootScope, $location, $window, $modalInstance, dataReceivingService) {
        $scope.isShownForm = true;
        $scope.isCalibrator = -1;
        $scope.calibratorDefined = false;

        /**
         * Receives all regex for input fields
         */
        $scope.FIRST_LAST_NAME_REGEX = /^([A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457']{1,20}\u002d[A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457']{1,20})$/;
        $scope.MIDDLE_NAME_REGEX = /^[A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457']{1,20}$/;
        $scope.FLAT_REGEX = /^([1-9][0-9]{0,3}|0)$/;
        $scope.BUILDING_REGEX = /^[1-9][0-9]{0,3}([A-Za-z]|[\u0410-\u042f\u0407\u0406\u0430-\u044f\u0456\u0457])?$/;
        $scope.PHONE_REGEX = /^[1-9]\d{8}$/;
        $scope.PHONE_REGEX_SECOND = /^[1-9]\d{8}$/;
        $scope.EMAIL_REGEX = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;

        $scope.checkboxModel = false;

        $scope.regions = [];
        $scope.devices = [];
        $scope.localities = [];
        $scope.providers = [];
        $scope.calibrators = [];
        $scope.streetsTypes = [];

        $scope.selectedData = {};
        $scope.selectedData.selectedStreetType = "";

        $scope.applicationCodes = [];
        $scope.codes = [];
        $scope.selectedData.selectedCount = '1';
        $scope.deviceCountOptions = [1, 2, 3, 4];

        /**
         * Closes modal window on browser's back/forward button click.
         */
        $rootScope.$on('$locationChangeStart', function () {
            $modalInstance.close();
        });

        addressServiceProvider.checkOrganizationType().success(function (response) {
            $scope.isCalibrator = response;
        });

        function arrayObjectIndexOf(myArray, searchTerm, property) {
            for (var i = 0, len = myArray.length; i < len; i++) {
                if (myArray[i][property] === searchTerm) return i;
            }
            return 0;
        }

        /**
         * Receives all possible regions.
         */
        $scope.receiveRegions = function () {
            addressServiceProvider.findAllRegions()
                .success(function (regions) {
                    $scope.regions = regions;
                    $scope.selectedData.region = "";
                    $scope.selectedData.district = "";
                    $scope.selectedData.locality = "";
                    $scope.selectedStreet = "";
                });
        };

        $scope.receiveRegions();

        /**
         * Receives all possible devices.
         */
        addressServiceProvider.findAllDevices()
            .success(function (devices) {
                $scope.devices = devices;
                $log.debug('device');
                $log.debug(devices);
                $scope.selectedData.selectedDevice = [];  //$scope.devices[0];
                $log.debug($scope.selectedData.selectedCount);
            });
        /**
         * Receives all possible districts.
         * On-select handler in region input form element.
         */
        $scope.receiveDistricts = function (selectedRegion) {
            $scope.districts = [];
            addressServiceProvider.findDistrictsByRegionId(selectedRegion.id)
                .success(function (districts) {
                    $scope.districts = districts;
                    $scope.selectedData.district = "";
                    $scope.selectedData.locality = "";
                    $scope.selectedStreet = "";
                });
        };
        /**
         * Receives all possible localities.
         * On-select handler in district input form element.
         */
        $scope.receiveLocalitiesAndProviders = function (selectedDistrict) {
            addressServiceProvider.findLocalitiesByDistrictId(selectedDistrict.id)
                .success(function (localities) {
                    $scope.localities = localities;
                    $scope.selectedData.locality = "";
                    $scope.selectedStreet = "";
                });
        };

        $scope.receiveCalibrators = function (deviceType) {
            addressServiceProvider.findCalibratorsForProviderByType(deviceType.deviceType)
                .success(function (calibrators) {
                    $scope.calibrators = calibrators;
                    $scope.selectedData.selectedCalibrator = "";
                    if ($scope.isCalibrator > 0) {
                        var index = arrayObjectIndexOf($scope.calibrators, $scope.isCalibrator, "id");
                        $scope.selectedData.selectedCalibrator = $scope.calibrators[index];
                    }
                });
        };

        addressServiceProvider.findStreetsTypes().success(function (streetsTypes) {
            $scope.streetsTypes = streetsTypes;
            $scope.selectedData.selectedStreetType = "";
            $log.debug("$scope.streetsTypes");
            $log.debug($scope.streetsTypes);
        });

        /**
         * Receives all possible streets.
         * On-select handler in locality input form element
         */
        $scope.receiveStreets = function (selectedLocality, selectedDistrict) {
            if (!$scope.blockSearchFunctions) {
                $scope.streets = [];
                addressServiceProvider.findStreetsByLocalityId(selectedLocality.id)
                    .success(function (streets) {
                        $scope.streets = streets;
                        $scope.selectedStreet = "";
                    });
                $scope.indexes = [];
                addressServiceProvider.findMailIndexByLocality(selectedLocality.designation, selectedDistrict.id)
                    .success(function (indexes) {
                        $scope.indexes = indexes;
                        $scope.selectedData.index = indexes[0];
                    });
            }
        };
        /**
         * Receives all possible buildings.
         * On-select handler in street input form element.
         */
        $scope.receiveBuildings = function (selectedStreet) {
            $scope.buildings = [];
            addressServiceProvider.findBuildingsByStreetId(selectedStreet.id)
                .success(function (buildings) {
                    $scope.buildings = buildings;
                });
        };

        /**
         * Sends data to the server where Verification entity will be created.
         * On-click handler in send button.
         */
        $scope.isMailValid = true;
        $scope.sendApplicationData = function () {
            $scope.$broadcast('show-errors-check-validity');
            if ($scope.clientForm.$valid) {
                $scope.formData.region = $scope.selectedData.region.designation;
                $scope.formData.district = $scope.selectedData.district.designation;
                $scope.formData.locality = $scope.selectedData.locality.designation;
                $scope.formData.street = $scope.selectedStreet.designation || $scope.selectedStreet;
                $scope.formData.building = $scope.selectedBuilding.designation || $scope.selectedBuilding;
                $scope.formData.calibratorId = $scope.selectedData.selectedCalibrator.id;
                $scope.formData.deviceId = $scope.selectedData.selectedDevice.id;

                for (var i = 0; i < $scope.selectedData.selectedCount; i++) {
                    verificationServiceProvider.sendInitiatedVerification($scope.formData)
                        .success(function (applicationCode) {
                            if($scope.applicationCodes === undefined)
                            {
                                $scope.applicationCodes = [];
                            }
                            $scope.applicationCodes.push(applicationCode);
                        });
                }
                verificationServiceProvider.checkMailIsExist($scope.formData)
                    .success(function (isMailValid) {
                        $scope.isMailValid = isMailValid;
                    });

                //hide form because application status is shown
                $scope.isShownForm = false;
            }
        };

        $scope.closeAlert = function () {
            $modalInstance.close();
        };

        /**
         * Resets form
         */
        $scope.resetApplicationForm = function () {

            $scope.$broadcast('show-errors-reset');

            $scope.clientForm.$setPristine();
            $scope.clientForm.$setUntouched();

            $scope.formData = null;

            $scope.selectedValues.firstSelectedDevice = undefined;
            $scope.selectedValues.secondSelectedDevice = undefined;

            $scope.selectedValues.firstDeviceCount = undefined;
            $scope.selectedValues.secondDeviceCount = undefined;

            $scope.selectedValues.selectedRegion = undefined;
            $scope.selectedValues.selectedDistrict = undefined;
            $scope.selectedValues.selectedLocality = undefined;
            $scope.selectedValues.selectedStreetType = undefined;
            $scope.selectedValues.selectedStreet = "";
            $scope.selectedValues.selectedBuilding = "";
            $scope.selectedValues.selectedIndex = undefined;
            $scope.defaultValue.privateHouse = false;
            $scope.firstDeviceProviders = [];
            $scope.secondDeviceProviders = [];
            $scope.selectedValues.firstSelectedProvider = undefined;
            $scope.selectedValues.secondSelectedProvider = undefined;

            $log.debug("$scope.resetApplicationForm");
        };

        /**
         * Fill application sending page from verification when there is verification ID in $stateParams
         * @param ID - Id of verification to fill from
         */
        /*$rootScope.verifIDforTempl = "54720638-4dac-46c2-a95b-12130ce791af";*/
        $scope.createNew = function () {
            if ($rootScope.verifIDforTempl) {
                dataReceivingService.getVerificationById($rootScope.verifIDforTempl).then(function (verification) {

                    $scope.verification = verification;
                    $scope.formData = {};
                    $scope.formData.lastName = $scope.verification.data.lastName;
                    $scope.formData.firstName = $scope.verification.data.firstName;
                    $scope.formData.middleName = $scope.verification.data.middleName;
                    $scope.formData.email = $scope.verification.data.email;
                    $scope.formData.phone = $scope.verification.data.phone;
                    $scope.formData.flat = $scope.verification.data.flat;
                    $scope.formData.comment = $scope.verification.data.comment;
                    $scope.defaultValue = {};
                    $scope.defaultValue.privateHouse = $scope.verification.data.flat == 0;


                    $scope.formData.region = $scope.verification.data.region;
                    $scope.formData.district = $scope.verification.data.district;
                    $scope.formData.locality = $scope.verification.data.locality;
                    $scope.formData.street = $scope.verification.data.street;
                    $scope.formData.building = $scope.verification.data.building;



     //               $scope.blockSearchFunctions = true;
                 /*   dataReceivingService.findAllRegions().then(function (respRegions) {
                        $scope.regions = respRegions.data;
                        var index = arrayObjectIndexOf($scope.regions, $scope.verification.data.region, "designation");
                        $scope.selectedValues.selectedRegion = $scope.regions[index];

                        dataReceivingService.findDistrictsByRegionId($scope.selectedValues.selectedRegion.id)
                            .then(function (districts) {
                                $scope.districts = districts.data;
                                var index = arrayObjectIndexOf($scope.districts, $scope.verification.data.district, "designation");
                                $scope.selectedValues.selectedDistrict = $scope.districts[index];

                                dataReceivingService.findLocalitiesByDistrictId($scope.selectedValues.selectedDistrict.id)
                                    .then(function (localities) {
                                        $scope.localities = localities.data;
                                        var index = arrayObjectIndexOf($scope.localities, $scope.verification.data.locality, "designation");
                                        $scope.selectedValues.selectedLocality = $scope.localities[index];

                                        dataReceivingService.findStreetsByLocalityId($scope.selectedValues.selectedLocality.id)
                                            .then(function (streets) {
                                                $scope.streets = streets.data;
                                                var index = arrayObjectIndexOf($scope.streets, $scope.verification.data.street, "designation");
                                                $scope.selectedValues.selectedStreet = $scope.streets[index];

                                                dataReceivingService.findBuildingsByStreetId($scope.selectedValues.selectedStreet.id)
                                                    .then(function (buildings) {
                                                        $scope.buildings = buildings.data;
                                                        var index = arrayObjectIndexOf($scope.buildings, $scope.verification.data.building, "designation");
                                                        $scope.selectedValues.selectedBuilding = $scope.buildings[index].designation;

                                                        dataReceivingService.findMailIndexByLocality($scope.selectedValues.selectedLocality.designation, $scope.selectedValues.selectedDistrict.id)
                                                            .success(function (indexes) {
                                                                $scope.indexes = indexes;
                                                                $scope.selectedValues.selectedIndex = $scope.indexes[0];
                                                                $scope.blockSearchFunctions = false;
                                                            });

                                                    });
                                            });
                                    });
                            });
                    });*///
                });

            }
        }
    }
]);
