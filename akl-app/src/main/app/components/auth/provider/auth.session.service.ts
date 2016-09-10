angular.module('app')
.factory('AuthServerProvider', function loginService($http, localStorageService, $window, Tracker, API_URL) {
    return {
        login: function (credentials) {
            var data = 'username=' + encodeURIComponent(credentials.username) +
                '&password=' + encodeURIComponent(credentials.password) +
                '&remember-me=' + credentials.rememberMe + '&submit=Login';
            return $http.post(API_URL + '/authentication', data, {
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                }
            }).success(function (response) {
                return response;
            });
        },
        logout: function () {
            Tracker.disconnect();

            // logout from the server
            $http.post(API_URL + '/logout').success(response => {
                localStorageService.clearAll();
                // to get a new csrf token call the api
                $http.get(API_URL + '/account');
                return response;
            });
        },
        getToken: function () {
            var token = localStorageService.get('token');
            return token;
        },
        hasValidToken: function () {
            var token = this.getToken();
            return !!token;
        }
    };
});
