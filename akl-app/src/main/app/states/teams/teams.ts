angular.module('app')
.config($stateProvider => $stateProvider
    .state('teams', {
        parent: 'root',
        url: '/teams',
        views: {
            'content@': {
                templateUrl: 'states/teams/teams.html',
                controller: 'TeamsController'
            }
        },
        params: {
            page: {
                value: '1',
                squash: true
            },
            season: {
                value: null,
                squash: true
            },
            filter: {
                value: null,
                squash: true
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('team');
                $translatePartialLoader.addPart('rank');
                return $translate.refresh();
            },
            teams: (Api, $stateParams) => {
                return Api.all('teams').getList({
                    season: $stateParams.season,
                    filter: $stateParams.filter,
                    page: $stateParams.page,
                    per_page: 20
                });
            },
            seasons: (Api) => {
                return Api.all('seasons').getList();
            }
        }
    })
    .state('teams.new', {
        url: '/new',
        data: {
            roles: ['ROLE_USER']
        },
        onEnter: ($stateParams, $state, $uibModal) => {
            $uibModal.open({
                templateUrl: 'states/teams/teams.dialog.html',
                controller: 'TeamsDialogController',
                size: 'lg',
                resolve: {
                    entity: () => {
                        return { tag: null, name: null, imageUrl: null, rank: null, description: null, id: null };
                    }
                }
            }).result.then(() => {
                $state.go('team', null, { reload: true });
            }, () => {
                $state.go('team');
            })
        }
    })
    .state('teams.detail', {
        url: '/{id}',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/teams/teams.detail.html',
                controller: 'TeamsDetailController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('team');
                $translatePartialLoader.addPart('rank');
                return $translate.refresh();
            },
            team: ($stateParams, Api) => {
                return Api.one('teams', $stateParams.id).get();
            }
        }
    })
    .state('teams.detail.edit', {
        url: '/{id}/edit',
        data: {
            roles: ['ROLE_USER']
        },
        onEnter: ($stateParams, $state, $uibModal) => {
            $uibModal.open({
                templateUrl: 'states/teams/teams.dialog.html',
                controller: 'TeamsDialogController',
                size: 'lg',
                resolve: {
                    entity: (Team) => {
                        return Team.get({ id : $stateParams.id });
                    }
                }
            }).result.then(() => {
                $state.go('teams.detail', null, { reload: true });
            }, function () {
                $state.go('^');
            })
        }
    })
    .state('teams.detail.accept', {
        url: '/member/{userId}/accept',
        data: {
            roles: ['ROLE_USER']
        },
        onEnter: ($stateParams, $state, $uibModal) => {
            $uibModal.open({
                templateUrl: 'states/teams/teams.member.dialog.html',
                controller: 'TeamsMemberDialogController',
                size: 'lg',
                resolve: {
                    team: Team => Team.get({ id: $stateParams.id })
                }
            }).result.then(() => {
                $state.go('teams.detail', { id: $stateParams.id }, { reload: true });
            }, () => {
                $state.go('^');
            });
        }
    })
    .state('teams.detail.schedule', {
        url: '/schedule',
        data: {
            roles: ['ROLE_USER']
        },
        onEnter: ($state, $stateParams, $uibModal, Api, team) => {
            $uibModal.open({
                templateUrl: 'states/teams/teams.detail.schedule.html',
                controller: 'TeamsDetailScheduleController',
                size: 'lg',
                resolve: {
                    schedule: () => {
                        return team.data.all('schedule').getList();
                    }
                }
            }).result.then(() => {
                $state.go('^', null, {
                    reload: true
                });
            }, () => {
                $state.go('^');
            })
        }
    })
);
