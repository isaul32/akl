angular.module('app')
.controller('ResetFinishController', ($scope, $stateParams, $timeout, Auth) => {

    $scope.keyMissing = $stateParams.key === undefined;
    $scope.doNotMatch = null;

    $scope.resetAccount = {};
    $timeout(() => {
        angular.element('[ng-model="resetAccount.password"]').focus();
    });

    $scope.finishReset = () => {
        if ($scope.resetAccount.password !== $scope.confirmPassword) {
            $scope.doNotMatch = 'ERROR';
        } else {
            Auth.resetPasswordFinish({key: $stateParams.key, newPassword: $scope.resetAccount.password}).then(() => {
                $scope.success = 'OK';
            }).catch(() => {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        }
    };
});
