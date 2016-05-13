'use strict';

angular.module('aklApp')
    .controller('TeamDetailController', function ($scope, $rootScope, team) {
        $scope.team = team.data;
    });
