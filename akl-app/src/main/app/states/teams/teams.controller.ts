angular.module('app')
.controller('TeamsController', ($scope, teams, $state, $stateParams, Principal, Team, seasons) => {
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.teams = teams.data;
    $scope.seasons = seasons.data;

    $scope.pages = teams.headers('X-Total-Count');
    $scope.params = _.cloneDeep($stateParams);

    if ($scope.params.filter != null) {
        angular.element("#filter").focus();
    }

    $scope.initSeason = () => {
        const currentSeason: any = _.find($scope.seasons, {archived: false});
        $scope.params.season = $scope.params.season || currentSeason.id;
    };

    $scope.updateSearch = () => {
        $state.transitionTo($state.current, $scope.params);
    };

    if ($scope.isAuthenticated()) {
        Principal.identity(true).then(account => {
            $scope.account = account;
            Principal.isInRole('ROLE_ADMIN').then(res => {
                $scope.isAdmin = res;
            });
        });
    }

    $scope.canCreateTeam = () => {
        const account = $scope.account;
        if (account && account.activated) {
            const ownTeam = _.find(account.teams, (team: any) => !team.season.archived);
            if (!ownTeam) {
                return true;
            }
        }

        return false;
    };

    $scope.hasPermissions = team => {
        return $scope.isAdmin;
    };

    $scope.isInactive = team => {
        return $scope.isAdmin && !team.activated;
    };

    $scope.activate = id => {
        Team.activate({id: id}, () => {
            const team: any = _.find($scope.teams, { id: id});
            team.activated = true;
            $scope.clear();
        }).$promise.then(() => {}).catch(() => {});
    };

    $scope.remove = id => {
        Team.get({id: id}, result => {
            $scope.team = result;
            $('#deleteTeamConfirmation').modal('show');
        });
    };

    $scope.confirmDelete = id => {
        Team.delete({id: id}, () => {
            _.remove($scope.teams, {
                id: id
            });
            $('#deleteTeamConfirmation').modal('hide');
            $scope.clear();
        });
    };

    $scope.clear = () => {
        $scope.team = {};
    };
});
