angular.module('app')
.controller('LoginController', ($scope, $state, Auth) => {
    $scope.rememberMe = false;
    angular.element('[ng-model="username"]').focus();
    
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
            console.log($scope.authenticationError);
        });
    };
});
