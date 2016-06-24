angular.module('app')
.config($stateProvider => $stateProvider
    .state('admin', {
        abstract: true,
        parent: 'root'
    })
);
