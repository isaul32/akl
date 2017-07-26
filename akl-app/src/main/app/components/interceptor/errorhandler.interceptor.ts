angular.module('app')
.factory('errorHandlerInterceptor', ($q, $rootScope, API_URL) => {
    return {
        responseError: response => {
            if (!(response.status == 401 && response.data.path.indexOf(API_URL + '/account') == 0 )){
                $rootScope.$emit('aklApp.httpError', response);
            }
            return $q.reject(response);
        }
    };
});
