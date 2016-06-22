'use strict';

angular.module('aklApp')
.directive('challonge', function () {
    return {
        restrict: 'E',
        template: '<div style="height: 100%">' +
                  '    <iframe id="challonge" class="challonge" ng-src="{{ options.src | trusted }}" frameBorder="0" style="height: 1000px; width: 100%;"></iframe>' +
                  '</div>',
        controller: function ($scope, $element, $attrs, $parse) {
            $scope.options = $parse($attrs.options)($scope) || {};
            var window = $element[0];
            console.log(window);
        }
    }
})
.filter('trusted', ['$sce', function ($sce) {
    return function (url) {
        return $sce.trustAsResourceUrl(url);
    };
}]);