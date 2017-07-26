angular.module('app')
.controller('LoginController', ($scope, $state, Auth) => {
    angular.element('[ng-model="username"]').focus();

    $scope.authenticationError = false;
    
    $scope.login = event => {
        event.preventDefault();

        Auth.login({
            username: $scope.username,
            password: $scope.password,
        }).then(() => {
            $scope.authenticationError = false;
            $state.go('home', {}, {
                reload: true
            });
        }).catch(() => {
            $scope.authenticationError = true;
        });
    };
});
