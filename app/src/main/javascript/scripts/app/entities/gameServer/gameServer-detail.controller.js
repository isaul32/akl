'use strict';

angular.module('aklApp')
    .controller('GameServerDetailController', function ($scope, $rootScope, $stateParams, entity, GameServer) {
        $scope.gameServer = entity;
        $scope.load = function (id) {
            GameServer.get({id: id}, function(result) {
                $scope.gameServer = result;
            });
        };
        var unsubscribe = $rootScope.$on('aklApp:gameServerUpdate', function(event, result) {
            $scope.gameServer = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
