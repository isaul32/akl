angular.module('app')
.config($stateProvider => $stateProvider
    .state('password', {
        parent: 'account',
        url: '/password',
        data: {
            roles: ['ROLE_USER']
        },
        views: {
            'content@': {
                templateUrl: 'states/account/password/password.html',
                controller: 'PasswordController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('password');
                return $translate.refresh();
            }
        }
    })
);
