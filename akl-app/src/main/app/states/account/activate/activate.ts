angular.module('app')
.config($stateProvider => $stateProvider
    .state('activate', {
        parent: 'account',
        url: '/activate?key',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/account/activate/activate.html',
                controller: 'ActivateController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('activate');
                return $translate.refresh();
            }
        }
    })
);

