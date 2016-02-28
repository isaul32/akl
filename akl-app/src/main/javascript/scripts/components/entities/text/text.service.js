'use strict';

angular.module('aklApp')
    .factory('Text', function ($resource, DateUtils, API_URL) {
        return $resource(API_URL + '/texts/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
