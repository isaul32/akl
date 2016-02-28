'use strict';

angular.module('aklApp')
    .factory('Password', function ($resource, API_URL) {
        return $resource(API_URL + '/account/change_password', {}, {
        });
    });

angular.module('aklApp')
    .factory('PasswordResetInit', function ($resource, API_URL) {
        return $resource(API_URL + '/account/reset_password/init', {}, {
        })
    });

angular.module('aklApp')
    .factory('PasswordResetFinish', function ($resource, API_URL) {
        return $resource(API_URL + '/account/reset_password/finish', {}, {
        })
    });
