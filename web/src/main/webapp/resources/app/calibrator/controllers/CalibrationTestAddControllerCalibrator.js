/**
 * Created by Konyk on 11.08.2015.
 */
angular
    .module('employeeModule')
    .controller('CalibrationTestAddControllerCalibrator', ['$rootScope', '$translate', '$scope', '$modal', '$http', '$log',
        'CalibrationTestServiceCalibrator', '$location', 'Upload', '$timeout',
        function ($rootScope, $translate, $scope, $modal, $http, $log, calibrationTestServiceCalibrator, $location, Upload,  $timeout) {

            $scope.testId = $location.search().param;
            $scope.hasProtocol = $location.search().loadProtocol || false;

            $scope.fileLoaded = false;

            $scope.TestDataFormData = [{}, {}, {}, {}, {}, {}];

            /**
             * Resets Test form
             */
            $scope.resetTestForm = function () {
                $scope.$broadcast('show-errors-reset');
                $scope.TestForm = null;
                $scope.TestDataFormData = [{}, {}, {}, {}, {}, {}];
            };

            $scope.uploadBbiFile = function(testId) {
                var modalInstance =  $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/calibrator/views/modals/upload-bbiFile.html',
                    controller: 'UploadBbiFileController',
                    size: 'lg',
                    resolve: {
                        calibrationTest: function () {
                            return testId;
                        },
                        parseBbiFile: function() {
                            return $scope.parseBbiFile;
                        }
                    }
                });
            };

            function convectorStatus(consumptionStatus,flag) {
               if(flag) {
                   if (consumptionStatus === "IN_THE_AREA") {
                       consumptionStatus = "В зоні";
                   } else if (consumptionStatus === "NOT_IN_THE_AREA") {
                       consumptionStatus = "Не в зоні";
                   }
               } else{
                   if (consumptionStatus === "В зоні") {
                       consumptionStatus = "IN_THE_AREA";
                   } else if (consumptionStatus === "Не в зоні") {
                       consumptionStatus = "NOT_IN_THE_AREA";
                   }
               }
                return consumptionStatus;
            }

            function convectorTestResult(testResult, flag) {
                if (flag) {
                    if (testResult === "SUCCESS") {
                        testResult = "Успішний";
                    } else if (testResult === "FAILED") {
                        testResult = "Невдалий";
                    } else if(testResult === "RAW") {
                        testResult = "Необроблений";
                    }
                }else {
                    if (testResult=== "Успішний") {
                        testResult = "SUCCESS";
                    } else if (testResult === "Невдалий") {
                        testResult = "FAILED";
                    } else if(testResult === "Необроблений") {
                        testResult= "RAW";
                    }
                }
                return testResult;
            }

            function convectorForListData (listTestData,flag){
                 var list = new Array();
                 for (var i = 0; i < listTestData.length; i++) {
                     var dataTest = listTestData[i];
                     dataTest.consumptionStatus = convectorStatus(dataTest.consumptionStatus, flag);
                     dataTest.testResult = convectorTestResult(dataTest.testResult, flag);
                     list[i]=dataTest;
                 }
                 return list;
            }

            $scope.parseBbiFile = function (data) {
                $scope.fileLoaded = true;
                data.consumptionStatus = convectorStatus(data.consumptionStatus, true);
                data.testResult = convectorTestResult(data.testResult, true);
                data.listTestData = convectorForListData(data.listTestData,true)
                $scope.TestForm = data;
                for (var i = 0; i < $scope.statusData.length; i++) {
                    if ($scope.statusData[i].id === data.status) {
                        $scope.selectedStatus = {
                            label: $scope.statusData[i].label,
                            id: $scope.statusData[i].id
                        };
                    }
                }
                var date = $scope.TestForm.testDate;
                $scope.TestForm.testDate = moment(date).utcOffset(0).format("DD.MM.YYYY HH:mm");
                $scope.TestForm.testPhoto = "data:image/png;base64," + $scope.TestForm.testPhoto;
                $scope.TestDataFormData = data.listTestData;
            };

            $scope.showEditMainPhotoModal = function (id) {
                var modalInstance =  $modal.open({
                    animation: true,
                    templateUrl: 'resources/app/calibrator/views/modals/edit-main-photo-modal.html',
                    controller: 'EditPhotoController',
                    size: 'md',
                    resolve: {
                         photoId: function() {
                            return id;
                        },
                         parentScope: function() {
                            return $scope;
                        }
                    }
                });
            };

            $scope.setMainPhoto = function (data) {
                $scope.TestForm.testPhoto = data;
            };

            function getCalibrationTests() {
                calibrationTestServiceCalibrator
                    .getCalibrationTests()
                    .then(function (data) {
                        $scope.calibrationTests = data.calibrationTests;
                    })
            }

            function getProtocolTest(verificationID) {
                calibrationTestServiceCalibrator
                    .getTestProtocol(verificationID)
                    .then(function (data){
                        $scope.parseBbiFile(data);
                        $log.debug("inside");
                    } );
            }

            $scope.selectedStatus = {
                label: null
            }
            $scope.statusData = [
                {id: 'TEST_OK', label: null},
                {id: 'TEST_NOK', label: null}
            ];

            $scope.setStatus = function (status) {
                $scope.selectedStatus = {
                    label: status.label,
                    id: status.id
                };
            };

            $scope.setTypeDataLanguage = function () {
                var lang = $translate.use();
                if (lang === 'ukr') {
                    $scope.statusData[0].label = 'придатний';
                    $scope.statusData[1].label = 'непридатний';
                } else if (lang === 'eng') {
                    $scope.statusData[0].label = 'Tested OK';
                    $scope.statusData[1].label = 'Tested NOK';
                }
            };


            $scope.setTypeDataLanguage();

            if ( $scope.hasProtocol){
                getProtocolTest($scope.testId);
            }else{
                getCalibrationTests();

            }
            function retranslater() {
                //convectorTestResult($scope.TestForm,false);
                protocol = {
                    fileName:$scope.TestForm.fileName,
                    counterNumber:$scope.TestForm.counterNumber,
                    temperature:$scope.TestForm.temperature,
                    installmentNumber:$scope.TestForm.installmentNumber,
                    latitude:$scope.TestForm.latitude,
                    longitude:$scope.TestForm.longitude,
                    testResult:convectorTestResult($scope.TestForm.testResult,false),
                    status:$scope.selectedStatus.id,
                    listTestData:convectorForListData($scope.TestForm.listTestData,false)
                    }
            };



            $scope.closeForm = function(){
                window.history.back();
            }

            /**
             * update test from the form in database.
             */
                /*$scope.generalForms={testForm:$scope.TestForm, smallForm: $scope.TestDataFormData};
                  $log.debug($scope.generalForms);
                .updateCalibrationTest($scope.TestForm, $scope.testId)*/

            $scope.updateCalibrationTest = function () {
                        retranslater();
                        calibrationTestServiceCalibrator
                            .updateCalibrationTest(protocol,$scope.testId)
                            .then(function (status) {
                                if (status == 201) {
                                    $rootScope.onTableHandling();
                                }
                                if(status==200){
                                    $modal.open({
                                        animation: true,
                                        templateUrl: 'resources/app/calibrator/views/modals/calibration-test-edited-success.html',
                                        controller: function ($modalInstance) {
                                            this.ok = function () {
                                                $modalInstance.close();
                                                window.history.back();
                                            }
                                        },
                                        controllerAs: 'successController',
                                        size: 'md'
                                    });

                                }
                            });
            }

        }]);