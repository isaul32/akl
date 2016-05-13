'use strict';

angular.module('aklApp').controller('TeamDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Team',
        function($scope, $stateParams, $uibModalInstance, entity, Team) {

        $scope.team = entity;
        $scope.load = function(id) {
            Team.get({id : id}, function(result) {
                $scope.team = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('aklApp:teamUpdate', result);
            $uibModalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.team.id != null) {
                Team.update($scope.team, onSaveFinished);
            } else {
                Team.save($scope.team, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
