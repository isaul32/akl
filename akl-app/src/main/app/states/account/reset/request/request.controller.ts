angular.module('app')
.controller('RequestResetController', ($rootScope, $scope, $state, $timeout, Auth) => {

    $scope.success = null;
    $scope.error = null;
    $scope.errorEmailNotExists = null;
    $scope.resetAccount = {};
    $timeout(() =>{
        angular.element('[ng-model="resetAccount.email"]').focus();
    });

    $scope.requestReset = () => {

        $scope.error = null;
        $scope.errorEmailNotExists = null;

        Auth.resetPasswordInit($scope.resetAccount.email).then(() => {
            $scope.success = 'OK';
        }).catch((response) => {
            $scope.success = null;
            if (response.status === 400 && response.data === 'e-mail address not registered') {
                $scope.errorEmailNotExists = 'ERROR';
            } else {
                $scope.error = 'ERROR';
            }
        });
    }

});
