angular.module('app')
.config($stateProvider => $stateProvider
    .state('account', {
        abstract: true,
        parent: 'root'
    })
);
