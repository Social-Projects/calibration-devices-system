angular
    .module('employeeModule')
    .controller('TaskSendingModalControllerCalibrator', ['$rootScope', '$scope', '$modal', '$modalInstance', 'VerificationPlanningTaskService', '$log',
        function ($rootScope, $scope, $modal, $modalInstance, verificationPlanningTaskService, $log) {

            $scope.calibrationTask = {};
            $scope.incorrectValue = false;

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function() {
                $modalInstance.close();
            });

            /**
             * Closes edit modal window.
             */
            $scope.closeModal = function () {
                console.log("close modal window");
                $modalInstance.close();
            };

            /**
             * Initializing the addInfo
             * */
            $scope.addInfo = {};

            /**
             * Toggle button (additional info) functionality
             * */
            $scope.showStatus = {
                opened: false
            };

            $scope.openAdditionalInformation = function () {
                if($scope.showStatus.opened === false){
                    $scope.showStatus.opened = true;
                } else {
                    $scope.showStatus.opened = false;
                }
            };

            /**
             *  Date picker and formatter setup
             *
             */
            $scope.firstCalendar = {};
            $scope.firstCalendar.isOpen = false;
            $scope.secondCalendar = {};
            $scope.secondCalendar.isOpen = false;
            $scope.thirdCalendar = {};
            $scope.thirdCalendar.isOpen = false;

            $scope.open1 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.firstCalendar.isOpen = true;
            };

            $scope.open2 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.secondCalendar.isOpen = true;
            };

            $scope.open3 = function ($event) {
                $event.preventDefault();
                $event.stopPropagation();
                $scope.thirdCalendar.isOpen = true;
            };

            moment.locale('uk');
            $scope.dateOptions = {
                formatYear: 'yyyy',
                startingDay: 1,
                showWeeks: 'false',

            };

            $scope.formats = ['dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
            $scope.format = $scope.formats[2];

            // Disable weekend selection
            $scope.disabled = function(date, mode) {
                return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
            };

            $scope.toggleMin = function() {
                $scope.minDate = $scope.minDate ? null : new Date();
            };

            $scope.toggleMin();
            $scope.maxDate = new Date(2100, 5, 22);


            $scope.clearDate1 = function () {
                $log.debug($scope.calibrationTask.taskDate);
                $scope.calibrationTask.taskDate = null;
            };

            $scope.clearDate2 = function () {
                $log.debug($scope.calibrationTask.dateOfVerif);
                $scope.calibrationTask.dateOfVerif = null;
            };

            $scope.clearDate3 = function () {
                $log.debug($scope.calibrationTask.noWaterToDate);
                $scope.calibrationTask.noWaterToDate = null;
            };

            $scope.resetTaskForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.calibrationTask = {};
                $scope.incorrectValue = false;
                $scope.calibrationTask.pickerDate = null;
                $scope.installationNumberValidation = null;
                $scope.floorValidation = null;
                $scope.counterNumberValidation = null;
                $scope.showSendingMessage = false;
                $scope.modulesSerialNumbers = {};
                $scope.checkPlaceAndStatus();
                $scope.checkPlace();
            };


            $scope.checkPlace = function(){
                try{
                    if ($scope.calibrationTask.place==null) {
                        return true;
                    } else if ($scope.calibrationTask.place == 'INSTALLATION_FIX') {
                        return false;
                    } else if ($scope.calibrationTask.place == 'INSTALLATION_FIX' && $scope.calibrationTask.counterStatus == 'not_removed'){
                        return false;
                    } else {
                        return true;
                    }
                }  catch (e) {
                    console.log("Got an error!",e);
                }
            }

            $scope.checkCounterStatus = function(){
                try{
                   if ($scope.calibrationTask.counterStatus == 'not_removed'){
                        return false;
                    } else {
                        return true;
                    }
                }  catch (e) {
                    console.log("Got an error!",e);
                }
            }


            $scope.checkPlaceAndStatus = function(){
                try{
                    if (($scope.calibrationTask.place==null) && ($scope.calibrationTask.counterStatus==null)) {
                        return true;
                    } else if ($scope.calibrationTask.place == 'INSTALLATION_FIX' && $scope.calibrationTask.counterStatus == 'not_removed') {
                        return false;
                    } else if ($scope.calibrationTask.place == 'INSTALLATION_PORT'){
                        $scope.calibrationTask.counterStatus = '';
                        return false;
                    } else {
                        return true;
                    }
                } catch (e) {
                    console.log("Got an error!",e);
                }

            }

            $scope.checkAll = function (caseForValidation) {
                switch (caseForValidation) {
                    case ('installationNumber'):
                        var installationNumber = $scope.calibrationTask.installationNumber;
                        if (/^[0-9]{5,20}$/.test(installationNumber)) {
                            validator('installationNumber', false);
                        } else {
                            validator('installationNumber', true);
                        }
                        break;
                    case ('entrance'):
                        var entrance = $scope.calibrationTask.entrance;
                        if (/^[0-9]{1,2}$/.test(entrance)) {
                            validator('entrance', false);
                        } else {
                            validator('entrance', true);
                        }
                        break;
                    case ('doorCode'):
                        var doorCode = $scope.calibrationTask.doorCode;
                        if (/^[0-9]{1,4}$/.test(doorCode)) {
                            validator('doorCode', false);
                        } else {
                            validator('doorCode', true);
                        }
                        break;
                    case ('floor'):
                        var floor = $scope.calibrationTask.floor;
                        if (floor == null) {

                        } else if (/^\d{1,2}$/.test(floor)) {
                            validator('floor', false);
                        } else {
                            validator('floor', true);
                        }
                        break;
                    case ('counterNumber'):
                        var counterNumber = $scope.calibrationTask.counterNumber;
                        if (/^[0-9]{5,20}$/.test(counterNumber)) {
                            validator('counterNumber', false);
                        } else {
                            validator('counterNumber', true);
                        }
                        break;
                    case ('time'):
                        var time = $scope.calibrationTask.time;
                        if (/^[0-1]{1}[0-9]{1}(\.)[0-9]{2}(\-)[0-2]{1}[0-9]{1}(\.)[0-9]{2}$/.test(time)) {
                            validator('time', false);
                        } else {
                            validator('time', true);
                        }
                        break;
                }

            }

            function validator(caseForValidation, isValid) {
                switch (caseForValidation) {
                    case ('installationNumber'):
                        $scope.installationNumberValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('entrance'):
                        $scope.entranceValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('doorCode'):
                        $scope.doorCodeValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('floor'):
                        $scope.floorValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('counterNumber'):
                        $scope.counterNumberValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;
                    case ('time'):
                        $scope.timeValidation = {
                            isValid: isValid,
                            css: isValid ? 'has-error' : 'has-success'
                        }
                        break;

                }
            }

            $scope.modulesSerialNumbers = {};

            $scope.receiveModuleNumbers = function(){
                console.log($scope.calibrationTask.place + " " + $scope.calibrationTask.taskDate);
                var place = $scope.calibrationTask.place;
                var taskDate = $scope.calibrationTask.taskDate;
                var applicationFiled = $scope.calibrationTask.applicationFiled;
                verificationPlanningTaskService.getModuls(place, taskDate, applicationFiled)
                    .then(function (result) {
                        $log.debug('result ', result);
                        $scope.modulesSerialNumbers = result.data;
                        $log.debug('my array ', $scope.modulesSerialNumbers);
                    }, function (result) {
                        $log.debug('error fetching data:', result);
                    });
            }


            $scope.showSendingMessage = false;
            $scope.task = {};
            $scope.save = function (){
                if ($rootScope.emptyStatus == true) {
                    $scope.showSendingMessage = true;
                    console.log("Ви не вибрали заявок для надсилання")
                } else {
                    console.log($rootScope.verifIds);
                    $scope.calibrationTask = {
                        taskDate: $scope.calibrationTask.taskDate,
                        serialNumber: $scope.calibrationTask.installationNumber,
                        verificationsId: $rootScope.verifIds
                    };
                    console.log($scope.calibrationTask);
                    verificationPlanningTaskService.saveTask($scope.calibrationTask).then(
                        function (data) {
                            if (data == 200) {
                                $scope.closeModal();
                            } else {
                             $scope.incorrectValue = true;
                             console.log($scope.incorrectValue);
                            }
                        });

                }

            }

        }]);
