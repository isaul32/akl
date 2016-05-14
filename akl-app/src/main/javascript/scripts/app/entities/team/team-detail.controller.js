'use strict';

angular.module('aklApp')
    .controller('TeamDetailController', function ($scope, $rootScope, Team, Principal, AccountTeam, team, requests, API_URL) {
        $scope.team = team.data;
        $scope.requests = requests;

        $scope.isAuthenticated = Principal.isAuthenticated;

        var getCurrentUserDetails = function(force) {
            AccountTeam.team(force).then(function(team) {
                $scope.currentUserTeam = team;
                return Principal.identity();
            })
            .then(function(user) {
                $scope.currentUser = user;
            });
        };

        getCurrentUserDetails();

        $scope.requestInvite = function() {
            $('#membershipRequestConfirmation').modal('show');
        };

        $scope.sendRequest = function() {
            Team.requestInvite({id: $scope.team.id}).$promise
            .then(function() {
                getCurrentUserDetails(true);
                $('#membershipRequestConfirmation').modal('hide');
            });
        };
    });
