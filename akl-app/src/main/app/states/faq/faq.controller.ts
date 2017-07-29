angular.module('app')
    .controller('FaqController', ($scope, $rootScope, Principal, $translate, text) => {
        Principal.identity().then((account) => {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
        });

        $scope.lang = $translate.use();
        $rootScope.$on('$translateChangeSuccess', () => {
            $scope.lang = $translate.use();
        });

        $scope.text = text.data;
    });
