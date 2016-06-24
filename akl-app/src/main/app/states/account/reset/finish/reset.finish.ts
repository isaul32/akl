angular.module('app')
.config($stateProvider => $stateProvider
    .state('reset.finish', {
        parent: 'account',
        url: '/reset/finish?key',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/account/reset/finish/reset.finish.html',
                controller: 'ResetFinishController'
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
