angular.module('app')
    .factory('MonitoringService', ($rootScope, $http, SERVICE_URL) => {
        return {
            getMetrics: () => {
                return $http.get(SERVICE_URL + '/metrics').then(response => {
                    return response.data;
                });
            },
            checkHealth: () => {
                return $http.get(SERVICE_URL + '/health').then(response => {
                    return response.data;
                });
            },
            threadDump: () => {
                return $http.get(SERVICE_URL + '/dump').then(response => {
                    return response.data;
                });
            }
        };
    });
