'use strict';

angular.module('aklApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('rules', {
                parent: 'site',
                url: '/rules',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/rules/rules.html',
                        controller: 'RulesController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    text: ['Api', function (Api) {
                        return Api.one('texts', 2).get();
                    }]
                }
            });
    });
