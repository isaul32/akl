'use strict';

angular.module('aklApp')
    .controller('MainController', function ($scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            $scope.content = "<h1>Testi</h1>";
        });
    });
