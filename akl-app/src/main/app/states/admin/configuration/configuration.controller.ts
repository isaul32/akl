angular.module('app')
.controller('ConfigurationController', ($scope, ConfigurationService) => {
    ConfigurationService.get().then(configuration => {
        $scope.configuration = configuration;
    });
});
