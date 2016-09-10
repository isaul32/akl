angular.module('app')
.config($stateProvider => $stateProvider
    .state('logout', {
        parent: 'account',
        url: '/logout',
        views: {
            'content@': {
                templateUrl: 'states/home/home.html',
                controller: 'LogoutController'
            }
        }
    })
);
