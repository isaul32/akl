'use strict';

angular.module('aklApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('steam', {
                parent: 'account',
                url: '/login-steam',
                data: {
                    roles: [],
                    pageTitle: 'login.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/account/steam/steam.html',
                        controller: 'SteamController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('steam');
                        return $translate.refresh();
                    }]
                }
            });
    });
