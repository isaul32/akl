angular.module('app')
.config($stateProvider => $stateProvider
    .state('logs', {
        parent: 'admin',
        url: '/logs',
        data: {
            roles: ['ROLE_ADMIN']
        },
        views: {
            'content@': {
                templateUrl: 'states/admin/logs/logs.html',
                controller: 'LogsController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('logs');
                return $translate.refresh();
            }
        }
    })
);
