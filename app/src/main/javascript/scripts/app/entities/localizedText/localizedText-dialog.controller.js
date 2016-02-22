'use strict';

angular.module('aklApp').controller('LocalizedTextDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'LocalizedText',
        function($scope, $stateParams, $modalInstance, entity, LocalizedText) {

        $scope.localizedText = entity;
        $scope.load = function(id) {
            LocalizedText.get({id : id}, function(result) {
                $scope.localizedText = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('aklApp:localizedTextUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.localizedText.id != null) {
                LocalizedText.update($scope.localizedText, onSaveFinished);
            } else {
                LocalizedText.save($scope.localizedText, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
