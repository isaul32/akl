'use strict';

angular.module('aklApp')
    .factory('Sessions', function ($resource) {
        return $resource('/api/account/sessions/:series', {}, {
            'getAll': { method: 'GET', isArray: true}
        });
    });



