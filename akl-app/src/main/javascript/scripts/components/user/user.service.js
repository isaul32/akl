'use strict';

angular.module('aklApp')
    .factory('User', function ($resource, API_URL) {
        return $resource(API_URL + '/users/:login', {}, {
                'query': {method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                }
            });
        });
