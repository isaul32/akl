'use strict';

angular.module('aklApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('team', {
                parent: 'entity',
                url: '/team',
                data: {
                    roles: [],
                    pageTitle: 'aklApp.team.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/team/teams.html',
                        controller: 'TeamController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('team');
                        $translatePartialLoader.addPart('rank');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    currentTeam: ['AccountTeam', function(AccountTeam) {
                        return AccountTeam.team();
                    }]
                }
            })
            .state('team.new', {
                parent: 'team',
                url: '/new',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/team/team-dialog.html',
                        controller: 'TeamDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {tag: null, name: null, imageUrl: null, rank: null, description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('team', null, { reload: true });
                    }, function() {
                        $state.go('team');
                    })
                }]
            })
            .state('team.detail', {
                parent: 'team',
                url: '/team/{id:int}',
                data: {
                    roles: [],
                    pageTitle: 'aklApp.team.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/team/team-detail.html',
                        controller: 'TeamDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('team');
                        $translatePartialLoader.addPart('rank');
                        return $translate.refresh();
                    }],
                    team: ['$stateParams', 'Api', function($stateParams, Api) {
                        return Api.one('teams', $stateParams.id).get();
                    }],
                    requests: ['$stateParams', 'Principal', 'Team', function($stateParams, Principal, Team) {
                        return Principal.identity().then(function() {
                            return Principal.isInRole('ROLE_ADMIN');
                        }).then(function(isAdmin) {
                            if (isAdmin) {
                                return Team.requests({id: $stateParams.id});
                            }

                            return null;
                        });
                    }]
                }
            })
            .state('team.edit', {
                parent: 'team',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/team/team-dialog.html',
                        controller: 'TeamDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Team', function(Team) {
                                return Team.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('team', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('team.detail.accept', {
                parent: 'team.detail',
                url: '/member/{userId}/accept',
                data: {
                    roles: ['ROLE_USER']
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/team/team-member-dialog.html',
                        controller: 'TeamMemberDialogController',
                        size: 'lg',
                        resolve: {
                            team: ['Team', function(Team) {
                                return Team.get({id: $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('team.detail', {id: $stateParams.id}, {reload: true});
                    }, function() {
                        $state.go('^');
                    });
                }]
            });
    });
