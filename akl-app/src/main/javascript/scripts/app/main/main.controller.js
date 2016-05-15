'use strict';

angular.module('aklApp')
    .controller('MainController', function ($scope, $rootScope, Principal, $translate, text) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.lang = $translate.use();
        $rootScope.$on('$translateChangeSuccess', function () {
            $scope.lang = $translate.use();
        });

        $scope.text = text.data;
    });
