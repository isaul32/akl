'use strict';

angular.module('aklApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('users', {
                parent: 'admin',
                url: '/user',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'user.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/user/users.html',
                        controller: 'UserController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('user');
                        return $translate.refresh();
                    }],
                    users: ['Api', '$stateParams', function (Api) {
                        return Api.all('users').getList();
                    }],
                    authorities: ['Api', function (Api) {
                        return Api.all('users').all('authorities').getList();
                    }]
                }
            })
            .state('user', {
                parent: 'users',
                url: '/{id:int}',
                data: {
                    roles: [],
                    pageTitle: 'user.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/user/user-detail.html',
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
