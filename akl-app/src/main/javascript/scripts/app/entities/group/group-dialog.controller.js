'use strict';

angular.module('aklApp')
.controller('GroupDialogController', function ($scope, $uibModalInstance, group, Api) {
    $scope.group = group.data;
    
    $scope.save = function () {
        if ($scope.group.id != null) {
            $scope.group.put().then(function () {
                $uibModalInstance.close();
            });
        } else {
            Api.all('groups').post($scope.group).then(function () {
                $uibModalInstance.close();
            });
        }
    };

    $scope.delete = function () {
        $scope.group.remove().then(function () {
            $uibModalInstance.close();
        });
    };

    $scope.clear = function () {
        $uibModalInstance.dismiss('cancel');
    };
});
