'use strict';

angular.module('aklApp')
    .controller('UsersController', function ($scope, users, $state, Api, authorities) {
        $scope.users = users.data;
        $scope.authorities = authorities.data;

        $scope.delete = function (index) {
            $scope.user = $scope.users[index];
            $('#deleteUserConfirmation').modal('show');
        };

        $scope.confirmDelete = function () {
            $scope.user.remove()
                .then(function () {
                    $scope.users = _.without($scope.users, $scope.user);
                    $('#deleteUserConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.openAuthorities = function (index) {
            $scope.users[index].getList('authorities')
                .then(function (authorities) {
                    $scope.user = $scope.users[index];
                    $scope.userAuthorities = [];

                    _($scope.authorities).each(function (authority) {
                        var authorityObject = {};
                        authorityObject.name = authority.name;
                        authorityObject.value = _.some(authorities.data, ['name', authority.name]);
                        $scope.userAuthorities.push(authorityObject);
                    });
                    $('#changeUserAuthorities').modal('show');
                });
        };

        $scope.changeAuthorities = function () {
            _.remove($scope.userAuthorities, function (authority) {
                return authority.value === false;
            });
            $scope.user.post('authorities', $scope.userAuthorities)
                .then(function () {
                    $('#changeUserAuthorities').modal('hide');
                    $scope.userAuthorities = [];
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.user = {};
        }
    });