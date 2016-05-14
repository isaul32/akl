'use strict';

angular.module('aklApp')
    .controller('TeamDetailController', function ($scope, $rootScope, $state, Team, Principal, AccountTeam, team, requests, API_URL) {
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

        $scope.declineRequest = function(id) {
            $scope.declineRequestId = id;
            $('#membershipRequestDeclination').modal('show');
        };

        $scope.sendDecline = function() {
            Team.declineRequest({id: $scope.team.id, userId: $scope.declineRequestId}).$promise
            .then(function() {
                var accessor = $('#membershipRequestDeclination');    
                accessor.modal('hide');
                accessor.one('hidden.bs.modal', function() {
                    $state.go('team.detail', {id: $scope.team.id}, {reload: true});
                });
            });
        };

        $scope.sendRequest = function() {
            Team.requestInvite({id: $scope.team.id}).$promise
            .then(function() {
                getCurrentUserDetails(true);
                $('#membershipRequestConfirmation').modal('hide');
            });
        };
    });
