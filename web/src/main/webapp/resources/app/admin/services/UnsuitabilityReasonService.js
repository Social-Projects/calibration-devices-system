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
            getUnsuitabilityReasonById: function (id) {
                var url = 'admin/unsuitability-reasons/get/' + id;
                return $http.get(url).then(function (result) {
                    return result.data;
                });
            },
           /* editUnsuitabilityReason: function (formData, id) {
                var url = 'admin/unsuitability-reasons/edit/' + id;
                return $http.post(url, formData)
                    .then(function (result) {
                        return result.status;
                    });
            }, */
           /* deleteUnsuitabilityReason: function (id) {
                var url = 'admin/unsuitability-reasons/delete/' + id;
                return $http.delete(url)
                    .then(function (result) {
                        return result.status;
                    });
            },*/
            findAllCounters: function () {
                return getData('counters');
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
            return $http.get('application/' + url).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }
    })
;
