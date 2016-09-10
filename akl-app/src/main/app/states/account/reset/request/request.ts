angular.module('app')
.config($stateProvider => $stateProvider
    .state('request', {
        parent: 'reset',
        url: '/reset/request',
        views: {
            'content@': {
                templateUrl: 'states/account/reset/request/request.html',
                controller: 'RequestResetController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('reset');
                return $translate.refresh();
            }
        }
    })
);
