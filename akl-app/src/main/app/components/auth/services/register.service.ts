angular.module('app')
.factory('Register', ($resource, API_URL) => {
    return $resource(API_URL + '/register', {}, {
    });
});
