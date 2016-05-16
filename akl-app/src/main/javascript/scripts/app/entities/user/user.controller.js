'use strict';

angular.module('aklApp')
    .controller('UserController', function ($scope, users, $state, Api, authorities, ParseLinks) {
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
            $('#changeUserAuthorities').modal('hide');
            _.remove($scope.userAuthorities, function (authority) {
                return authority.value === false;
            });
            $scope.user.post('authorities', $scope.userAuthorities)
                .then(function () {
                    $scope.userAuthorities = [];
                    $scope.clear();
                });
        };

        $scope.loadAll = function() {
            Api.all('users').getList({
                page: $scope.page,
                per_page: 20
            }).then(function (page) {
                $scope.links = ParseLinks.parse(page.headers('link'));
                $scope.users = page.data;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.clear = function () {
            $scope.user = {};
        }
    });