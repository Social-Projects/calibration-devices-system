angular
    .module('employeeModule')
    .controller('NewVerificationsControllerVerificator', ['$scope', '$log', '$modal', '$location', 'CalibrationTestServiceCalibrator', 'VerificationServiceVerificator',
        '$rootScope', 'ngTableParams', '$filter', '$timeout', '$translate',
        function ($scope, $log, $modal, $location, calibrationTestServiceCalibrator,  verificationServiceVerificator, $rootScope, ngTableParams, $filter, $timeout,
                  $translate) {

            $scope.resultsCount = 0;

            /**
             * this function return true if is StateVerificatorEmployee
             */
            $scope.isStateVerificatorEmployee = function () {
                verificationServiceVerificator.getIfEmployeeStateVerificator().success(function(data){
                    $scope.isEmployee =  data;
                });

            };
            $scope.openAddTest = function (verificationID) {
                        $location.path('/calibrator/verifications/calibration-test-add/').search({
                            'param': verificationID,
                            'loadProtocol': 1
                        });
            };
            $scope.isStateVerificatorEmployee();


            $scope.clearAll = function () {
                $scope.selectedStatus.name = null;
                $scope.tableParams.filter({});
            }

            $scope.doSearch = function () {
                $scope.tableParams.reload();
            }

            $scope.selectedStatus = {
                name: null
            }

            $scope.statusData = [
                {id: 'SENT_TO_VERIFICATOR', label: null},
                {id: 'TEST_OK', label: null},
                {id: 'TEST_NOK', label: null}
            ];

            $scope.setTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {

                    $scope.statusData[0].label = 'Предявлено повірнику';
                    $scope.statusData[1].label = 'Перевірено придатний';
                    $scope.statusData[2].label = 'Перевірено непридатний';

                } else if (lang === 'eng') {

                    $scope.statusData[0].label = 'Sent to verificator';
                    $scope.statusData[1].label = 'Tested OK';
                    $scope.statusData[2].label = 'Tested NOK';

                }
            };

            $scope.setTypeDataLanguage();


            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10,
                sorting: {
                    date: 'desc'
                }
            }, {
                total: 0,
                filterDelay: 1500,
                getData: function ($defer, params) {

                    var sortCriteria = Object.keys(params.sorting())[0];
                    var sortOrder = params.sorting()[sortCriteria];

                    if ($scope.selectedStatus.name != null) {
                        params.filter().status = $scope.selectedStatus.name.id;
                    }

                    verificationServiceVerificator.getNewVerifications(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
                        .success(function (result) {
                            $scope.resultsCount = result.totalItems;
                            $defer.resolve(result.content);
                            params.total(result.totalItems);
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                }
            });

            $scope.checkFilters = function () {
                var obj = $scope.tableParams.filter();
                for (var i in obj) {
                    if (obj.hasOwnProperty(i) && obj[i]) {
                        return true;
                    }
                }
                return false;
            };


            $scope.markAsRead = function (id) {
                var dataToSend = {
                    verificationId: id,
                    readStatus: 'READ'
                };

                verificationServiceVerificator.markVerificationAsRead(dataToSend).success(function () {
                    $rootScope.$broadcast('verification-was-read');
                    $scope.tableParams.reload();
                });
            };

            $scope.openDetails = function (verifId, verifDate, verifReadStatus) {
                $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/verificator/views/modals/new-verification-details.html',
                    controller: 'DetailsModalControllerVerificator',
                    size: 'lg',
                    resolve: {
                        response: function () {
                            return verificationServiceVerificator.getNewVerificationDetails(verifId)
                                .success(function (verification) {
                                    verification.id = verifId;
                                    verification.initialDate = verifDate;
                                    if (verifReadStatus == 'UNREAD') {
                                        $scope.markAsRead(verifId);
                                    }
                                    return verification;
                                });
                        }
                    }
                });
            };

            $scope.testReview = function (verifId) {
                $log.debug(verifId);
                $modal.open({

                    animation: true,
                    templateUrl: 'resources/app/verificator/views/modals/testReview.html',
                    controller: 'CalibrationTestReviewControllerVerificator',
                    size: 'lg',
                    resolve: {
                        response: function () {
                            return verificationServiceVerificator.getCalibraionTestDetails(verifId)
                                .success(function (calibrationTest) {
                                    //calibrationTest.id = verifId;
                                    $log.debug('CalibrationTest');
                                    $log.debug(calibrationTest);
                                    return calibrationTest;
                                })
                                .error(function () {
                                    console.log('ERROR');
                                });
                        }
                    }
                });
            };

            $scope.idsOfVerifications = [];
            $scope.checkedItems = [];
            $scope.allIsEmpty = true;

            $scope.resolveVerificationId = function (id) {

                var index = $scope.idsOfVerifications.indexOf(id);
                if (index === -1) {
                    $scope.idsOfVerifications.push(id);
                    index = $scope.idsOfVerifications.indexOf(id);
                }

                if (!$scope.checkedItems[index]) {
                    $scope.idsOfVerifications.splice(index, 1, id);
                    $scope.checkedItems.splice(index, 1, true);
                } else {
                    $scope.idsOfVerifications.splice(index, 1);
                    $scope.checkedItems.splice(index, 1);
                }
                checkForEmpty();
            };

            $scope.openRejectTest = function () {
                if (!$scope.allIsEmpty) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: 'resources/app/verificator/views/modals/mailComment.html',
                        controller: 'TestRejectControllerVerificator',
                        size: 'md',

                    });
                    /**
                     * executes when modal closing
                     */
                    modalInstance.result.then(function (formData) {

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            organizationId: 1,
                            message: formData.message
                        };

                        verificationServiceVerificator
                            .rejectTestToCalibrator(dataToSend)
                            .success(function () {
                                $log.debug('success sending');
                                $scope.tableParams.reload();
                                $rootScope.$broadcast('verification-sent-to-calibrator');
                            });

                        $scope.idsOfVerifications = [];
                        $scope.checkedItems = [];

                    });
                } else {
                    $scope.isClicked = true;
                }
            };

            $scope.openSendingModal = function () {
                if (!$scope.allIsEmpty) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: 'resources/app/verificator/views/modals/verification-sending.html',
                        controller: 'SendingModalControllerVerificator',
                        size: 'md',
                        resolve: {
                            response: function () {
                                return verificationServiceVerificator.getProviders()
                                    .success(function (providers) {
                                        return providers;
                                    });
                            }
                        }
                    });

                    //executes when modal closing
                    modalInstance.result.then(function (formData) {

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            organizationId: formData.provider.id
                        };

                        $log.info(dataToSend);
                        verificationServiceVerificator
                            .sendVerificationsToProvider(dataToSend)
                            .success(function () {
                                $scope.tableParams.reload();
                                $rootScope.$broadcast('verification-sent-to-provider');
                            });

                        $scope.idsOfVerifications = [];
                        $scope.checkedItems = [];
                    });
                } else {
                    $scope.isClicked = true;
                }
            };


            //For NOT_OK!!!
            $scope.openSendingModalNotOK = function () {
                if (!$scope.allIsEmpty) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: 'resources/app/verificator/views/modals/verification-sending.html',
                        controller: 'SendingModalControllerVerificator',
                        size: 'md',
                        resolve: {
                            response: function () {
                                return verificationServiceVerificator.getProviders()
                                    .success(function (providers) {
                                        return providers;
                                    });
                            }
                        }
                    });


                    //executes when modal closing
                    modalInstance.result.then(function (formData) {

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            organizationId: formData.provider.id
                        };

                        $log.info(dataToSend);
                        verificationServiceVerificator
                            .sendVerificationNotOkStatus(dataToSend)
                            .sendEmployeeVerificator(dataToSend)
                            .success(function () {
                                $scope.tableParams.reload();
                                $rootScope.$broadcast('verification-sent-to-provider');
                            });

                        $scope.idsOfVerifications = [];
                        $scope.checkedItems = [];
                    });
                } else {
                    $scope.isClicked = true;
                }
            };

            /**
             * removing employee from chosen verification
             * @param verificationId
             */
            $scope.removeVerificatorEmployee = function (verificationId) {
                var dataToSend = {
                    idVerification: verificationId
                };
                $log.info(dataToSend);
                verificationServiceVerificator.cleanVerificatorEmployeeField(dataToSend)
                    .success(function () {
                        $scope.tableParams.reload();
                    });
            };

            /**
             * assigning new employee to verification
             * @param verificationId
             */
            $scope.addVerificatorEmployee = function (verificationId) {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/verificator/views/employee/assigning-verificatorEmployee.html',
                    controller: 'VerificatorEmployeeControllerVerificator',
                    size: 'md',
                    windowClass: 'xx-dialog',
                    resolve: {
                        verificatorEmployee: function () {
                            return verificationServiceVerificator.getVerificators()
                                .success(function (verificators) {
                                    return verificators;
                                }
                            );
                        }
                    }
                });
                /**
                 * executes when modal closing
                 */
                modalInstance.result.then(function (formData) {
                    idVerification = 0;
                    var dataToSend = {
                        idVerification: verificationId,
                        employeeCalibrator: formData.provider
                    };
                    $log.info(dataToSend);
                    verificationServiceVerificator
                        .sendEmployeeVerificator(dataToSend)
                        .success(function () {
                            $scope.tableParams.reload();
                        });
                });
            };


            var checkForEmpty = function () {
                $scope.allIsEmpty = $scope.idsOfVerifications.length === 0;
            };

            /**
             *  Date picker and formatter setup
             *
             */
            $scope.openState = {};
            $scope.openState.isOpen = false;

            $scope.open = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.openState.isOpen = true;
            };


            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false'
            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];
            $scope.initiateVerification = function () {

                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/provider/views/modals/initiate-verification.html',
                    controller: 'AddingVerificationsControllerProvider',
                    size: 'lg',

                });
            };

        }]);
