angular.module('app')
.controller('PasswordController', ($scope, Auth, Principal, Api, $state) => {
    Principal.identity().then(account => {
        $scope.account = account;
    });

    $scope.success = null;
    $scope.error = null;
    $scope.doNotMatch = null;
    
    $scope.changePassword = () => {
        if ($scope.password !== $scope.confirmPassword) {
            $scope.doNotMatch = 'ERROR';
        } else {
            $scope.doNotMatch = null;
            Auth.changePassword($scope.password).then(() => {
                $scope.error = null;
                $scope.success = 'OK';
            }).catch(() => {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        }
    };

    $scope.changeLogin = () => {
        Api.all("account").all("change_login").post($scope.login).then(() => {
            Auth.logout();
            $state.go('login', {
                reload: true
            });
        }).catch(err => {
            console.error(err);
        });
    }
});
