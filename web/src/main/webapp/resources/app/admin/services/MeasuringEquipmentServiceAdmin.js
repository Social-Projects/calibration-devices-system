angular
    .module('adminModule')
    .factory('MeasuringEquipmentServiceAdmin', function ($http) {
        return {
            getPage: function (pageNumber, itemsPerPage, search, sortCriteria, sortOrder) {
                return getDataWithParams(pageNumber + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },

            saveCalibrationModule: function (formData) {
                return sendData('add', formData);
            },
            getCalibrationModuleById: function (id) {
                return getData("get/" + id);
            },
            editCalibrationModule: function (formData, id) {
                return sendData('edit/' + id, formData);
            },
            getEarliestCalibrationModuleDate: function() {
                return getData('earliest_date');
            },
            disableCalibrationModule: function(id) {
                return getData("disable/" + id);
            },
            enableCalibrationModule: function(id) {
                return getData("enable/" + id);
            }
        };

        function sendData(url, data) {
            return $http.post('admin/calibration-module/' + url, data)
                .then(function (result) {
                    return result.status;
                });
        }

        function getData(url) {
            return $http.get('admin/calibration-module/' + url).success(function (result) {
                return result;
            }).error(function (err) {
                return err;
            });
        }

        function getDataWithParams(url, params) {
            return $http.get('admin/calibration-module/' + url, {
                params: params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }
    });