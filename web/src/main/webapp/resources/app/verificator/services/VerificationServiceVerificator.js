angular
    .module('employeeModule')
    .factory('VerificationServiceVerificator', ['$http', '$log', function ($http, $log) {

        return {
        	getArchiveVerifications: function (currentPage, itemsPerPage, search, sortCriteria, sortOrder) {
            	return getDataWithParams('verificator/verifications/archive/' + currentPage + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },
            getNewVerifications: function (currentPage, itemsPerPage, search, sortCriteria, sortOrder) {
                return getDataWithParams('verificator/verifications/new/' + currentPage + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },
            getNewVerificationDetails: function (verificationId) {
                return getData('verifications/new/' + verificationId);
            },
            getArchivalVerificationDetails: function (verificationId) {
                return getData('verifications/archive/' + verificationId);
            },
            getProviders: function (url) {
                return getData('verifications/new/providers');
            },
            getCalibrators: function (url) {
                return getData('verifications/new/calibrators');
            },
            getVerificators: function (url) {
                return updateData('new/verificatorEmployees');
            },
            sendEmployeeVerificator: function (data) {
                return updateData('assign/verificatorEmployee', data);
            },
            cleanVerificatorEmployeeField:function (data) {
                return employeeUpdateData('remove/verificatorEmployee', data);
            },
            getVerificators: function (url) {
                return getData('verifications/new/verificatorEmployees');
            },
            rejectTestToCalibrator: function (data) {
                return updateData('new/reject', data);
            },
            sendVerificationsToProvider: function (data) {
                return updateData('new/update', data);
            },
            sendVerificationNotOkStatus: function (data) {
                return updateData('new/notOk', data);
            },
            sendInitiatedVerification:function(form){
                return sendData("send",form);
            },
            getVerificatorsCorrespondingProvider:function(url){ // ??
                return getData("applications/verificators");
            },
            getLocalitiesCorrespondingProvider:function(url){
                return getData("applications/localities");
            },
            getStreetsCorrespondingLocality:function(selectedLocality){
                return getData("applications/streets/" + selectedLocality.id);
            },
            getBuildingsCorrespondingStreet:function(selectedBuilding){
                    return getData("applications/buildings/" + selectedBuilding.id);
                },
            getCountOfNewVerifications: function(url) {
            	return getData('verifications/new/count/verificator');
            },
            markVerificationAsRead : function(data) {
            	return updateData('new/read', data);
            },
            searchNewVerifications : function(data) {
            	return sendDataWithParams('new/search', data);
            },
            getCalibraionTestDetails: function (verificationId){
            	return getData('verifications/show/' + verificationId);
            },
            getIfEmployeeStateVerificator: function(url) {
                return getData('verifications/verificator/role');
            },

        };
       
        function getData(url) {

            $log.info(url);

            return $http.get('verificator/' + url)
                .success(function (data) {
                    return data;
                })
                .error(function (err) {
                    return err;
                });
        }

        function getEmployeeData(url) {
            return $http.get('verificator/admin/users/' + url)
                .success(function (data) {
                    return data;
                })
                .error(function (err) {
                    return err;
                });
        }
        
        function getDataWithParams(url, params) {
            return $http.get(url, {
                params : params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }

        function employeeUpdateData(url, data) {
            return $http.put('verificator/admin/users/' + url, data)
                .success(function (responseData) {
                    return responseData;
                })
                .error(function (err) {
                    return err;
                });
        }

        function getDataWithParams(url, params) {
            return $http.get(url, {
                params : params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }

        function updateData(url, data) {
            return $http.put('verificator/verifications/' + url, data)
                .success(function (responseData) {
                    return responseData;
                })
                .error(function (err) {
                    return err;
                });
        }

        function sendData(url, data) {
            return $http.post('verificator/applications/' + url, data)
                .success(function (responseData) {
                    return responseData;
                })
                .error(function (err) {
                    return err;
                });
        }
    }]);
