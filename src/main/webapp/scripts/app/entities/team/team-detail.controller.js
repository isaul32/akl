'use strict';

angular.module('aklApp')
    .controller('TeamDetailController', function ($scope, $rootScope, $stateParams, entity, Team, User) {
        $scope.team = entity;
        $scope.load = function (id) {
            Team.get({id: id}, function(result) {
                $scope.team = result;
            });
        };
        $rootScope.$on('aklApp:teamUpdate', function(event, result) {
            $scope.team = result;
        });
    });
