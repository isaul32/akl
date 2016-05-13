'use strict';

angular.module('aklApp')
    .controller('UsersController', function ($scope, users, $stateParams, $state, Api) {
        $scope.users = users.data;

        $scope.delete = function (index) {
            $scope.user = $scope.users[index];
            $('#deleteUserConfirmation').modal('show');
        };

        $scope.confirmDelete = function () {
            $scope.user.remove()
                .then(function (res) {
                    console.log(res);
                    $scope.users = _.without($scope.users, $scope.user);
                    $('#deleteUserConfirmation').modal('hide');
                    $scope.user = {};
                });
        };
    });