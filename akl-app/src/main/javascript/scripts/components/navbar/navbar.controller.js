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

        $scope.isValidMember = function() {
            if (!$scope.team || !$scope.account) {
                return false;
            }

            if (!$scope.team.activated) {
                return false;
            }

            var index = _.chain($scope.team.members)
                .concat($scope.team.standins)
                .concat($scope.team.captain)
                .findIndex(['id', $scope.account.id])
                .value();

            return index !== -1;
        };

        getAccount();

        $rootScope.$on('$stateChangeStart', function () {
            getAccount();
        });

        $scope.logout = function () {
            Auth.logout();
            $scope.steamUser = null;
            $state.go('home');
        };
    });
