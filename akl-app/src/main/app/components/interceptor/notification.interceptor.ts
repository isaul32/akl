angular.module('app')
.factory('notificationInterceptor', ($q, AlertService) => {
    return {
        response: response => {
            let alertKey = response.headers('X-aklApp-alert');
            if (angular.isString(alertKey)) {
                AlertService.success(alertKey, { param : response.headers('X-aklApp-params') });
            }
            return response;
        },
    };
});
