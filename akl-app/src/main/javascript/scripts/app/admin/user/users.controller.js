'use strict';

angular.module('aklApp')
    .controller('UsersController', function ($scope, users) {
        $scope.users = users;
    });