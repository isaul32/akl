angular.module('app')
.controller('NavbarController', ($rootScope, $scope, $location, $state, Auth, Principal, AccountTeam, Api) => {
    $scope.isAuthenticated = Principal.isAuthenticated;
    $scope.$state = $state;

    Principal.identity().then(account => {
        $scope.account = account;

        if (account !== null && account.communityId !== null) {
            Api.all('steam').all('user').get(account.communityId).then(res => {
                $scope.steamUser = _.result(res, 'data.response.players[0]');
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
