'use strict';

angular.module('aklApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('entity', {
                abstract: true,
                parent: 'site'
            })
            .state('league', {
                abstract: true,
                parent: 'site'
            })
        ;
    });
