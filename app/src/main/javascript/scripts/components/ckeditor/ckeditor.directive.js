'use strict';

angular.module('aklApp')
    .directive('ckInline', function() {
        return {
            restrict: 'AEC',
            template:
                '<div>' +
                    'ckLine dir works' +
                '</div>',
            controller: ['$scope',
                function($scope) {
                    $scope.$on('$destroy', function () {
                    });
                }
            ]
        }
    });
