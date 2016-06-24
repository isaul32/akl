angular.module('app')
.factory('Activate', function ($resource, API_URL) {
    return $resource(API_URL + '/activate', {}, {
        'get': { method: 'GET', params: {}, isArray: false}
    });
});


