angular
    .module('adminModule')
    .factory('DevicesService', function ($http) {
        return {
            getPage: function (pageNumber, itemsPerPage, search, sortCriteria, sortOrder) {
                return getDataWithParams('admin/device-category/' + pageNumber + '/' + itemsPerPage + '/' + sortCriteria + '/' + sortOrder, search);
            },
            saveDeviceCategory: function (formData) {
                return $http.post("admin/device-category/add", formData)
                    .then(function (result) {
                        return result.status;

                    });
            },
            getDeviceCategoryById: function (id) {
                var url = 'admin/device-category/get/' + id;
                return $http.get(url).then(function (result) {
                    return result.data;
                });
            },
            editDeviceCategory: function (formData, id) {
                var url = 'admin/device-category/edit/' + id;
                return $http.post(url, formData)
                    .then(function (result) {
                        return result.status;
                    });
            },
           deleteDeviceCategory: function (id) {
                var url = 'admin/device-category/delete/' + id;
                return $http.delete(url)
                    .then(function (result) {
                        return result.status;
                    });
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
    });