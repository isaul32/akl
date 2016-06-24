angular.module('app')
.directive('challonge', () => {
    return {
        restrict: 'E',
        template: '<iframe id="challonge" class="challonge" ng-src="{{ options.src | trusted }}" frameBorder="0" style="height: 1000px; width: 100%;"></iframe>',
        controller: ($scope, $element, $attrs, $parse) => {
            $scope.options = $parse($attrs.options)($scope) || {};
        }
    }
})
.filter('trusted', $sce => {
    return url => {
        return $sce.trustAsResourceUrl(url);
    };
});