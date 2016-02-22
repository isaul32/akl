'use strict';

angular.module('aklApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('localizedText', {
                parent: 'entity',
                url: '/localizedTexts',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'aklApp.localizedText.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/localizedText/localizedTexts.html',
                        controller: 'LocalizedTextController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('localizedText');
                        $translatePartialLoader.addPart('language');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('localizedText.detail', {
                parent: 'entity',
                url: '/localizedText/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'aklApp.localizedText.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/localizedText/localizedText-detail.html',
                        controller: 'LocalizedTextDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('localizedText');
                        $translatePartialLoader.addPart('language');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'LocalizedText', function($stateParams, LocalizedText) {
                        return LocalizedText.get({id : $stateParams.id});
                    }]
                }
            })
            .state('localizedText.new', {
                parent: 'localizedText',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/localizedText/localizedText-dialog.html',
                        controller: 'LocalizedTextDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {language: null, text: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('localizedText', null, { reload: true });
                    }, function() {
                        $state.go('localizedText');
                    })
                }]
            })
            .state('localizedText.edit', {
                parent: 'localizedText',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/localizedText/localizedText-dialog.html',
                        controller: 'LocalizedTextDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['LocalizedText', function(LocalizedText) {
                                return LocalizedText.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('localizedText', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
