angular.module('app')
.config($stateProvider => $stateProvider
    .state('finish', {
        parent: 'reset',
        url: '/reset/finish?key',
        views: {
            'content@': {
                templateUrl: 'states/account/reset/finish/finish.html',
                controller: 'ResetFinishController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('reset');
                return $translate.refresh();
            }
        }
    })
);
