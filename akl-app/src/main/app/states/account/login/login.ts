angular.module('app')
.config($stateProvider => $stateProvider
    .state('login', {
        parent: 'account',
        url: '/login',
        views: {
            'content@': {
                templateUrl: 'states/account/login/login.html',
                controller: 'LoginController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('login');
                return $translate.refresh();
            }
        }
    })
);
