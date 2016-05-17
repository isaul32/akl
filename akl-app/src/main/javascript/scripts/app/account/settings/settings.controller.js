'use strict';

angular.module('aklApp')
    .controller('SettingsController', function ($scope, Principal, Auth, Language, $translate) {
        $scope.success = null;
        $scope.error = null;
        
        Principal.identity(true).then(function(account) {
            $scope.settingsAccount = account;
            $scope.settingsAccount.birthdate = new Date($scope.settingsAccount.birthdate);
        });

        $scope.save = function () {
            Auth.updateAccount($scope.settingsAccount).then(function() {
                $scope.error = null;
                $scope.success = 'OK';
                
                // Force update account
                Principal.identity(true).then(function(account) {
                    $scope.settingsAccount = account;
					$scope.settingsAccount.birthdate = new Date($scope.settingsAccount.birthdate);
                });
				
                Language.getCurrent().then(function(current) {
                    if ($scope.settingsAccount.langKey !== current) {
                        $translate.use($scope.settingsAccount.langKey);
                    }
                });
            }).catch(function() {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
        };
    });
