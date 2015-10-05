angular
    .module('employeeModule')
    .controller('VerificationPlanningTaskController', ['$scope', '$log',
        '$modal', 'VerificationPlanningTaskService',
        '$rootScope', 'ngTableParams', '$timeout', '$filter', '$window', '$location', '$translate',
        function ($scope, $log, $modal, verificationPlanningTaskService, $rootScope, ngTableParams, $timeout, $filter, $window, $location, $translate) {

            $scope.resultsCount = 0;

            $scope.tableParams = new ngTableParams({
                page: 1,
                count: 10
            }, {
                total: 0,
                filterDelay: 1500,
                getData: function ($defer, params) {
                    verificationPlanningTaskService.getVerificationsByCalibratorEmplAndTaskStatus(params.page(), params.count())
                        .then(function (result) {
                            if (result.status==200){
                                $log.debug('result ', result);
                            }
                        }, function (result) {
                            $log.debug('error fetching data:', result);
                        });
                }

            });

            $scope.openTask = function(verificationId){
                $rootScope.verifId = verificationId;

                $scope.$modalInstance  = $modal.open({
                    animation: true,
                    controller: 'TaskControllerCalibrator',
                    templateUrl: '/resources/app/calibrator/views/modals/eddTaskModal.html'
                });
            };

        }]);


