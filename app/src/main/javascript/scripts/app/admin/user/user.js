'use strict';

angular.module('aklApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user', {
                parent: 'admin',
                url: '/user',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'user.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/user/user.html',
                        //controller: 'UserController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user');
                        return $translate.refresh();
                    }]
                }
            });
    });
