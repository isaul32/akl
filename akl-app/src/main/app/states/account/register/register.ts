angular.module('app')
.config($stateProvider => $stateProvider
    .state('register', {
        parent: 'account',
        url: '/register',
        views: {
            'content@': {
                templateUrl: 'states/account/register/register.html',
                controller: 'RegisterController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('register');
                return $translate.refresh();
            }
        }
    })
);
