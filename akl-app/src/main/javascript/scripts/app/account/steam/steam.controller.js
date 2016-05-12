'use strict';

angular.module('aklApp')
    .controller('SteamController', function ($scope, API_URL) {
        $scope.url = API_URL + '/login';
    });
