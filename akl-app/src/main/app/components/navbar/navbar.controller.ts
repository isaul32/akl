angular.module('app')
.controller('NavbarController', ($rootScope, $scope, $location, $state, Auth, Principal, AccountTeam, Api, $http,
                                 SERVICE_PATH) => {
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.$state = $state;
    $scope.hasDBConsole = false;
    $scope.hasDocs = false;

    Principal.identity().then(account => {
        $scope.account = account;

        if (account !== null && account.communityId !== null) {
            Api.all('steam').all('user').get(account.communityId).then(res => {
                $scope.steamUser = _.result(res, 'data.response.players[0]');
            });
        }

        if (Principal.isInRole('ROLE_ADMIN')) {
            $http({
                method: 'GET',
                url: SERVICE_PATH
            }).then(() => {
                $scope.hasDocs = true;
            }, () => {
                $scope.hasDocs = false;
            });

            $http({
                method: 'GET',
                url: SERVICE_PATH + '/console'
            }).then(() => {
                $scope.hasDBConsole = true;
            }, () => {
                $scope.hasDBConsole = false;
            });
        }
    });

    Api.one('twitch').get().then(res => {
        if (res.data && res.data.stream) {
            $scope.live = true;
        }
    });

    $scope.logout = () => {
        Auth.logout();
        $state.go('home', {}, {
            reload: true
        });
    };
});
