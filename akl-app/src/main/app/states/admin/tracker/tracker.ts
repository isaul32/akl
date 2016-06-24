angular.module('app')
.config($stateProvider => $stateProvider
    .state('tracker', {
        parent: 'admin',
        url: '/tracker',
        data: {
            roles: ['ROLE_ADMIN']
        },
        views: {
            'content@': {
                templateUrl: 'states/admin/tracker/tracker.html',
                controller: 'TrackerController'
            }
        },
        resolve: {
            mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('tracker');
                return $translate.refresh();
            }]
        },
        onEnter: Tracker => {
            Tracker.subscribe();
        },
        onExit: Tracker => {
            Tracker.unsubscribe();
        },
    })
);
