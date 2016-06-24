angular.module('app')
.config($stateProvider => $stateProvider
    .state('settings', {
        parent: 'account',
        url: '/settings',
        data: {
            roles: ['ROLE_USER']
        },
        views: {
            'content@': {
                templateUrl: 'states/account/settings/settings.html',
                controller: 'SettingsController'
            }
        },
        resolve: {
            translatePartialLoader: ['$translate', '$translatePartialLoader', ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('settings');
                $translatePartialLoader.addPart('rank');
                return $translate.refresh();
            }]
        }
    })
);
