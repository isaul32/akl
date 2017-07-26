angular.module('app')
.config($stateProvider => $stateProvider
    .state('groups', {
        parent: 'league',
        url: '/groups',
        views: {
            'content@': {
                templateUrl: 'states/league/groups/groups.html',
                controller: ($scope, groups) => {
                    $scope.groups = groups.data;

                    $scope.createTournament = () => {
                        $scope.groups.all('tournament').post();
                    };
                }
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('group');
                $translatePartialLoader.addPart('team');
                return $translate.refresh();
            },
            groups: Api => Api.all('groups').getList()
        }
    })
    .state('groups.new', {
        url: '/new',
        data: {
            roles: ['ROLE_ADMIN']
        },
        onEnter: ($state, $uibModal) => {
            $uibModal.open({
                templateUrl: 'states/league/groups/groups.dialog.html',
                controller: 'GroupsDialogController',
                size: 'lg',
                resolve: {
                    group: () => {
                        return {
                            data: {}
                        };
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
    .state('groups.edit', {
        url: '/{id}/edit',
        data: {
            roles: ['ROLE_ADMIN']
        },
        onEnter: ($state, $stateParams, $uibModal, Api) => {
            $uibModal.open({
                templateUrl: 'states/league/groups/groups.dialog.html',
                controller: 'GroupsDialogController',
                size: 'lg',
                resolve: {
                    group: (Api) => Api.one('groups', $stateParams.id).get()
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
    .state('groups.teams', {
        url: '/teams',
        data: {
            roles: ['ROLE_ADMIN']
        },
        views: {
            'content@': {
                templateUrl: 'states/league/groups/groups.teams.html',
                controller: 'GroupsTeamsController'
            }
        },
        resolve: {
            teams: (Api) => Api.all('teams').getList({ per_page: 100 })
        }
    })
);
