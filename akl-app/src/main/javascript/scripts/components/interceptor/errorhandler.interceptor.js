'use strict';

angular.module('aklApp')
    .factory('errorHandlerInterceptor', function ($q, $rootScope, API_URL) {
        return {
            'responseError': function (response) {
                if (!(response.status == 401 && response.data.path.indexOf(API_URL + '/account') == 0 )){
	                $rootScope.$emit('aklApp.httpError', response);
	            }
                return $q.reject(response);
            }
        };
    });