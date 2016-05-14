'use strict';

angular.module('aklApp')
    .controller('NavbarController', function ($rootScope, $scope, $location, $state, Auth, Principal, AccountTeam, $http, API_URL) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;

        var getAccount = function () {
            Principal.identity().then(function(account) {
                $scope.account = account;

                if ($scope.isAuthenticated() && $scope.account.communityId !== null) {
                    $http.get(API_URL + '/steam/user/' + $scope.account.communityId).success(function (data) {
                        $scope.steamUser = data;
                    });
                }

                return AccountTeam.team();
            }).then(function(team) {
                $scope.team = team;
            });
        };

        getAccount();

        $rootScope.$on('$stateChangeStart', function () {
            getAccount();
        });

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
    });
