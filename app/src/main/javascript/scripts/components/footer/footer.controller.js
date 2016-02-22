'use strict';

angular.module('aklApp')
    .controller('FooterController', function ($scope) {
        $scope.currentYear = new Date().getFullYear();
    });
