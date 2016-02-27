'use strict';

angular.module('aklApp')
	.controller('GameServerDeleteController', function($scope, $uibModalInstance, entity, GameServer) {

        $scope.gameServer = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            GameServer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
