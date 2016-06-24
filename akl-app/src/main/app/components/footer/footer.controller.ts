angular.module('app')
.controller('FooterController', $scope => {
    $scope.currentYear = new Date().getFullYear();
});
