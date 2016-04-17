'use strict';

angular.module('aklApp')
    .controller('MainController', function ($scope, Principal, text, $translate) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.lang = $translate.use();
        $scope.text = text.data;
    });
