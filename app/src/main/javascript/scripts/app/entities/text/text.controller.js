'use strict';

angular.module('aklApp')
    .controller('TextController', function ($scope, Text, TextSearch, ParseLinks) {
        $scope.texts = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Text.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.texts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Text.get({id: id}, function(result) {
                $scope.text = result;
                $('#deleteTextConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Text.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTextConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            TextSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.texts = result;
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
            $scope.text = {id: null};
        };
    });
