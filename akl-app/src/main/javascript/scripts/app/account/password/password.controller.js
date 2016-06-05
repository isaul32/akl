'use strict';

angular.module('aklApp')
    .controller('PasswordController', function ($scope, Auth, Principal, Api, $state) {
        Principal.identity().then(function(account) {
            $scope.account = account;
        });

        $scope.success = null;
        $scope.error = null;
        $scope.doNotMatch = null;
        $scope.changePassword = function () {
            if ($scope.password !== $scope.confirmPassword) {
                $scope.doNotMatch = 'ERROR';
            } else {
                $scope.doNotMatch = null;
                Auth.changePassword($scope.password).then(function () {
                    $scope.error = null;
                    $scope.success = 'OK';
                }).catch(function () {
                    $scope.success = null;
                    $scope.error = 'ERROR';
                });
            }
        };
        $scope.changeLogin = function () {
            Api.all("account").all("change_login").post($scope.login).then(function () {
                Auth.logout();
                $state.go('login', {
                    reload: true
                });
            });
        }
    });
