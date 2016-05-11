'use strict';

angular.module('aklApp')
    .factory('Team', function ($resource, DateUtils, API_URL) {
        return $resource(API_URL + '/teams/:id', {id: '@id'}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' },
            'activate': {
                method: 'POST',
                url: API_URL + '/teams/:id/activate'
            }
        });
    });
