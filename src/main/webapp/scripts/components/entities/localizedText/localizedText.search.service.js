'use strict';

angular.module('aklApp')
    .factory('LocalizedTextSearch', function ($resource) {
        return $resource('api/_search/localizedTexts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
