'use strict';

angular.module('aklApp')
.config(function ($stateProvider) {
    $stateProvider
        .state('group', {
            parent: 'league',
            url: '/groups',
            data: {
                roles: []
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/group/groups.html',
                    controller: 'GroupController'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('group');
                    $translatePartialLoader.addPart('team');
                    return $translate.refresh();
                }],
                groups: ['Api', function (Api) {
                    return Api.all('groups').getList();
                }]
            }
        })
        .state('group.new', {
            parent: 'group',
            url: '/new',
            data: {
                roles: ['ROLE_ADMIN']
            },
            onEnter: ['$state', '$uibModal', function ($state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/group/group-dialog.html',
                    controller: 'GroupDialogController',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                data: {}
                            };
                        }
                    }
                }).result.then(function () {
                    $state.go('^', null, {
                        reload: true
                    });
                }, function() {
                    $state.go('^');
                })
            }]
        })
        .state('group.edit', {
            parent: 'group',
            url: '/{id}/edit',
            data: {
                roles: ['ROLE_ADMIN']
            },
            onEnter: ['$state', '$stateParams', '$uibModal', 'Api', function ($state, $stateParams, $uibModal, Api) {
                $uibModal.open({
                    templateUrl: 'scripts/app/entities/group/group-dialog.html',
                    controller: 'GroupDialogController',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return Api.one('groups', $stateParams.id).get();
                        }
                    }
                }).result.then(function () {
                    $state.go('^', null, {
                        reload: true
                    });
                }, function() {
                    $state.go('^');
                })
            }]
        })
        .state('group.teams', {
            parent: 'group',
            url: '/teams',
            data: {
                roles: ['ROLE_ADMIN']
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/group/group-teams.html',
                    controller: 'GroupTeamsController'
                }
            },
            resolve: {
                teams: ['Api', function (Api) {
                    return Api.all('teams').getList({
                        per_page: 100
                    });
                }]
            }
        })
});
