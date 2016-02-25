'use strict';

angular.module('aklApp').controller('GameServerDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'GameServer',
        function($scope, $stateParams, $uibModalInstance, entity, GameServer) {

        $scope.gameServer = entity;
        $scope.load = function(id) {
            GameServer.get({id : id}, function(result) {
                $scope.gameServer = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('aklApp:gameServerUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.gameServer.id != null) {
                GameServer.update($scope.gameServer, onSaveSuccess, onSaveError);
            } else {
                GameServer.save($scope.gameServer, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
