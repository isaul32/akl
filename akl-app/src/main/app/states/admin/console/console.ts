angular.module('app')
.config($stateProvider => $stateProvider
    .state('console', {
        parent: 'admin',
        url: '/console',
        data: {
            roles: ['ROLE_ADMIN']
        },
        views: {
            'content@': {
                templateUrl: 'states/admin/console/console.html'
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
