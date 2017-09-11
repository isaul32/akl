angular.module('app')
.controller('SettingsController', ($scope, Principal, Auth, Language, $translate, $anchorScroll) => {
    $scope.success = null;
    $scope.error = null;

    Principal.identity(true).then(account => {
        $scope.settingsAccount = account;
        $scope.settingsAccount.birthdate = new Date($scope.settingsAccount.birthdate);
    });

    $scope.save = function () {
        Auth.updateAccount($scope.settingsAccount).then(() => {
            $scope.error = null;
            $scope.success = 'OK';

            // Force update account
            Principal.identity(true).then(account => {
                $scope.settingsAccount = account;
                $scope.settingsAccount.birthdate = new Date($scope.settingsAccount.birthdate);
            });

            Language.getCurrent().then(current => {
                if ($scope.settingsAccount.langKey !== current) {
                    $translate.use($scope.settingsAccount.langKey);
                }
            });

            // Scroll to top
            $anchorScroll();

        }).catch(() => {
            $scope.success = null;
            $scope.error = 'ERROR';
        });
    };
});
