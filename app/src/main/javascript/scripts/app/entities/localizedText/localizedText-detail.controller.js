'use strict';

angular.module('aklApp')
    .controller('LocalizedTextDetailController', function ($scope, $rootScope, $stateParams, entity, LocalizedText) {
        $scope.localizedText = entity;
        $scope.load = function (id) {
            LocalizedText.get({id: id}, function(result) {
                $scope.localizedText = result;
            });
        };
        $rootScope.$on('aklApp:localizedTextUpdate', function(event, result) {
            $scope.localizedText = result;
        });
    });
