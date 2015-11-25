angular
    .module('adminModule')
    .factory('UserService', function ($http) {
        return {
            isUsernameAvailable: function (username) {
                var url = 'admin/users/available/' + username;
                return $http.get(url)
                    .then(function(result) {
                        return result.data;
                    });
            },
            saveUser: function (userData) {
                return $http.post('admin/sysadmins/add', userData)
                    .success(function (response, status) {
                        console.log("Done status: " + status);
                        return response;
                    })
                    .error(function (err) {
                        alert(err);
                        alert("in error");
                        return err;
                    });
            },

          /*      function (username) {
                var url = '/admin/users/available/' + username;
                return $http.get(url)
                    .then(function(result) {
                        return result.data;
                    });
            }*/
        };
        function getData(url, params) {
            return $http.get(url, {
                params: params
            }).success(function (data) {
                return data;
            }).error(function (err) {
                return err;
            });
        }

        function saveData(url, data) {
            return $http.post(url, data)
                .success(function (response) {
                    return response;
                })
                .error(function (err) {
                    alert(err);
                    alert("in error");
                    return err;
                });
        }
    });
