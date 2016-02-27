'use strict';

angular.module('aklApp')
    .controller('GameServerController', function ($scope, $state, GameServer, GameServerSearch) {

        $scope.gameServers = [];
        $scope.loadAll = function() {
            GameServer.query(function(result) {
               $scope.gameServers = result;
            });
        };
        $scope.loadAll();


        $scope.search = function () {
            GameServerSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.gameServers = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.gameServer = {
                name: null,
                server_ip: null,
                rcon_ip: null,
                rcon_password: null,
                server_port: null,
                rcon_port: null,
                state: null,
                id: null
            };
        };
    });
