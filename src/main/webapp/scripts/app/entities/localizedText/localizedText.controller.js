'use strict';

angular.module('aklApp')
    .controller('LocalizedTextController', function ($scope, LocalizedText, LocalizedTextSearch, ParseLinks) {
        $scope.localizedTexts = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            LocalizedText.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.localizedTexts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            LocalizedText.get({id: id}, function(result) {
                $scope.localizedText = result;
                $('#deleteLocalizedTextConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            LocalizedText.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteLocalizedTextConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            LocalizedTextSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.localizedTexts = result;
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
            $scope.localizedText = {language: null, text: null, id: null};
        };
    });
