'use strict';

angular.module('aklApp')
    .factory('LocalizedText', function ($resource, DateUtils) {
        return $resource('/api/localizedTexts/:id', {}, {
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
