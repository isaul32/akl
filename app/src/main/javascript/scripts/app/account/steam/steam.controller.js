'use strict';

angular.module('aklApp')
    .controller('SteamController', function ($scope, Principal) {
        Principal.identity(true).then(function(account) {
            $scope.settingsAccount = account;
        });
    });
