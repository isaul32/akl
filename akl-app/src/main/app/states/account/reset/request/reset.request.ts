angular.module('app')
.config($stateProvider => $stateProvider
    .state('reset.request', {
        parent: 'account',
        url: '/reset/request',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/account/reset/request/reset.request.html',
                controller: 'RequestResetController'
            }
        },
        resolve: {
            translatePartialLoader: ['$translate', '$translatePartialLoader', ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('reset');
                return $translate.refresh();
            }]
        }
    })
);
