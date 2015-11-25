angular
    .module('adminModule')
    .controller(
    'MeasuringEquipmentDisableModalControllerAdmin',
    [
        '$rootScope',
        '$scope',
        '$modalInstance',
        'toaster',
        '$filter',
        'moduleId',
        'MeasuringEquipmentServiceAdmin',
        function ($rootScope, $scope, $modalInstance, toaster, $filter,
                  moduleId, MeasuringEquipmentServiceAdmin) {

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function() {
                $modalInstance.close();
            });

            /**
             * Closes the modal window
             */
            $rootScope.closeModal = function (close) {
                if (close === true) {
                    $modalInstance.close();
                }
                $modalInstance.dismiss();
            };

            $scope.disableCalibrationModule = function () {
                MeasuringEquipmentServiceAdmin.disableCalibrationModule(moduleId).then(function (result) {
                    if (result.status == 200) {
                        $scope.closeModal(true);
                    }
                });
            };

        }]);

