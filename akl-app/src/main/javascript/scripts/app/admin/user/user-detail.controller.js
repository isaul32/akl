'use strict';

angular.module('aklApp')
    .controller('UserDetailController', function ($scope, $rootScope, user) {
        $scope.user = user.data.plain();
    });
