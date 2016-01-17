'use strict';

angular.module('aklApp')
    .factory('GameServerSearch', function ($resource) {
        return $resource('api/_search/gameServers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
