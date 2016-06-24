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
                        group: function () {
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
                        group: function () {
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
        .state('state', {
            parent: 'league',
            url: '/state',
            data: {
                roles: []
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/group/state.html',
                    controller: function ($scope) {
                        $scope.options = {
                            src: 'http://akl.challonge.com/2016/module?tab=groups&theme=4465'
                        }
                    }
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('group');
                    return $translate.refresh();
                }]
            }
        })
        .state('final', {
            parent: 'league',
            url: '/final',
            data: {
                roles: []
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/group/final.html',
                    controller: function ($scope, $rootScope, Principal, $translate, text) {
                        Principal.identity().then(function (account) {
                            $scope.account = account;
                            $scope.isAuthenticated = Principal.isAuthenticated;
                        });

                        $scope.lang = $translate.use();
                        $rootScope.$on('$translateChangeSuccess', function () {
                            $scope.lang = $translate.use();
                        });

                        $scope.text = text.data;
                    }
                }
            },
            resolve: {
                translatePartialLoader: function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('group');
                    return $translate.refresh();
                },
                text: function (Api) {
                    return Api.one('texts', 3).get();
                }
            }
        })
        .state('afterparty', {
            parent: 'league',
            url: '/afterparty',
            data: {
                roles: []
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/group/afterparty.html',
                    controller: function ($scope, $rootScope, Principal, $translate, text) {
                        Principal.identity().then(function (account) {
                            $scope.account = account;
                            $scope.isAuthenticated = Principal.isAuthenticated;
                        });

                        $scope.lang = $translate.use();
                        $rootScope.$on('$translateChangeSuccess', function () {
                            $scope.lang = $translate.use();
                        });

                        $scope.text = text.data;
                    }
                }
            },
            resolve: {
                translatePartialLoader: function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('group');
                    return $translate.refresh();
                },
                text: function (Api) {
                    return Api.one('texts', 4).get();
                }
            }
        })
});
