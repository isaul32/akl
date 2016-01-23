'use strict';

angular.module('aklApp')
    .controller('TeamController', function ($scope, Team, TeamSearch, ParseLinks) {
        $scope.teams = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Team.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.teams = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Team.get({id: id}, function(result) {
                $scope.team = result;
                $('#deleteTeamConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Team.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTeamConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TeamSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.teams = result;
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
            $scope.team = {tag: null, name: null, imageUrl: null, rank: null, description: null, id: null};
        };
    });
