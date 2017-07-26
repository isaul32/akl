angular.module('app')
.controller('ScheduleController', ($scope, SERVICE_URL, $compile, $translate) => {
    $scope.gcalEvents = {
        googleCalendarApiKey: 'AIzaSyBrwxD8d4zhA7kDMXt-KH-prQvyrSRnZXk',
        googleCalendarId: 'jk1adevi3b3jcm2oobcea3ldf8@group.calendar.google.com'
    };

    $scope.eventSources = [
        $scope.gcalEvents,
        {
            url: 'http://titeen.it/akl/2016/reserver/ok.json',
            color: '#31302B',
            textColor: 'white'
        }
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
            locale: $translate.use(),
            editable: false,
            displayEventEnd: true,
            firstDay: 1,
            defaultView: 'agendaWeek',
            allDaySlot: false,
            slotDuration: '00:30:00',
            scrollTime: '18:00:00',
            header: {
                left: 'title',
                center: 'agendaWeek,month',
                right: 'today prev,next'
            },
            eventClick: $scope.eventClick,
            eventRender: $scope.eventRender
        }
    };
});
