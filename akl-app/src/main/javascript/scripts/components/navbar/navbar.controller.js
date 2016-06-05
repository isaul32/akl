'use strict';

angular.module('aklApp')
    .controller('NavbarController', function ($rootScope, $scope, $location, $state, Auth, Principal, AccountTeam, Api) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;

        Principal.identity().then(function (account) {
            $scope.account = account;

            if (account !== null && account.communityId !== null) {
                Api.all('steam').all('user').get(account.communityId).then(function (res) {
                    $scope.steamUser = _.result(res, 'data.response.players[0]');
                });
            }
        });

        Api.one('twitch').get().then(function (res) {
            if (res.data.stream) {
                $scope.live = true;
            }
        });

        $scope.logout = function () {
            Auth.logout();
            $state.go('home', {}, {
                reload: true
            });
        };
    });
