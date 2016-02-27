'use strict';

angular.module('aklApp')
    .factory('Register', function ($resource) {
        return $resource('/api/register', {}, {
        });
    });


