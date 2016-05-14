'use strict';

angular.module('aklApp')
    .controller('TeamDetailController', function ($scope, $rootScope, Team, Principal, AccountTeam, team, API_URL) {
        $scope.team = team.data;

        $scope.isAuthenticated = Principal.isAuthenticated;

        var getCurrentTeam = function(force) {
            AccountTeam.team(force).then(function(team) {
                $scope.currentTeam = team;
            });
        };

        getCurrentTeam();

        $scope.requestInvite = function() {
            $('#membershipRequestConfirmation').modal('show');
        };

        $scope.sendRequest = function() {
            Principal.identity().then(function(user) {
                return Team.requestInvite({id: $scope.team.id}).$promise;
            })
            .then(function() {
                getCurrentTeam(true);
                $('#membershipRequestConfirmation').modal('hide');
            });
        };
    });
