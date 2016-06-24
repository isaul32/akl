angular.module('app')
.factory('LogsService', ($resource, API_URL) => {
    return $resource(API_URL + '/logs', {}, {
        'findAll': { method: 'GET', isArray: true},
        'changeLevel': { method: 'PUT'}
    });
});
