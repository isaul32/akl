'use strict';

angular.module('aklApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
