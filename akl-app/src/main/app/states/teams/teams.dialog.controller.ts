angular.module('app')
.controller('TeamsDialogController', ($scope, $stateParams, $uibModalInstance, entity, Team, $state) => {
    $scope.team = entity;
    $scope.load = (id) => {
        Team.get({id : id}, result => {
            $scope.team = result;
        });
    };

    let onSaveFinished = result => {
        $scope.$emit('aklApp:teamUpdate', result);
        $uibModalInstance.close(result);
        $state.go("^", null, { reload: true });
    };

    $scope.save = () => {
        if ($scope.team.id != null) {
            Team.update($scope.team, onSaveFinished);
        } else {
            Team.save($scope.team, onSaveFinished);
        }
    };

    $scope.clear = () => {
        $uibModalInstance.dismiss('cancel');
        $state.go("^")
    };
});
