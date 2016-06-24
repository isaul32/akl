angular.module('app')
.config($stateProvider => $stateProvider
    .state('rules', {
        parent: 'root',
        url: '/rules',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/rules/rules.html',
                controller: 'RulesController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('global');
                return $translate.refresh();
            },
            text: (Api) => {
                return Api.one('texts', 2).get();
            }
        }
    })
);
