angular.module('app')
.config($stateProvider => $stateProvider
    .state('audits', {
        parent: 'admin',
        url: '/audits',
        data: {
            roles: ['ROLE_ADMIN']
        },
        views: {
            'content@': {
                templateUrl: 'states/admin/audits/audits.html',
                controller: 'AuditsController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('audits');
                return $translate.refresh();
            }
        }
    })
);
