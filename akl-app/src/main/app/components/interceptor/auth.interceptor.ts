angular.module('app')
.factory('authExpiredInterceptor', ($rootScope, $q, $injector, API_PATH) => {
    return {
        responseError: response => {
            if (response.status == 401 && response.data.path !== undefined && response.data.path.indexOf(API_PATH + '/account') == -1){
                let Auth = $injector.get('Auth');
                let $state = $injector.get('$state');
                let to = $rootScope.toState;
                let params = $rootScope.toStateParams;
                Auth.logout();
                $rootScope.returnToState = to;
                $rootScope.returnToStateParams = params;
                $state.go('login', {}, {
                    reload: true
                });
            }

            return $q.reject(response);
        }
    };
});
