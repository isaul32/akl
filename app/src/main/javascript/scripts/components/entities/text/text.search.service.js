'use strict';

angular.module('aklApp')
    .factory('TextSearch', function ($resource) {
        return $resource('api/_search/texts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
