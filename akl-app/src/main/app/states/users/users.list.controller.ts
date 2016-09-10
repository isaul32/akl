angular.module('app')
.controller('UsersListController', ($rootScope, $scope, users, $state, $stateParams, Api, authorities) => {
    $scope.users = users.data;
    $scope.authorities = authorities.data;

    $scope.delete = index => {
        $scope.user = $scope.users[index];
        $('#deleteUserConfirmation').modal('show');
    };

    $scope.confirmDelete = () => {
        $scope.user.remove()
            .then(() => {
                $scope.users = _.without($scope.users, $scope.user);
                $('#deleteUserConfirmation').modal('hide');
                $scope.clear();
            });
    };

    $scope.openAuthorities = index => {
        $scope.users[index].getList('authorities')
            .then(authorities => {
                $scope.user = $scope.users[index];
                $scope.userAuthorities = [];

                _($scope.authorities).each(authority => {
                    let authorityObject: any = {};
                    authorityObject.name = authority.name;
                    authorityObject.value = _.some(authorities.data, ['name', authority.name]);
                    $scope.userAuthorities.push(authorityObject);
                });
                $('#changeUserAuthorities').modal('show');
            });
    };

    $scope.changeAuthorities = () => {
        $('#changeUserAuthorities').modal('hide');
        _.remove($scope.userAuthorities, (authority: any) => {
            return authority.value === false;
        });
        $scope.user.post('authorities', $scope.userAuthorities)
            .then(() => {
                $scope.userAuthorities = [];
                $scope.clear();
            });
    };

    $scope.pages = users.headers('X-Total-Count');
    $scope.currentPage = $stateParams.page;

    $scope.pageChanged = () => {
        $state.transitionTo($state.current, {
            page: $scope.currentPage
        });
    };

    $scope.clear = () => {
        $scope.user = {};
    }
});