angular.module('app')
.config($stateProvider => $stateProvider
    .state('home', {
        parent: 'root',
        url: '/',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/home/home.html',
                controller: 'HomeController'
            }
        },
        resolve: {
            mainTranslatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('main');
                return $translate.refresh();
            },
            text: (Api) => {
                return Api.one('texts', 1).get();
            }
        }
    })
);
