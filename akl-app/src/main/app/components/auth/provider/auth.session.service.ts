angular.module('app')
.factory('AuthServerProvider', function loginService($http, localStorageService, $window, Tracker, API_URL, $q) {
    return {
        login: function (credentials) {
            const data = 'username=' + encodeURIComponent(credentials.username) +
                '&password=' + encodeURIComponent(credentials.password) +
                '&submit=Login';
            let deferred = $q.defer();

            $http.post(API_URL + '/authentication', data, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).then(res => {
                deferred.resolve(res);
            }).catch(err => {
                deferred.reject(err);
            });

            return deferred.promise;
        },
        logout: function () {
            Tracker.disconnect();

            // Logout from the server
            $http.post(API_URL + '/logout').then(response => {
                localStorageService.clearAll();
                // To get a new csrf token call the api
                $http.get(API_URL + '/account');
                return response;
            }).catch(err => {
                console.error(err);
            });
        },
        getToken: function () {
            return localStorageService.get('token');
        },
        hasValidToken: function () {
            const token = this.getToken();
            return !!token;
        }
    };
});
