angular
    .module('employeeModule')
    .controller('NewVerificationsControllerProvider', ['$scope', '$log', '$modal', 'VerificationServiceProvider', '$rootScope', 'ngTableParams', '$filter', '$timeout', '$translate',
        function ($scope, $log, $modal, verificationServiceProvider, $rootScope, ngTableParams, $filter, $timeout, $translate) {

            $scope.resultsCount = 0;

            /**
             * this function return true if is StateVerificatorEmployee
             */
            $scope.isVerificatorEmployee = function () {
                verificationServiceProvider.getIfEmployeeProvider().success(function (data) {
                    $scope.isEmployee = data;
                });

            };

            $scope.isVerificatorEmployee();

            $scope.clearAll = function () {
                $scope.selectedStatus.name = null;
                $scope.tableParams.filter({});
                $scope.clearDate(); // sets 'all time' timerange
            };

            $scope.clearDate = function () {
                //daterangepicker doesn't support null dates
                $scope.myDatePicker.pickerDate = $scope.defaultDate;
                //setting corresponding filters with 'all time' range
                $scope.tableParams.filter()['date'] = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                $scope.tableParams.filter()['endDate'] = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");

            };

            $scope.doSearch = function () {
                $scope.tableParams.reload();
            };

            $scope.selectedStatus = {
                name: null
            };

            $scope.statusData = [
                {id: 'SENT', label: null},
                {id: 'ACCEPTED', label: null}
            ];

            $scope.setTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {
                    $scope.statusData[0].label = 'Надійшла';
                    $scope.statusData[1].label = 'Прийнята';

                } else if (lang === 'eng') {
                    $scope.statusData[0].label = 'Sent';
                    $scope.statusData[1].label = 'Accepted';

                }
            };

            $scope.setTypeDataLanguage();


            $scope.myDatePicker = {};
            $scope.myDatePicker.pickerDate = null;
            $scope.defaultDate = null;

            $scope.initDatePicker = function (date) {
                /**
                 *  Date picker and formatter setup
                 *
                 */


                /*TODO: i18n*/
                $scope.myDatePicker.pickerDate = {
                    startDate: (date ? moment(date, "YYYY-MM-DD") : moment()),
                    //earliest day of  all the verifications available in table
                    //we should reformat it here, because backend currently gives date in format "YYYY-MM-DD"
                    endDate: moment() // current day
                };

                if ($scope.defaultDate == null) {
                    //copy of original daterange
                    $scope.defaultDate = angular.copy($scope.myDatePicker.pickerDate);
                }
                moment.locale('uk'); //setting locale for momentjs library (to get monday as first day of the week in ranges)
                $scope.opts = {
                    format: 'DD-MM-YYYY',
                    showDropdowns: true,
                    locale: {
                        firstDay: 1,
                        fromLabel: 'Від',
                        toLabel: 'До',
                        applyLabel: "Прийняти",
                        cancelLabel: "Зачинити",
                        customRangeLabel: "Обрати самостійно"
                    },
                    ranges: {
                        'Сьогодні': [moment(), moment()],
                        'Вчора': [moment().subtract(1, 'day'), moment().subtract(1, 'day')],
                        'Цього тижня': [moment().startOf('week'), moment().endOf('week')],
                        'Цього місяця': [moment().startOf('month'), moment().endOf('month')],
                        'Попереднього місяця': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                        'За увесь час': [$scope.defaultDate.startDate, $scope.defaultDate.endDate]
                    },
                    eventHandlers: {}
                };
            };


            $scope.showPicker = function ($event) {
                angular.element("#datepickerfield").trigger("click");
            };


            $scope.isDateDefault = function () {
                var pickerDate = $scope.myDatePicker.pickerDate;

                if (pickerDate == null || $scope.defaultDate == null) { //moment when page is just loaded
                    return true;
                }
                if (pickerDate.startDate.isSame($scope.defaultDate.startDate, 'day') //compare by day
                    && pickerDate.endDate.isSame($scope.defaultDate.endDate, 'day')) {
                    return true;
                }
                return false;
            };


            $scope.checkFilters = function () {
                if ($scope.tableParams == null) return false; //table not yet initialized
                var obj = $scope.tableParams.filter();
                for (var i in obj) {
                    if (obj.hasOwnProperty(i) && obj[i]) {
                        if (i == 'date' || i == 'endDate')
                            continue; //check for these filters is in another function
                        return true;
                    }
                }
                return false;
            };

            $scope.checkDateFilters = function () {
                if ($scope.tableParams == null) return false; //table not yet initialized
                var obj = $scope.tableParams.filter();
                if ($scope.isDateDefault())
                    return false;
                else if (!moment(obj.date).isSame($scope.defaultDate.startDate)
                    || !moment(obj.endDate).isSame($scope.defaultDate.endDate)) {
                    //filters are string,
                    // so we are temporarily convertin them to momentjs objects
                    return true;
                }
                return false;
            };

            verificationServiceProvider.getNewVerificationEarliestDate().success(function (date) {
                    //first we will try to receive date period
                    // to populate ng-table filter
                    // I did this to reduce reloading and flickering of the table
                    $scope.initDatePicker(date);
                    $scope.tableParams = new ngTableParams({
                        page: 1,
                        count: 10,
                        sorting: {
                            date: 'desc'
                        }
                    }, {
                        total: 0,
                        filterDelay: 1000,
                        getData: function ($defer, params) {

                            var sortCriteria = Object.keys(params.sorting())[0];
                            var sortOrder = params.sorting()[sortCriteria];

                            if ($scope.selectedStatus.name != null) {
                                params.filter().status = $scope.selectedStatus.name.id;
                            }
                            else {
                                params.filter().status = null; //case when the filter is cleared with a button on the select
                            }

                            params.filter().date = $scope.myDatePicker.pickerDate.startDate.format("YYYY-MM-DD");
                            params.filter().endDate = $scope.myDatePicker.pickerDate.endDate.format("YYYY-MM-DD");

                            verificationServiceProvider.getNewVerifications(params.page(), params.count(), params.filter(), sortCriteria, sortOrder)
                                .success(function (result) {

                                    $scope.resultsCount = result.totalItems;
                                    $defer.resolve(result.content);
                                    params.total(result.totalItems);
                                    $scope.allVerifications = result.content;
                                }, function (result) {
                                    $log.debug('error fetching data:', result);
                                });
                        }
                    });
                }
            );


            $scope.markAsRead = function (id) {
                var dataToSend = {
                    verificationId: id,
                    readStatus: 'READ'
                };

                verificationServiceProvider.markVerificationAsRead(dataToSend).success(function () {
                    $rootScope.$broadcast('verification-was-read');
                    $scope.tableParams.reload();
                });
            };


            /**
             * open modal
             */
            $scope.openDetails = function (verifId, verifDate, verifReadStatus, verifStatus) {

                $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/provider/views/modals/new-verification-details.html',
                    controller: 'DetailsModalControllerProvider',
                    size: 'lg',
                    resolve: {
                        response: function () {
                            return verificationServiceProvider.getNewVerificationDetails(verifId)
                                .success(function (verification) {
                                    $rootScope.verificationID = verifId;
                                    verification.id = $rootScope.verificationID;
                                    verification.initialDate = verifDate;
                                    verification.status = verifStatus;
                                    if (verifReadStatus == 'UNREAD') {
                                        $scope.markAsRead(verifId);
                                    }
                                    return verification;
                                });
                        }
                    }
                });
            };

            $scope.removeProviderEmployee = function (verifId) {
                var dataToSend = {
                    idVerification: verifId
                };
                $log.info(dataToSend);
                verificationServiceProvider.cleanProviderEmployeeField(dataToSend)
                    .success(function () {
                        $scope.tableParams.reload();
                    });
            };

            /*function that determines if the tooltip is shown on disable checkbox*/
            $scope.filterCells = function (employee) {
                return employee ? 'none' : 'mouseenter';
            };

            $scope.addProviderEmployee = function (verifId, providerEmployee) {
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/provider/views/modals/adding-providerEmployee.html',
                    controller: 'ProviderEmployeeControllerProvider',
                    size: 'md',
                    windowClass: 'xx-dialog',
                    resolve: {
                        providerEmploy: function () {
                            return verificationServiceProvider.getProviders()
                                .success(function (providers) {
                                    return providers;
                                }
                            );
                        }
                    }
                });
                /**
                 * executes when modal closing
                 */
                modalInstance.result.then(function (formData) {
                    var idVerification = 0;
                    var dataToSend = {
                        idVerification: verifId,
                        employeeProvider: formData.provider
                    };
                    $log.info(dataToSend);
                    verificationServiceProvider
                        .sendEmployeeProvider(dataToSend)
                        .success(function () {
                            $scope.tableParams.reload();
                        });
                });
            };

            $scope.idsOfVerifications = [];
            $scope.checkedItems = [];
            $scope.allIsEmpty = true;
            $scope.idsOfCalibrators = null;

            $scope.checkProviderEmployee = function () {
                var resalt = false;
                angular.forEach($scope.allVerifications, function (verification) {
                    if (verification.providerEmployee) {
                        resalt = true;
                    }
                });
                return resalt;
            }
            var getAllSelected = function () {
                var checkedItems = $scope.allVerifications.filter(function (verification) {
                    return verification.selected;
                });

                return checkedItems.length === $scope.allVerifications.length;
            }

            var setAllSelected = function (value) {
                angular.forEach($scope.allVerifications, function (verification) {
                    verification.selected = value;
                    if (verification.providerEmployee) {
                        $scope.resolveVerificationId(verification.id);
                    }
                });
            }

            $scope.allSelected = function (value) {
                if (value !== undefined) {
                    return setAllSelected(value);
                } else {
                    return getAllSelected();
                }
            }

            /**
             * push verification id to array
             */

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

            /**
             * open modal
             */
            $scope.openSendingModal = function () {
                if (!$scope.allIsEmpty) {
                    var modalInstance = $modal.open({
                        animation: true,
                        templateUrl: 'resources/app/provider/views/modals/verification-sending.html',
                        controller: 'SendingModalControllerProvider',
                        size: 'md',
                        resolve: {
                            response: function () {
                                return verificationServiceProvider.getCalibrators()
                                    .success(function (calibrators) {

                                        return calibrators;
                                    }
                                );
                            }
                        }
                    });

                    /**
                     * executes when modal closing
                     */
                    modalInstance.result.then(function (formData) {

                        var dataToSend = {
                            idsOfVerifications: $scope.idsOfVerifications,
                            organizationId: formData.calibrator.id
                        };


                        verificationServiceProvider
                            .sendVerificationsToCalibrator(dataToSend)
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

            /**
             * check if idsOfVerifications array is empty
             */
            var checkForEmpty = function () {
                $scope.allIsEmpty = $scope.idsOfVerifications.length === 0;
            };


            /**
             * Modal window used to explain the reason of verification rejection
             */
            $scope.openMailModal = function (ID) {
                $log.debug('ID');
                $log.debug(ID);
                var modalInstance = $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/provider/views/modals/mailComment.html',
                    controller: 'MailSendingModalControllerProvider',
                    size: 'md',

                });

                /**
                 * executes when modal closing
                 */
                modalInstance.result.then(function (formData) {

                    var messageToSend = {
                        verifID: ID,
                        msg: formData.message
                    };

                    var dataToSend = {
                        verificationId: ID,
                        status: 'REJECTED'
                    };
                    verificationServiceProvider.rejectVerification(dataToSend).success(function () {
                        verificationServiceProvider.sendMail(messageToSend)
                            .success(function (responseVal) {
                                $scope.tableParams.reload();
                            });
                    });
                });
            };

            $scope.$on('verification_rejected', function (event, args) {

                $scope.openMailModal(args.verifID);
            });

            $scope.initiateVerification = function () {

                $rootScope.verifIDforTempl = $scope.idsOfVerifications[0];
                var modalInstance = $modal.open({
                    animation: true,
                    backdrop: 'static',
                    templateUrl: 'resources/app/provider/views/modals/initiate-verification.html',
                    controller: 'AddingVerificationsControllerProvider',
                    size: 'lg'
                });
            };

        }]);

