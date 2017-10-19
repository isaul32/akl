angular.module('app')
.config($stateProvider => $stateProvider
    .state('league', {
        parent: 'root',
        url: '/league',
        abstract: true
    })
    .state('league.state', {
        url: '/state',
        views: {
            'content@': {
                templateUrl: 'states/league/league.state.html',
                controller: ($scope, $sce, $templateRequest, API_URL) => {
                    $scope.options = {
                        src: 'https://akl.challonge.com/2017A/module?tab=finals&theme=4465'
                    };
                    const templateUrl = $sce.getTrustedResourceUrl(API_URL + '/challonge');

                    $templateRequest(templateUrl).then(template => {
                        $scope.scoreboard = template;
                    }, () => {
                        console.error("Cannot get scoreboard template");
                    });
                }
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('group');
                return $translate.refresh();
            }
        }
    })
    .state('league.final', {
        url: '/final',
        views: {
            'content@': {
                templateUrl: 'states/league/league.final.html',
                controller: ($scope, $rootScope, Principal, $translate, text) => {
                    Principal.identity().then(account => {
                        $scope.account = account;
                        $scope.isAuthenticated = Principal.isAuthenticated;
                    });

                    $scope.lang = $translate.use();
                    $rootScope.$on('$translateChangeSuccess', () => {
                        $scope.lang = $translate.use();
                    });

                    $scope.text = text.data;
                }
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('group');
                return $translate.refresh();
            },
            text: Api => Api.one('texts', 3).get()
        }
    })
    .state('league.afterparty', {
        url: '/afterparty',
        views: {
            'content@': {
                templateUrl: 'states/league/league.afterparty.html',
                controller: ($scope, $rootScope, Principal, $translate, text) => {
                    Principal.identity().then(account => {
                        $scope.account = account;
                        $scope.isAuthenticated = Principal.isAuthenticated;
                    });

                    $scope.lang = $translate.use();
                    $rootScope.$on('$translateChangeSuccess', () => {
                        $scope.lang = $translate.use();
                    });

                    $scope.text = text.data;
                }
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('group');
                return $translate.refresh();
            },
            text: Api => Api.one('texts', 4).get()
        }
        })
);
