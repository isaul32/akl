angular.module('app')
.config($stateProvider => $stateProvider
    .state('configuration', {
        parent: 'admin',
        url: '/configuration',
        data: {
            roles: ['ROLE_ADMIN']
        },
        views: {
            'content@': {
                templateUrl: 'states/admin/configuration/configuration.html',
                controller: 'ConfigurationController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('configuration');
                return $translate.refresh();
            }
        }
    })
);
