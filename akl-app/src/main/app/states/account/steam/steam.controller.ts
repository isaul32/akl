angular.module('app')
.controller('SteamController', ($scope, API_URL) => {
    $scope.url = API_URL + '/login';
});
