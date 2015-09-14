angular
    .module('welcomeModule')
    .controller('FeedbackController', ['$scope', '$modalInstance', '$log',
        function ($scope, $modalInstance, $log) {
            $scope.formData = {};
            $scope.complete = false;

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };

            $scope.sendingStarted = false;

            $scope.submit = function () {

                $scope.$broadcast('show-errors-check-validity');
                if ($scope.mailSendingForm.$valid) {
                    $scope.sendingStarted = false;
                    $modalInstance.close($scope.formData, $scope.sendingStarted);

                }
            }

            $scope.FIRST_LAST_NAME_REGEX = /^([A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20})$/;
            $scope.EMAIL_REGEX = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/;

        }]);
