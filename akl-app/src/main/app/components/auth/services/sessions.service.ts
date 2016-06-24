angular.module('app')
.factory('Sessions', ($resource, API_URL) => {
    return $resource(API_URL + '/account/sessions/:series', {}, {
        'getAll': { method: 'GET', isArray: true }
    });
});



