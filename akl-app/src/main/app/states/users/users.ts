angular.module('app')
.config($stateProvider => $stateProvider
    .state('users', {
        abstract: true,
        parent: 'root'
    })
    .state('users.list', {
        url: '/users',
        data: {
            roles: ['ROLE_ADMIN']
        },
        views: {
            'content@': {
                templateUrl: 'states/users/users.list.html',
                controller: 'UsersListController'
            }
        },
        params: {
            page: {
                value: '1',
                squash: true
            },
            filter: {
                value: null,
                squash: true
            }
        },
        resolve: {
            translatePartialLoader:  ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('user');
                return $translate.refresh();
            },
            users: (Api, $stateParams) => {
                return Api.all('users').getList({
                    page: $stateParams.page,
                    per_page: 20,
                    filter: $stateParams.filter
                });
            },
            authorities: Api => {
                return Api.all('users').all('authorities').getList();
            }
        }
    })
    .state('users.detail', {
        url: '/users/{userId}',
        views: {
            'content@': {
                templateUrl: 'states/users/users.detail.html',
                controller: ($scope, $rootScope, user, steamUser) => {
                    $scope.user = user.data;
                    $scope.steamUser = _.result(steamUser, 'data.response.players[0]');
                }
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('user');
                $translatePartialLoader.addPart('rank');
                return $translate.refresh();
            },
            user: ($stateParams, Api) => {
                return Api.one('users', $stateParams.userId).get();
            },
            steamUser: (Api, user) => {
                if (user.data.communityId !== null) {
                    return Api.all('steam').all('user').get(user.data.communityId);
                }
            }
        }
    })
);
