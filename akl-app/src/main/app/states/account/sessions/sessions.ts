angular.module('app')
.config($stateProvider => $stateProvider
    .state('sessions', {
        parent: 'account',
        url: '/sessions',
        data: {
            roles: ['ROLE_USER']
        },
        views: {
            'content@': {
                templateUrl: 'states/account/sessions/sessions.html',
                controller: 'SessionsController'
            }
        },
        resolve: {
            translatePartialLoader: ['$translate', '$translatePartialLoader', ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('sessions');
                return $translate.refresh();
            }]
        }
    })
);
