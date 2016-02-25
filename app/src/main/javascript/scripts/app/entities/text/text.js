'use strict';

angular.module('aklApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('text', {
                parent: 'entity',
                url: '/texts',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'aklApp.text.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/text/texts.html',
                        controller: 'TextController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('text');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('text.detail', {
                parent: 'entity',
                url: '/text/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'aklApp.text.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/text/text-detail.html',
                        controller: 'TextDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('text');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Text', function($stateParams, Text) {
                        return Text.get({id : $stateParams.id});
                    }]
                }
            })
            .state('text.new', {
                parent: 'text',
                url: '/new',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/text/text-dialog.html',
                        controller: 'TextDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('text', null, { reload: true });
                    }, function() {
                        $state.go('text');
                    })
                }]
            })
            .state('text.edit', {
                parent: 'text',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/text/text-dialog.html',
                        controller: 'TextDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Text', function(Text) {
                                return Text.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('text', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
