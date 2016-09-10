angular.module('app')
.config($stateProvider => $stateProvider
    .state('reset', {
        abstract: true,
        parent: 'account'
    })
);
