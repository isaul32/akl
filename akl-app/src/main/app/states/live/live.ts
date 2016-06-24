angular.module('app')
.config($stateProvider => $stateProvider
    .state('live', {
        parent: 'root',
        url: '/live',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/live/live.html'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('global');
                return $translate.refresh();
            }
        }
    })
);
