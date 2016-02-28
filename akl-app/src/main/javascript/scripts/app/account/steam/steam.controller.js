'use strict';

angular.module('aklApp')
    .controller('SteamController', function ($scope, Principal, API_URL) {
        Principal.identity(true).then(function(account) {
            $scope.settingsAccount = account;
            $scope.url = API_URL + '/login';
        });
    });
