'use strict';

angular.module('aklApp')
    .controller('RulesController', function ($scope, $translate, text) {
        $scope.eventSources = [];
        $scope.lang = $translate.use();
        $scope.text = text.data;
    });
