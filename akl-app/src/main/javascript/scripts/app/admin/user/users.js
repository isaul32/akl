'use strict';

angular.module('aklApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('user', {
                parent: 'admin',
                url: '/users',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'user.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/user/users.html',
                        controller: 'UsersController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user');
                        return $translate.refresh();
                    }],
                    /*count: ['Api', function (Api) {
                        return Api.all('users').one('count').get();
                    }],*/
                    users: ['Api', '$stateParams', function (Api, $stateParams) {
                        return Api.all('users').getList();
                    }],
                    authorities: ['Api', function (Api) {
                        return Api.all('users').all('authorities').getList();
                    }]
                }
            });
    });
