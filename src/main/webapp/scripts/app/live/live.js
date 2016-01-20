'use strict';

angular.module('aklApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('live', {
                parent: 'site',
                url: '/live',
                data: {
                    roles: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/live/live.html',
                        controller: 'LiveController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            });
    });
