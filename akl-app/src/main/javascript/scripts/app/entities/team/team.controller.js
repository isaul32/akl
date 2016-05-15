'use strict';

angular.module('aklApp')
    .controller('TeamController', function ($scope, Team, ParseLinks, Principal, currentTeam) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.teams = [];
        $scope.page = 1;
        $scope.currentTeam = currentTeam;
        
        $scope.loadAll = function() {
            Team.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.teams = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        Principal.identity().then(function(identity) {
            $scope.identity = identity;
            return Principal.isInRole('ROLE_ADMIN');
        }).then(function(result) {
            $scope.isAdmin = result;
        });

        $scope.canCreateTeam = function() {
            return $scope.identity && $scope.identity.email && $scope.identity.activated;
        };

        $scope.hasPermissions = function(team) {
            //return $scope.isAdmin || $scope.identity && ($scope.identity.id === team.captain.id);
            return $scope.isAdmin;
        };

        $scope.isInactive = function (team) {
            return $scope.isAdmin && !team.activated;
        };

        $scope.activate = function (id) {
            Team.activate({id: id}, function(result) {
                $scope.loadAll();
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
                    $scope.loadAll();
                    $('#deleteTeamConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.team = {tag: null, name: null, imageUrl: null, rank: null, description: null, id: null};
        };
    });
