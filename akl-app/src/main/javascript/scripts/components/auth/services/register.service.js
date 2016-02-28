'use strict';

angular.module('aklApp')
    .factory('Register', function ($resource, API_URL) {
        return $resource(API_URL + '/register', {}, {
        });
    });


