angular.module('app')
.config($stateProvider => $stateProvider
    .state('steam', {
        parent: 'account',
        url: '/steam',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/account/steam/steam.html',
                controller: 'SteamController'
            }
        },
        resolve: {
            translatePartialLoader: ['$translate', '$translatePartialLoader', ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('steam');
                return $translate.refresh();
            }]
        }
    })
);
