'use strict';

angular.module('aklApp')
    .controller('TeamController', function ($scope, teams, $state, $stateParams, Principal, Team) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.teams = teams.data;

        $scope.pages = teams.headers('X-Total-Count');
        $scope.currentPage = $stateParams.page;

        $scope.pageChanged = function() {
            $state.transitionTo($state.current, {
                page: $scope.currentPage
            });
        };

        if ($scope.isAuthenticated()) {
            Principal.identity(true).then(function(account) {
                $scope.account = account;
                return Principal.isInRole('ROLE_ADMIN');
            }).then(function(result) {
                $scope.isAdmin = result;
            });
        }

        $scope.canCreateTeam = function() {
            return $scope.account && $scope.account.email && $scope.account.activated;
        };

        $scope.hasPermissions = function(team) {
            return $scope.isAdmin;
        };

        $scope.isInactive = function (team) {
            return $scope.isAdmin && !team.activated;
        };

        $scope.activate = function (id) {
            Team.activate({id: id}, function(result) {
                _.find($scope.teams, {
                    id: id
                }).activated = true;
                $scope.clear();
            });
        };

        $scope.delete = function (id) {
            Team.get({id: id}, function(result) {
                $scope.team = result;
                $('#deleteTeamConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Team.delete({id: id},
                function () {
                    _.remove($scope.teams, {
                        id: id
                    });
                    $('#deleteTeamConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.team = {};
        };
    });
