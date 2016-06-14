'use strict';

angular.module('aklApp')
.controller('GroupController', function ($scope, groups) {
    $scope.groups = groups.data;
    
    $scope.createTournament = function () {
        $scope.groups.all('tournament').post();
    }
});
