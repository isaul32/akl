'use strict';

angular.module('aklApp')
    .factory('authExpiredInterceptor', function ($rootScope, $q, $injector, API_PATH) {
        return {
            responseError: function(response) {
                if (response.status == 401 && response.data.path !== undefined && response.data.path.indexOf(API_PATH + '/account') == -1){
                    var Auth = $injector.get('Auth');
                    var $state = $injector.get('$state');
                    var to = $rootScope.toState;
                    var params = $rootScope.toStateParams;
                    Auth.logout();
                    $rootScope.returnToState = to;
                    $rootScope.returnToStateParams = params;
                    $state.go('login');
                }
                return $q.reject(response);
            }
        };
    });
