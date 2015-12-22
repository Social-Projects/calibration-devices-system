angular
    .module('employeeModule')
    .controller('EditEmployeeController', ['$rootScope', '$scope', '$modalInstance', '$log', '$modal',
        '$timeout', '$state', '$http', 'UserService', 'toaster', '$filter', 'success-editing',

        function ($rootScope, $scope, $modalInstance, $log, $modal, $timeout, $state, $http, userService, toaster, $filter) {
            var organizationTypeProvider = false;
            var organizationTypeCalibrator = false;
            var organizationTypeVerificator = false;
            var employeeData = {};

            /**
             * Closes modal window on browser's back/forward button click.
             */
            $rootScope.$on('$locationChangeStart', function () {
                $modalInstance.close();
            });

            $scope.$on('info_about_editUser', function (event, args) {

                for (var i = 0; i < args.roles.length; i++) {
                    if (args.roles[i] === 'PROVIDER_EMPLOYEE') {
                        $scope.selectedEmployee.push('PROVIDER_EMPLOYEE');
                        organizationTypeProvider = true;
                    }
                    if (args.roles[i] === 'CALIBRATOR_EMPLOYEE') {
                        $scope.selectedEmployee.push('CALIBRATOR_EMPLOYEE');
                        organizationTypeCalibrator = true;
                    }
                    if (args.roles[i] === 'STATE_VERIFICATOR_EMPLOYEE') {
                        $scope.selectedEmployee.push('STATE_VERIFICATOR_EMPLOYEE');
                        organizationTypeVerificator = true;
                    }
                }


                if (args.isAvaliable == true) {
                    $scope.showRestore = false;
                } else {
                    $scope.showRestore = true;
                }


            });

            userService.isAdmin()
                .success(function (response) {
                    var includeCheckBox = false;
                    var thereIsAdmin = 0;
                    var roles = response + '';
                    var role = roles.split(',');
                    for (var i = 0; i < role.length; i++) {
                        if (role[i] === 'PROVIDER_ADMIN' || role[i] === 'CALIBRATOR_ADMIN' || role[i] === 'STATE_VERIFICATOR_ADMIN')
                            thereIsAdmin++;
                    }
                    if (thereIsAdmin === 0) {
                        $scope.accessLable = true;
                    } else {
                        $scope.verificator = true;
                    }
                    if (thereIsAdmin === 1) {
                        if (role[0] === 'PROVIDER_ADMIN')
                            organizationTypeProvider = true;
                        if (role[0] === 'CALIBRATOR_ADMIN')
                            organizationTypeCalibrator = true;
                        if (role[0] === 'STATE_VERIFICATOR_ADMIN')
                            organizationTypeVerificator = true;
                    }
                    if (thereIsAdmin > 1) {
                        $scope.showListOfOrganization = true;
                        for (var i = 0; i < role.length; i++) {
                            if ((role[0] === 'PROVIDER_ADMIN' && role[1] === 'CALIBRATOR_ADMIN') ||
                                (role[0] === 'CALIBRATOR_ADMIN' && role[1] === 'PROVIDER_ADMIN'))
                                $scope.showListOfOrganizationChosenOne = true;
                            if ((role[0] === 'STATE_VERIFICATOR_ADMIN' && role[1] === 'CALIBRATOR_ADMIN') ||
                                (role[0] === 'CALIBRATOR_ADMIN' && role[1] === 'STATE_VERIFICATOR_ADMIN'))
                                $scope.showListOfOrganizationChosenTwo = true;
                        }
                    }
                });

            /**
             * Choose role of employee
             * @param selectedEmployee
             */
            $scope.choose = function (selectedEmployee) {
                organizationTypeProvider = false;
                organizationTypeCalibrator = false;
                organizationTypeVerificator = false;
                employee = selectedEmployee + '';
                var resaultEmployee = employee.split(',');
                for (var i = 0; i < resaultEmployee.length; i++) {
                    if (resaultEmployee[i] === 'PROVIDER_EMPLOYEE') {
                        organizationTypeProvider = true;
                    }
                    if (resaultEmployee[i] === 'CALIBRATOR_EMPLOYEE') {
                        organizationTypeCalibrator = true;
                    }
                    if (resaultEmployee[i] === 'STATE_VERIFICATOR_EMPLOYEE') {
                        organizationTypeVerificator = true
                    }
                }
            };

            $scope.regions = null;
            $scope.districts = [];
            $scope.localities = [];
            $scope.streets = [];
            $scope.buildings = [];
            $scope.employeeFormData = {};
            $scope.selectedEmployee = [];

            /**
             * Change password
             */
            $scope.changePassword = function () {
                $scope.user.password = 'generate';
                $scope.generationMessage = true;
            };


            /**
             * Check passwords for equivalent
             */

            $scope.checkPasswords = function () {
                var first = $scope.employeeFormData.password;
                var second = $scope.employeeFormData.rePassword;
                $log.info(first);
                $log.info(second);
                var isValid = false;
                if (first != second) {
                    isValid = true;
                }
                $scope.passwordValidation = {
                    isValid: isValid,
                    css: isValid ? 'has-error' : 'has-success'
                }
            };


            function arrayObjectIndexOf(myArray, searchTerm, property) {
                for (var i = 0, len = myArray.length; i < len; i++) {
                    if (myArray[i][property] === searchTerm) return i;
                }
                var elem = {
                    id: length,
                    designation: searchTerm
                };
                myArray.push(elem);
                return (myArray.length - 1);
            }

            /**
             * Refactor data
             */
            function retranslater() {
                employeeData = {
                    firstName: $scope.user.firstName,
                    lastName: $scope.user.lastName,
                    middleName: $scope.user.middleName,
                    phone: $scope.user.phone,
                    secondPhone: $scope.user.secondPhone,
                    email: $scope.user.email,
                    username: $scope.user.username,
                    password: $scope.user.password,

                    userRoles: [],
                    isAvaliable: true

                };


                if (organizationTypeProvider === true) {
                    employeeData.userRoles.push('PROVIDER_EMPLOYEE');
                }
                if (organizationTypeCalibrator === true) {
                    employeeData.userRoles.push('CALIBRATOR_EMPLOYEE');
                }
                if (organizationTypeVerificator === true) {
                    employeeData.userRoles.push('STATE_VERIFICATOR_EMPLOYEE');
                }


            }

            /*
             Fire employee
             */
            $scope.fireEmployee = function (action) {
                if (action === 'fire') {
                    updateEmployee();
                    $scope.showRestore = true;
                    employeeData.isAvaliable = false;

                } else {
                    $scope.showRestore = false;
                }


            };


            $scope.onEmployeeFormSubmit = function () {

                $scope.$broadcast('show-errors-check-validity');
                if ($scope.checkboxModel == false){
                	$scope.user.secondPhone = null;
                }
                retranslater();
                updateEmployee();
                $scope.incorrectValue = true;


            };

            /**
             * Update new employee in database.
             */
            function updateEmployee() {
                userService.updateUser(
                    employeeData).then(
                    function (data) {
                        if (data.status == 201) {
                            $rootScope.$broadcast('new-employee-added');
                            $scope.closeModal();
                            toaster.pop('success', $filter('translate')('INFORMATION'),
                                $filter('translate')('SUCCESSFUL_EDIT_EMPLOYEE'));
                        } else {
                            toaster.pop('error', $filter('translate')('INFORMATION'),
                                $filter('translate')('ERROR_EDIT_EMPLOYEE'));
                        }
                    });
            }

            $scope.closeWindow = function () {
                $modalInstance.close();
            };

            /* Closes the modal window
             */
            $rootScope.closeModal = function () {
                $modalInstance.close();
            };
            $scope.FIRST_LAST_NAME_REGEX = /^([A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}\u002d{1}[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}|[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20})$/;
            $scope.MIDDLE_NAME_REGEX = /^[A-Z\u0410-\u042f\u0407\u0406\u0404']{1}[a-z\u0430-\u044f\u0456\u0457\u0454']{1,20}$/;
            $scope.PNOHE_REGEX_MY = /^[1-9]\d{8}$/;
            $scope.PHONE_REGEX = /^[1-9]\d{8}$/;
            $scope.EMAIL_REGEX = /^[-a-z0-9~!$%^&*_=+}{\'?]+(\.[-a-z0-9~!$%^&*_=+}{\'?]+)*@([a-z0-9_][-a-z0-9_]*(\.[-a-z0-9_]+)*\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|[a-z][a-z])|([0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}))(:[0-9]{1,5})?$/i;
//   $log.info(employeeFormData);

        }]);
