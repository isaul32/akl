'use strict';

angular.module('aklApp')
    .factory('TeamSearch', function ($resource) {
        return $resource('api/_search/teams/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
