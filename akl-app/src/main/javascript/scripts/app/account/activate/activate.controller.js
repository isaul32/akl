'use strict';

angular.module('aklApp')
    .controller('ActivationController', function ($scope, $stateParams, Auth, Principal) {
        Auth.activateAccount({ key: $stateParams.key }).then(function () {
            $scope.error = null;
            $scope.success = 'OK';

            // Update account details if user is currently authenticated
            if (Principal.isAuthenticated()) {
                Principal.identity(true);
            }
        }).catch(function () {
            $scope.success = null;
            $scope.error = 'ERROR';
        });
    });

