'use strict';

angular.module('aklApp')
    .controller('NavbarController', function ($rootScope, $scope, $location, $state, Auth, Principal, AccountTeam, Api) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;

        Principal.identity().then(function(account) {
            $scope.account = account;

            if ($scope.isAuthenticated() && $scope.account.communityId !== null) {
                Api.all('steam').all('user').get($scope.account.communityId)
                    .then(function (res) {
                        $scope.steamUser = res.data;
                    });
            }
        });

        $scope.logout = function () {
            Auth.logout();
            $state.go('home', {}, {
                reload: true
            });
        };
    });
