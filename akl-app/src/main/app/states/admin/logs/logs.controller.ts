angular.module('app')
.controller('LogsController', ($scope, LogsService) => {
    LogsService.findAll().$promise.then(res => {
        $scope.loggers = res;
    }).catch(err => {
        console.error(err);
    });

    $scope.changeLevel = (name, level) => {
        LogsService.changeLevel({ name: name, level: level }, () => {
            LogsService.findAll().$promise.then(res => {
                $scope.loggers = res;
            }).catch(err => {
                console.error(err);
            });
        }, (err) => {
            console.error(err);
        });
    };
});
