angular.module('app')
.config($stateProvider => $stateProvider
    .state('schedule', {
        parent: 'root',
        url: '/schedule',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/schedule/schedule.html',
                controller: 'ScheduleController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('global');
                return $translate.refresh();
            }
        }
    })
);
