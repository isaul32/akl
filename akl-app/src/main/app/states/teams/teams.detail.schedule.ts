interface Event {
    start: number;
    end: number;
}

angular.module('app')
.controller('TeamsDetailScheduleController', ($scope, $uibModalInstance, schedule, $translate, uiCalendarConfig) => {
    $scope.eventSources = [];
    $scope.events = [];
    $scope.selectedEvents = _.map(schedule.data, (event: Event) => {
        event.start = moment(event.start);
        event.end = moment(event.end);
        return event;
    });

    // 1 month = 336 * 2h
    /*for (let i = 0; i < 336; i++) {
        const ROUNDING = 60 * 60 * 1000;

        let start = moment();
        start = moment(Math.ceil((+start) / ROUNDING) * ROUNDING);
        if (start.get('hour') % 2 !== 0) {
            start.add(1, 'hours');
        }
        start.add(2 * i, 'hours');
        let end = moment(start).add(2, 'hours');

        const event = {
            start: start,
            end: end,
            className: 'calendar-event'
        };

        $scope.events.push(event);
    }
    $scope.eventSources.push($scope.events);*/

    $scope.select = (start, end, jsEvent, view) => {
        
        //uiCalendarConfig.calendars.teamScheduleCalendar.fullCalendar('updateEvent', event);
    };

    $scope.eventClick = (event, jsEvent) => {
        console.log(event);
        event.toggle = event.toggle !== true;
        angular.element(jsEvent.target).closest(".fc-time-grid-event").toggleClass('calendar-event-green', event.toggle);

        if (event.toggle) {
            $scope.selectedEvents.push({
                _id: event._id,
                start: event.start,
                end: event.end
            });
        } else {
            _.remove($scope.selectedEvents, {
                _id: event._id
            });
        }
    };

    $scope.uiConfig = {
        calendar: {
            locale: $translate.use(),
            editable: true,
            selectable: true,
            selectHelper: false,
            select: $scope.select,
            firstDay: 1,
            header: {
                left: 'title',
                center: '',
                right: 'today prev,next'
            },
            slotDuration: '00:30:00',
            scrollTime: '18:00:00',
            defaultView: 'agendaWeek',
            allDaySlot: false,
            eventClick: $scope.eventClick
        }
    };

    $scope.save = () => {
        _.assignIn(schedule.data, _.assign($scope.selectedEvents));
        schedule.data.post();
    };

    $scope.clear = () => {
        $uibModalInstance.dismiss('cancel');
    };
});
