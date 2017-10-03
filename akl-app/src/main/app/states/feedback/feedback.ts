angular.module('app')
.config($stateProvider => $stateProvider
    .state('feedback', {
        parent: 'root',
        url: '/feedback',
        data: {
            roles: []
        },
        views: {
            'content@': {
                templateUrl: 'states/feedback/feedback.html',
                controller: 'FeedbackController'
            }
        },
        resolve: {
            mainTranslatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('feedback');
                $translatePartialLoader.addPart('global');
                return $translate.refresh();
            }
        }
    })
);
