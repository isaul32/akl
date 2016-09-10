angular.module('app')
.config($stateProvider => $stateProvider
    .state('health', {
        parent: 'admin',
        url: '/health',
        data: {
            roles: ['ROLE_ADMIN']
        },
        views: {
            'content@': {
                templateUrl: 'states/admin/health/health.html',
                controller: 'HealthController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('health');
                return $translate.refresh();
            }
        }
    })
);
