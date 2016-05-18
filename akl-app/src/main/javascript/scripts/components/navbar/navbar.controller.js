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
                        if (res != null
                            && res.date !== null
                            && res.data.response !== null
                            && res.data.response.players !== null
                            && res.data.response.players[0] !== null) {
                            $scope.steamUser = res.data.response.players[0];
                        }
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
