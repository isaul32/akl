'use strict';

angular.module('aklApp').controller('TextDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Text', 'LocalizedText',
        function($scope, $stateParams, $modalInstance, entity, Text, LocalizedText) {

        $scope.text = entity;
        $scope.localizedtexts = LocalizedText.query();
        $scope.load = function(id) {
            Text.get({id : id}, function(result) {
                $scope.text = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('aklApp:textUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.text.id != null) {
                Text.update($scope.text, onSaveFinished);
            } else {
                Text.save($scope.text, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
