'use strict';

angular.module('aklApp')
    .controller('UserDetailController', function ($scope, $rootScope, user, steamUser) {
        $scope.user = user.data;
        if (steamUser != null
            && steamUser.date !== null
            && steamUser.data.response !== null
            && steamUser.data.response.players !== null
            && steamUser.data.response.players[0] !== null) {
            $scope.steamUser = steamUser.data.response.players[0];
        }
    });
