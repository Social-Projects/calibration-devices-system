/**
 * Created by Sonka on 23.11.2015.
 */
angular
    .module('adminModule')
    .factory('UnsuitabilityReasonService', function ($http) {
        return {
            getPage: function (pageNumber, itemsPerPage, search, sortCriteria, sortOrder) {
                return getDataWithParams('admin/unsuitability-reasons/' + pageNumber + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },
            saveUnsuitabilityReason: function (formData) {
                return $http.post("admin/unsuitability-reasons/add", formData)
                    .then(function (result) {
                        return result.status;
                    });
            },
            deleteUnsuitabilityReason: function (id) {
                var url = 'admin/unsuitability-reasons/delete/' + id;
                return $http.delete(url)
                    .then(function (result) {
                        return result.status;
                    })
            },
            getCounterTypes: function () {
                return getData('admin/unsuitability-reasons/counters');
            }
        };

        function getDataWithParams(url, params) {
            return $http.get(url, {
                params: params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }

        function getData(url) {
            return $http.get('' + url).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }
    });

