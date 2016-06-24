angular.module('app')
.controller('SessionsController', ($scope, Sessions, Principal) => {
    Principal.identity().then(account => {
        $scope.account = account;
    });

    $scope.success = null;
    $scope.error = null;
    $scope.sessions = Sessions.getAll();
    $scope.invalidate = series => {
        Sessions.delete({series: encodeURIComponent(series)}, () => {
                $scope.error = null;
                $scope.success = 'OK';
                $scope.sessions = Sessions.getAll();
            }, () => {
                $scope.success = null;
                $scope.error = 'ERROR';
            });
    };
});
