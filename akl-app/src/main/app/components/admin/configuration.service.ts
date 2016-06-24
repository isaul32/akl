angular.module('app')
.factory('ConfigurationService', ($rootScope, $filter, $http, SERVICE_URL) => {
    return {
        get: () => {
            return $http.get(SERVICE_URL + '/configprops').then(response => {
                let properties = [];
                angular.forEach(response.data, data => properties.push(data));
                let orderBy = $filter('orderBy');
                return orderBy(properties, 'prefix');
            });
        }
    };
});
