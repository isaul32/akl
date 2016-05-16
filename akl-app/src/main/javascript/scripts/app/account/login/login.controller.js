'use strict';

angular.module('aklApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth) {
        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = false;
        $timeout(function () {
            angular.element('[ng-model="username"]').focus();
        });
        $scope.login = function (event) {
            event.preventDefault();
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe
            }).then(function () {
                $scope.authenticationError = false;
                $state.go('home', {}, {
                    reload: true
                });
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });
