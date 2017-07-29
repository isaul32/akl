angular.module('app')
.config($stateProvider => $stateProvider
    .state('faq', {
        parent: 'root',
        url: '/faq',
        views: {
            'content@': {
                templateUrl: 'states/faq/faq.html',
                controller: 'FaqController'
            }
        },
        resolve: {
            mainTranslatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('main');
                return $translate.refresh();
            },
            text: (Api) => {
                return Api.one('texts', 5).get();
            }
        }
    })
);
