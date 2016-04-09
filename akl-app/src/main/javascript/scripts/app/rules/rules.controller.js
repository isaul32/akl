'use strict';

angular.module('aklApp')
    .controller('RulesController', function ($scope, $translate, text) {
        $scope.eventSources = [];
        var lang = $translate.use();
        text.get().then(function (res) {
            $scope.text = res.data[lang];
        });
    });
