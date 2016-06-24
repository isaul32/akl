angular.module('app')
.controller('LoginController', ($rootScope, $scope, $state, $timeout, Auth) => {
    $scope.user = {};
    $scope.errors = {};

    $scope.rememberMe = false;
    $timeout(() => {
        angular.element('[ng-model="username"]').focus();
    });
    
    $scope.login = event => {
        event.preventDefault();
        Auth.login({
            username: $scope.username,
            password: $scope.password,
            rememberMe: $scope.rememberMe
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
