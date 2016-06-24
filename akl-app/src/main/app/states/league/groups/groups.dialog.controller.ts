angular.module('app')
.controller('GroupsDialogController',($scope, $uibModalInstance, group, Api) => {
    $scope.group = group.data;
    
    $scope.save = () => {
        if ($scope.group.id != null) {
            $scope.group.put().then(() => {
                $uibModalInstance.close();
            });
        } else {
            Api.all('groups').post($scope.group).then(() => {
                $uibModalInstance.close();
            });
        }
    };

    $scope.delete = () => {
        $scope.group.remove().then(() => {
            $uibModalInstance.close();
        });
    };

    $scope.clear = () => {
        $uibModalInstance.dismiss('cancel');
    };
});
