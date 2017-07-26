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
        }).catch(err => {
            $scope.success = null;
            if (err.status === 400 && err.data === 'e-mail address not registered') {
                $scope.errorEmailNotExists = 'ERROR';
            } else {
                $scope.error = 'ERROR';
            }
        });
    }

});
