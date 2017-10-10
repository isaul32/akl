angular.module('app')
.config($stateProvider => $stateProvider
    .state('akl', {
        parent: 'root',
        url: '/akl',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/akl/akl.html',
                controller: 'AKLController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('global');
                return $translate.refresh();
            },
            text: (Api) => {
                return Api.one('texts', 8).get();
            }
        }
    })
);
