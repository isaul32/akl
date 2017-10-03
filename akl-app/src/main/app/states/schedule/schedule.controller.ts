angular.module('app')
.controller('ScheduleController', ($scope, SERVICE_URL, $compile, $translate, $locale) => {
    $scope.eventSources = [
        {
            googleCalendarId: 'jk1adevi3b3jcm2oobcea3ldf8@group.calendar.google.com',
            googleCalendarApiKey: 'AIzaSyBrwxD8d4zhA7kDMXt-KH-prQvyrSRnZXk',
            color: '#006052',
            textColor: 'white'
        },
        {
            url: 'https://akl.tite.fi/2017/reserver/ok.json',
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
            defaultView: 'month',
            allDaySlot: true,
            slotDuration: '00:30:00',
            scrollTime: '18:00:00',
            header: {
                left: 'title',
                center: 'agendaDay,agendaWeek,month',
                right: 'today prev,next'
            },
            eventClick: $scope.eventClick,
            eventRender: $scope.eventRender,
        }
    };
});
