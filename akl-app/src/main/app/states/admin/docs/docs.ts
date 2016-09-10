angular.module('app')
.config($stateProvider => $stateProvider
    .state('docs', {
        parent: 'admin',
        url: '/docs',
        data: {
            roles: ['ROLE_ADMIN']
        },
        views: {
            'content@': {
                templateUrl: 'states/admin/docs/docs.html'
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
