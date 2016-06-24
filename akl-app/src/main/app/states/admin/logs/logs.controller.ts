angular.module('app')
.controller('LogsController', ($scope, LogsService) => {
    $scope.loggers = LogsService.findAll();

    $scope.changeLevel = (name, level) => {
        LogsService.changeLevel({ name: name, level: level }, () => {
            $scope.loggers = LogsService.findAll();
        });
    };
});
