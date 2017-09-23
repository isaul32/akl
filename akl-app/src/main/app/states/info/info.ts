angular.module('app')
.config($stateProvider => $stateProvider
    .state('info', {
        parent: 'root',
        url: '/info',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/info/info.html',
                controller: 'InfoController'
            }
        },
        resolve: {
            mainTranslatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('main');
                return $translate.refresh();
            },
            text: (Api) => {
                return Api.one('texts', 6).get();
            }
        }
    })
);
