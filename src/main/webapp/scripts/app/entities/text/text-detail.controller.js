'use strict';

angular.module('aklApp')
    .controller('TextDetailController', function ($scope, $rootScope, $stateParams, entity, Text, LocalizedText) {
        $scope.text = entity;
        $scope.load = function (id) {
            Text.get({id: id}, function(result) {
                $scope.text = result;
            });
        };
        $rootScope.$on('aklApp:textUpdate', function(event, result) {
            $scope.text = result;
        });
    });
