angular.module('app')
.controller('TeamsController', ($scope, teams, $state, $stateParams, Principal, Team, seasons) => {
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.teams = teams.data;
    $scope.seasons = seasons.data;

    $scope.pages = teams.headers('X-Total-Count');
    $scope.currentPage = $stateParams.page;

    $scope.params = _.cloneDeep($stateParams);

    $scope.initSeason = () => {
        const currentSeason: any = _.find($scope.seasons, {archived: false});
        $scope.params.season = $scope.params.season || currentSeason.id;
    };

    $scope.updateSearch = () => {
        $state.transitionTo($state.current, $scope.params);
    };

    $scope.pageChanged = () => {
        $state.transitionTo($state.current, {
            page: $scope.currentPage
        });
    };

    if ($scope.isAuthenticated()) {
        Principal.identity(true).then(account => {
            $scope.account = account;
            return Principal.isInRole('ROLE_ADMIN');
        }).then(result => {
            $scope.isAdmin = result;
        });
    }

    $scope.canCreateTeam = () => {
        return $scope.account && $scope.account.email && $scope.account.activated;
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
        });
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
