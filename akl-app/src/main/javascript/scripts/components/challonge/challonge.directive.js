'use strict';

angular.module('aklApp')
.directive('challonge', function () {
    return {
        restrict: 'E',
        template: '<iframe id="challonge" class="challonge" ng-src="{{ options.src | trusted }}" frameBorder="0" style="height: 1000px; width: 100%;"></iframe>',
        controller: function ($scope, $element, $attrs, $parse) {
            $scope.options = $parse($attrs.options)($scope) || {};
        }
    }
})
.filter('trusted', ['$sce', function ($sce) {
    return function (url) {
        return $sce.trustAsResourceUrl(url);
    };
}]);