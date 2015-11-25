angular
    .module('employeeModule')
    .factory('CalibrationTestServiceCalibrator', function ($http) {
        return {
            getCalibrationTests: function (testId) {
                var url = 'calibrator/calibrationTestData/' + testId;
                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },

            getPage: function (currentPage, itemsPerPage, search, sortCriteria, sortOrder, id) {
                return getDataWithParams('calibrator/verifications/calibration-test/' + currentPage + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },
            saveCalibrationTest: function (formData, testId) {
                return $http.post("calibrator/calibrationTests/add/" + testId, formData)
                    .then(function (result) {
                        return result.status;
                    });
            },

            /*updateCalibrationTest: function (formData, testId) {
                return $http.post("calibrator/calibrationTests/updateProtocol/" + testId, formData)
                    .then(function (result) {
                        console.log("###############: "+result.status );
                        return result.status;
                    });
            },**/

            updateCalibrationTest: function (data,testId) {
                return $http.post("calibrator/calibrationTests/updateProtocol/" + testId, data)
                    .then(function (result) {
                        console.log("###############: "+result.status );
                        return result.status;
                    });
            },

            saveCalibrationTestData: function  (formdata, testId) {
              return $http.post("calibrator/calibrationTestData/addTestData/" + testId, formdata)
                  .then(function(result) {
                    return result.status;
                });
            },
            deleteCalibrationTest: function (calibrationTestId) {
                var url = 'calibrator/calibrationTests/delete/' + calibrationTestId;
                return $http.post(url)
                    .then(function (result) {
                        return result.status;
                    });
            },
            getEmptyTest: function (verificationId) {
                var url = 'calibrator/calibrationTests/createEmptyTest/' + verificationId;
                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },
            getTestProtocol: function (verificationId) {
                var url = 'calibrator/calibrationTests/getProtocol/' + verificationId;
                return $http.get(url)
                    .then(function (result) {
                        return result.data;
                    });
            },
            editCalibrationTest: function (formData, testId) {
                var url = 'calibrator/calibrationTests/edit/' + testId;
                return $http.post(url, formData)
                    .then(function (result) {
                        return result.status;
                    });
            },

            getCalibrationTestWithId: function (testId) {
                var url = 'calibrator/calibrationTests/getTest/' + testId;
                return $http.get(url).then(function (result) {
                    return result.data;
                });
            },

            parseBbiFile: function (fileName) {
                var dotIndex = fileName.lastIndexOf('.');
                var extension = fileName.substring(dotIndex + 1);
                fileName = fileName.substring(0, dotIndex);
                var url = 'calibrator/calibrationTestData/parseBbi/' + fileName + '/' + extension;
                return $http.get(url).then(function (result) {
                    return result;
                })
            }
        };

        function getData(url) {
            return $http.get('calibrator/' + url)
                .success(function (data) {
                    return data;
                })
                .error(function (err) {
                    return err;
                });
        }

        function getDataWithParams(url, params) {
            return $http.get(url, {
                params: params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }

        function sendData(url, data) {
            return $http.post('calibrator/' + url, data)
                .success(function (responseData) {
                    return responseData;
                })
                .error(function (err) {
                    return err;
                });
        }
    });
