'use strict';

angular.module('aklApp')
    .factory('LogsService', function ($resource, API_URL) {
        return $resource(API_URL + '/logs', {}, {
            'findAll': { method: 'GET', isArray: true},
            'changeLevel': { method: 'PUT'}
        });
    });
