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
                        templateUrl: 'scripts/app/admin/user/users.html',
                        controller: 'UserController'
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
            })
            .state('user.detail', {
                parent: 'admin',
                url: '/user/{id}',
                data: {
                    roles: [],
                    pageTitle: 'user.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/admin/user/user-detail.html',
                        controller: 'UserDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user');
                        $translatePartialLoader.addPart('rank');
                        return $translate.refresh();
                    }],
                    user: ['$stateParams', 'Api', function($stateParams, Api) {
                        return Api.one('users', $stateParams.id).get();
                    }]
                }
            });
    });
