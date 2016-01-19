'use strict';

angular.module('aklApp')
    .controller('NavbarController', function ($scope, $location, $state, Auth, Principal, ENV, $http) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.$state = $state;
        $scope.inProduction = ENV === 'prod';

        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;

            if ($scope.isAuthenticated) {
                $http.get('api/steam/user/' + $scope.account.communityId).success(
                    function (data) {
                        $scope.steamUser = data;
                });
            }
        });

        $scope.logout = function () {
            Auth.logout();
            $state.go('home');
        };
    });
