angular.module('app')
.config($stateProvider => $stateProvider
    .state('error', {
        parent: 'root',
        url: '/error',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/error/error.html'
            }
        },
        resolve: {
            mainTranslatePartialLoader: ($translate,$translatePartialLoader) => {
                $translatePartialLoader.addPart('error');
                return $translate.refresh();
            }
        }
    })
    .state('accessdenied', {
        parent: 'root',
        url: '/accessdenied',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/error/accessdenied.html'
            }
        },
        resolve: {
            mainTranslatePartialLoader: ($translate,$translatePartialLoader) => {
                $translatePartialLoader.addPart('error');
                return $translate.refresh();
            }
        }
    })
);
