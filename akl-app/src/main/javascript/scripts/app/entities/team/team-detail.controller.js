'use strict';

angular.module('aklApp')
    .controller('TeamDetailController', function ($scope, $rootScope, $state, $stateParams, Team, Principal, team) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        $scope.team = team.data;

        Principal.identity().then(function (account) {
            $scope.account = account;

            // If captain get requests
            if ($scope.team !== null && $scope.account !== null && $scope.team.captain.id === $scope.account.id) {
                Team.requests({
                    id: $stateParams.id
                }).$promise
                .then(function (requests) {
                    $scope.requests = requests;
                })
            }
        });

        $scope.requestInvite = function() {
            $('#membershipRequestConfirmation').modal('show');
        };

        $scope.sendRequest = function() {
            Team.requestInvite({id: $scope.team.id}).$promise
                .then(function() {
                    // Force update account
                    Principal.identity(true).then(function (account) {
                        $scope.account = account;
                    });
                    $('#membershipRequestConfirmation').modal('hide');
                });
        };

        $scope.declineRequest = function(id) {
            $scope.declineRequestId = id;
            $('#membershipRequestDeclination').modal('show');
        };

        $scope.sendDecline = function() {
            Team.declineRequest({id: $scope.team.id, userId: $scope.declineRequestId}).$promise
            .then(function() {
                _.remove($scope.requests, function (request) {
                    return request.id === $scope.declineRequestId;
                });
                $('#membershipRequestDeclination').modal('hide');
            });
        };

        $scope.leaveTeam = function () {
            $('#leaveTeamConfirmation').modal('show');
        };

        $scope.sendLeaveTeam = function () {
            $scope.team.post('leave').then(function (team) {
                $scope.team = team.data;
                $scope.account.teamId = null;
                $('#leaveTeamConfirmation').modal('hide');
            });
        };
    });
