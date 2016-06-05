'use strict';

angular.module('aklApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('gameServer', {
                parent: 'entity',
                url: '/gameServers',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/gameServer/gameServers.html',
                        controller: 'GameServerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('gameServer');
                        $translatePartialLoader.addPart('gameServerState');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('gameServer.detail', {
                parent: 'entity',
                url: '/gameServer/{id}',
                data: {
                    authorities: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/gameServer/gameServer-detail.html',
                        controller: 'GameServerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('gameServer');
                        $translatePartialLoader.addPart('gameServerState');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'GameServer', function($stateParams, GameServer) {
                        return GameServer.get({id : $stateParams.id});
                    }]
                }
            })
            .state('gameServer.new', {
                parent: 'gameServer',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/gameServer/gameServer-dialog.html',
                        controller: 'GameServerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    server_ip: null,
                                    rcon_ip: null,
                                    rcon_password: null,
                                    server_port: null,
                                    rcon_port: null,
                                    state: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('gameServer', null, { reload: true });
                    }, function() {
                        $state.go('gameServer');
                    })
                }]
            })
            .state('gameServer.edit', {
                parent: 'gameServer',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/gameServer/gameServer-dialog.html',
                        controller: 'GameServerDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['GameServer', function(GameServer) {
                                return GameServer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('gameServer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('gameServer.delete', {
                parent: 'gameServer',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/gameServer/gameServer-delete-dialog.html',
                        controller: 'GameServerDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['GameServer', function(GameServer) {
                                return GameServer.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('gameServer', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
