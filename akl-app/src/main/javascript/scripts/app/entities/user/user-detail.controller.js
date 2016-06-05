'use strict';

angular.module('aklApp')
    .controller('UserDetailController', function ($scope, $rootScope, user, steamUser) {
        $scope.user = user.data;
        $scope.steamUser = _.result(steamUser, 'data.response.players[0]');
    });
