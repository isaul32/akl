angular.module('app')
.controller('ScheduleController', ($scope, SERVICE_URL, $compile, $translate) => {
    $scope.gcalEvents = {
        googleCalendarApiKey: 'AIzaSyBrwxD8d4zhA7kDMXt-KH-prQvyrSRnZXk',
        googleCalendarId: 'jk1adevi3b3jcm2oobcea3ldf8@group.calendar.google.com'
    };

    $scope.eventSources = [
        $scope.gcalEvents
    ];

    $scope.eventRender = (event, element) => {
        element.attr({'tooltip': event.title,
            'tooltip-append-to-body': true});
        $compile(element)($scope);
    };

    $scope.eventClick = event => {
        if (event.url) {
            window.open(event.url);
            return false;
        }
    };

    $scope.uiConfig = {
        calendar: {
            lang: $translate.use(),
            editable: false,
            displayEventEnd: true,
            firstDay: 1,
            header: {
                left: 'title',
                center: '',
                right: 'today prev,next'
            },
            eventClick: $scope.eventClick,
            eventRender: $scope.eventRender
        }
    };
});
