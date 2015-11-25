angular
    .module('welcomeModule')
    .controller('ApplicationSendingController', ['$scope', '$q', '$state', '$http', '$log'
        , '$stateParams', '$window', '$rootScope', '$location', '$modal', '$filter', 'DataReceivingService', 'DataSendingService', 'toaster',
        function ($scope, $q, $state, $http, $log, $stateParams, $window, $rootScope, $location, $modal, $filter, dataReceivingService, dataSendingService, toaster) {
            $scope.isShownForm = true;
            $scope.blockSearchFunctions = false;
            $scope.isSecondDevice = false;
            $scope.appProgress = false;
            $scope.deviceShowError = false;
            $scope.isProvidersExist = true;
            $scope.checkboxModel = false;
            $scope.responseSuccess = false;
            $scope.showSendingAlert = false;
            $scope.values = [1, 2, 3, 4, 5, 6];

            $scope.regions = [];
            $scope.devices = [];
            $scope.streetsTypes = [];
            $scope.providers = [];
            $scope.firstDeviceProviders = [];
            $scope.secondDeviceProviders = [];

            $scope.selectedValues = {};
            $scope.selectedValues.selectedStreetType = undefined;
            $scope.selectedValues.secondDeviceCount = undefined;
            $scope.selectedValues.firstSelectedProvider = undefined;
            $scope.selectedValues.secondSelectedProvider = undefined;

            $scope.firstAplicationCodes = [];
            $scope.secondAplicationCodes = [];
            $scope.firstDeviceComment = "";
            $scope.secondDeviceComment = "";

            /**
             * Open application sending page and pass verification ID for auto filling it from verification
             * @param ID - Id of verification to fill from
             */
            $scope.createNew = function (ID) {
                $location.path('/application-sending/' + ID);
            };

            /**
             * Receives all regex for input fields
             *
             */
            $scope.STREET_REGEX = /^[a-z\u0430-\u044f\u0456\u0457]{1,20}\s([A-Z\u0410-\u042f\u0407\u0406][a-z\u0430-\u044f\u0456\u0457\u0454]{1,20}\u002d[A-Z\u0410-\u042f\u0407\u0406\u0454][a-z\u0430-\u044f\u0456\u0457\u0454]{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0454][a-z\u0430-\u044f\u0456\u0457\u0454]{1,20})$/;
            $scope.FIRST_LAST_NAME_REGEX = /^([A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}\u002d[A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457\u0454']{1,20})$/;
            $scope.MIDDLE_NAME_REGEX = /^[A-Z\u0410-\u042f\u0407\u0406\u0404'][a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}$/;
            $scope.FLAT_REGEX = /^([1-9][0-9]{0,3}|0)$/;
            $scope.BUILDING_REGEX = /^[1-9][0-9]{0,3}([A-Za-z]|[\u0410-\u042f\u0407\u0406\u0430-\u044f\u0456\u0457])?$/;
            $scope.PHONE_REGEX = /^[1-9]\d{8}$/;
            $scope.PHONE_REGEX_SECOND = /^[1-9]\d{8}$/;
            $scope.EMAIL_REGEX = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;

            /**
             * Selected values from select will be temprorily saved here.
             * We should create model object to avoid issues with scope inheritance
             * https://github.com/angular/angular.js/wiki/Understanding-Scopes
             */
            function arrayObjectIndexOf(myArray, searchTerm, property) {
                for (var i = 0, len = myArray.length; i < len; i++) {
                    if (myArray[i][property] === searchTerm) {
                        return i;
                    }
                }
                var elem = {
                    id: length,
                    designation: searchTerm
                };
                myArray.push(elem);
                return (myArray.length - 1);
            }

            /**
             * Fill application sending page from verification when there is verification ID in $stateParams
             * @param ID - Id of verification to fill from
             */
            if ($stateParams.verificationId) {
                dataReceivingService.getVerificationById($stateParams.verificationId).then(function (verification) {

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

                    $scope.blockSearchFunctions = true;
                    dataReceivingService.findAllRegions().then(function (respRegions) {
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
                    });
                });

            }

            /**
             * Receives all possible regions.
             */
            $scope.receiveRegions = function () {
                dataReceivingService.findAllRegions()
                    .success(function (regions) {
                        $scope.regions = regions;
                        $scope.selectedValues.selectedRegion = undefined; //for ui-selects
                        $scope.selectedValues.selectedDistrict = undefined;
                        $scope.selectedValues.selectedLocality = undefined;
                        $scope.selectedValues.selectedStreet = ""; //for bootstrap typeahead (ui.typeahead)
                    }
                );
            };

            if (!$stateParams.verificationId) {
                $scope.receiveRegions();
            }

            /**
             * Receives all possible districts.
             * On-select handler in region input form element.
             */
            $scope.receiveDistricts = function (selectedRegion) {
                if (!$scope.blockSearchFunctions) {
                    $scope.districts = [];
                    dataReceivingService.findDistrictsByRegionId(selectedRegion.id)
                        .success(function (districts) {
                            $scope.districts = districts;
                            $scope.selectedValues.selectedDistrict = undefined;
                            $scope.selectedValues.selectedLocality = undefined;
                            $scope.selectedValues.selectedStreet = "";
                        }
                    );
                }
            };

            /**
             * Receives all possible localities.
             * On-select handler in district input form element.
             */
            $scope.receiveLocalitiesAndProviders = function (selectedDistrict) {
                if (!$scope.blockSearchFunctions) {
                    $scope.localities = [];
                    dataReceivingService.findLocalitiesByDistrictId(selectedDistrict.id)
                        .success(function (localities) {
                            $scope.localities = localities;
                            $scope.selectedValues.selectedLocality = undefined;
                            $scope.selectedValues.selectedIndex = undefined;
                            $scope.selectedValues.selectedStreet = "";
                        }
                    );
                }
            };
            /**
             * Receives all providers in selected locality by device type
             */
            $scope.getProvidersByLocalityAndDeviceType = function (selectedLocality, selectedDeviceType, deviceGroup) {
                if (selectedLocality !== undefined && selectedDeviceType !== undefined) {
                    if (deviceGroup === 'firstDeviceGroup') {
                        dataReceivingService.findProvidersByLocalityAndDeviceType(selectedLocality.id, selectedDeviceType)
                            .success(function (providers) {
                                if (providers.length > 0) {
                                    $scope.firstDeviceProviders = providers;
                                    $scope.selectedValues.firstSelectedProvider = providers[0];
                                } else {
                                    $scope.firstDeviceProviders = [];
                                    $scope.selectedValues.firstSelectedProvider = undefined;
                                }
                            }
                        );
                    } else {
                        dataReceivingService.findProvidersByLocalityAndDeviceType(selectedLocality.id, selectedDeviceType)
                            .success(function (providers) {
                                if (providers.length > 0) {
                                    $scope.secondDeviceProviders = providers;
                                    $scope.selectedValues.secondSelectedProvider = providers[0];
                                } else {
                                    $scope.secondDeviceProviders = [];
                                    $scope.selectedValues.secondSelectedProvider = undefined;
                                }
                            }
                        );
                    }
                }
            };


            /**
             * Receives all possible streets.
             * On-select handler in locality input form element
             */
            dataReceivingService.findStreetsTypes()
                .success(function (streetsTypes) {
                    $scope.streetsTypes = streetsTypes;
                    $scope.selectedValues.selectedStreetType = undefined;
                }
            );

            $scope.receiveStreets = function (selectedLocality, selectedDistrict) {
                if (!$scope.blockSearchFunctions) {
                    $scope.streets = [];
                    dataReceivingService.findStreetsByLocalityId(selectedLocality.id)
                        .success(function (streets) {
                            $scope.streets = streets;
                            $scope.selectedValues.selectedStreet = undefined;
                        }
                    );

                    $scope.indexes = [];
                    dataReceivingService.findMailIndexByLocality(selectedLocality.designation, selectedDistrict.id)
                        .success(function (indexes) {
                            $scope.indexes = indexes;
                            if (indexes.length > 0) {
                                $scope.indexes = indexes;
                                $scope.selectedValues.selectedIndex = indexes[0];
                            }
                        }
                    );

                    dataReceivingService.findProvidersByLocality(selectedLocality.id)
                        .success(function (providers) {
                            $scope.isProvidersExist = providers.length > 0;
                        }
                    );
                }
            };

            /**
             * Receives all possible buildings.
             * On-select handler in street input form element.
             */
            $scope.receiveBuildings = function (selectedStreet) {
                if (!$scope.blockSearchFunctions) {
                    $scope.buildings = [];
                    dataReceivingService.findBuildingsByStreetId(selectedStreet.id)
                        .success(function (buildings) {
                            $scope.buildings = buildings;
                        }
                    );
                }
            };

            /**
             * Receives all possible devices.
             */
            dataReceivingService.findAllDevices()
                .success(function (devices) {
                    $scope.devices = devices;
                    $scope.selectedValues.firstSelectedDevice = undefined;
                    $scope.selectedValues.secondSelectedDevice = undefined;
                }
            );

            /**
             * Receives all possible device Types.
             */
            dataReceivingService.findAllDeviceTypes()
                .success(function (deviceTypes) {
                        $scope.deviceTypes = deviceTypes;
                        $scope.selectedValues.firstSelectedDeviceType = undefined;
                        $scope.selectedValues.secondSelectedDeviceType = undefined;
                }
            );

            $scope.selectDevice = function() {

                angular.forEach($scope.devices, function(value){
                    if(value.deviceType === $scope.selectedValues.firstSelectedDeviceType){
                        $scope.selectedValues.firstSelectedDevice = value;
                    }
                });

            };

            /**
             *  Error verification of device block
             */
            $scope.deviceErrorCheck = function () {
                /**
                 * Check first device selection group
                 */
                if ($scope.selectedValues.firstDeviceCount !== undefined) {
                    $scope.clientForm.firstDeviceCount.$invalid = false;
                }

                /**
                 * Check second device selection group
                 */
                if (($scope.selectedValues.secondDeviceCount !== undefined)) {
                    $scope.clientForm.secondDeviceCount.$invalid = false;
                }



                /**
                 * Check street selection group
                 */
                if (($scope.selectedValues.selectedStreetType !== undefined)) {
                    $scope.clientForm.streetType.$invalid = false;
                }

            };

            $scope.changeFlat = function () {
                $scope.$watch('formData', function (formData) {
                    if (formData) {
                        formData.flat = 0;
                    }
                });
            };

            /**
             * Sends data to the server where Verification entity will be created.
             * On-click handler in send button.
             */
            $scope.sendApplicationData = function () {
                $scope.codes = [];

                $scope.selectDevice();
                $scope.deviceErrorCheck();

                $scope.$broadcast('show-errors-check-validity');
                if ($scope.clientForm.$valid) {
                    $scope.isShownForm = false;
                    $scope.appProgress = true;

                    $scope.formData.region = $scope.selectedValues.selectedRegion.designation;
                    $scope.formData.district = $scope.selectedValues.selectedDistrict.designation;
                    $scope.formData.locality = $scope.selectedValues.selectedLocality.designation;
                    $scope.formData.street = $scope.selectedValues.selectedStreet.designation || $scope.selectedValues.selectedStreet;
                    $scope.formData.building = $scope.selectedValues.selectedBuilding.designation || $scope.selectedValues.selectedBuilding;

                    for (var i = 0; i < $scope.selectedValues.firstDeviceCount; i++) {
                        $scope.formData.deviceId = $scope.selectedValues.firstSelectedDevice.id;
                        $scope.formData.providerId = $scope.selectedValues.firstSelectedProvider.id;
                        $scope.formData.comment = $scope.firstDeviceComment;

                        $scope.firstAplicationCodes.push(dataSendingService.sendApplication($scope.formData))
                    }
                    // todo sending second device verification  bug
                    $q.all($scope.firstAplicationCodes).then(function (values) {
                        for (var i = 0; i < ($scope.selectedValues.firstDeviceCount); i++) {
                            $scope.codes.push(values[i].data);
                        }
                        for (var j = 0; j < $scope.selectedValues.secondDeviceCount; j++) {
                            $scope.formData.deviceId = $scope.selectedValues.secondSelectedDevice.id;
                            $scope.formData.providerId = $scope.selectedValues.secondSelectedProvider.id;
                            $scope.formData.comment = $scope.secondDeviceComment;
                            $scope.secondAplicationCodes.push(dataSendingService.sendApplication($scope.formData))
                        }

                        $q.all($scope.secondAplicationCodes).then(function (values) {
                            for (var i = 0; i < $scope.selectedValues.secondDeviceCount; i++) {
                                $scope.codes.push(values[i].data);
                            }
                            $scope.appProgress = false;
                        });
                    });
                    $log.debug(" $scope.codeslength");
                    $log.debug($scope.codes.length);
                }
            };
            $scope.closeAlert = function () {
                $location.path('/resources/app/welcome/views/start.html');
            };

            /**
             * Modal window used to send questions to administration
             */
            $scope.feedbackModalNoProvider = function () {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/welcome/views/modals/feedBackWindow.html',
                    controller: 'FeedbackController',
                    size: 'md'
                });

                /**
                 * executes when modal closing
                 */
                $scope.pop = function () {
                    toaster.pop('success', $filter('translate')('INFORMATION'), $filter('translate')('SUCCESSFUL_SENDING'));
                };

                modalInstance.result.then(function (formData) {
                    var messageToSend = {
                        verifID: $filter('translate')('NOTFOUND_TRANSLATION'),
                        msg: formData.message,
                        name: formData.firstName,
                        surname: formData.lastName,
                        email: formData.email
                    };

                    $scope.showSendingAlert = true;
                    dataSendingService.sendMailNoProvider(messageToSend)
                        .success(function () {
                            $scope.responseSuccess = true;
                            $scope.showSendingAlert = false;
                        });
                    $scope.pop();
                });

            };

            $scope.closeAlertW = function () {
                $scope.responseSuccess = false;
                $scope.showSendingAlert = false;
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

            $scope.$watch('selectedValues.selectedRegion', function () {
                $scope.selectedValues.selectedDistrict = undefined;
                $scope.selectedValues.selectedLocality = undefined;
                $scope.selectedValues.selectedIndex = undefined;
                $scope.selectedValues.selectedStreetType = undefined;
                $scope.selectedValues.selectedStreet = "";
            });

            $scope.$watch('selectedValues.selectedDistrict', function () {
                $scope.selectedValues.selectedLocality = undefined;
                $scope.selectedValues.selectedIndex = undefined;
                $scope.selectedValues.selectedStreetType = undefined;
                $scope.selectedValues.selectedStreet = "";
            });

            $scope.$watch('selectedValues.selectedLocality', function () {
                $scope.selectedValues.selectedIndex = undefined;
                $scope.selectedValues.selectedStreetType = undefined;
                $scope.selectedValues.selectedStreet = "";
            });

        }
    ]
);


