angular.module('app')
.controller('ActivateController', ($scope, $stateParams, Auth, Principal) => {
    Auth.activateAccount({ key: $stateParams.key }).then(() => {
        $scope.error = null;
        $scope.success = 'OK';

        if (Principal.isAuthenticated()) {
            Principal.identity(true);
        }
    }).catch(() => {
        $scope.success = null;
        $scope.error = 'ERROR';
    });
});

