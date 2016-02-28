'use strict';

angular.module('aklApp')
    .factory('Activate', function ($resource, API_URL) {
        return $resource(API_URL + '/activate', {}, {
            'get': { method: 'GET', params: {}, isArray: false}
        });
    });


