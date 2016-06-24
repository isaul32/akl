angular.module('app')
.controller('HealthModalController', ($scope, $uibModalInstance, currentHealth, baseName, subSystemName) => {

    $scope.currentHealth = currentHealth;
    $scope.baseName = baseName;
    $scope.subSystemName = subSystemName;

    $scope.cancel = () => {
        $uibModalInstance.dismiss('cancel');
    };
});
