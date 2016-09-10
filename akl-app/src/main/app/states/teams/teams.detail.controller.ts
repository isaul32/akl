angular.module('app')
.controller('TeamsDetailController', ($scope, $rootScope, $state, $stateParams, Team, Principal, team) => {
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.team = team.data;

    Principal.identity().then(account => {
        $scope.account = account;

        // If captain get requests
        if ($scope.team !== null && $scope.account !== null && $scope.team.captain.id === $scope.account.id) {
            Team.requests({
                id: $stateParams.id
            }).$promise
            .then(requests => {
                $scope.requests = requests;
            })
        }
    });

    $scope.requestInvite = () => {
        $('#membershipRequestConfirmation').modal('show');
    };

    $scope.sendRequest = () => {
        Team.requestInvite({id: $scope.team.id}).$promise
            .then(() => {
                // Force update account
                Principal.identity(true).then(account => {
                    $scope.account = account;
                });
                $('#membershipRequestConfirmation').modal('hide');
            });
    };

    $scope.declineRequest = id => {
        $scope.declineRequestId = id;
        $('#membershipRequestDeclination').modal('show');
    };

    $scope.sendDecline = () => {
        Team.declineRequest({id: $scope.team.id, userId: $scope.declineRequestId}).$promise.then(() => {
            _.remove($scope.requests, (request: any) => {
                return request.id === $scope.declineRequestId;
            });
            $('#membershipRequestDeclination').modal('hide');
        });
    };

    $scope.leaveTeam = () => {
        $('#leaveTeamConfirmation').modal('show');
    };

    $scope.sendLeaveTeam = () => {
        $scope.team.post('leave').then(team => {
            $scope.team = team.data;
            $scope.account.teamId = null;
            $('#leaveTeamConfirmation').modal('hide');
        });
    };
});
