'use strict';

angular.module('aklApp')
.controller('TeamScheduleDialogController', function($scope, $uibModalInstance, schedule, $translate) {
    $scope.eventSources = [];
    $scope.events = [];

    function addHours(d, h) {
        return d.setHours(d.getHours() + h);
    }

    $scope.currentDate = new Date();

    // 1 month = 336 * 2h
    for (var i = 0; i < 336; i++) {
        var startDate = new Date();
        startDate.setHours(Math.ceil(startDate.getHours()), 0, 0, 0);
        if (startDate.getHours() % 2 !== 0) {
            startDate.setHours(startDate.getHours() + 1);
        }
        addHours(startDate, i * 2);

        var endDate = new Date(startDate);
        addHours(endDate, 2);

        $scope.events.push({
            start: startDate,
            end: endDate,
            className: 'calendar-event'
        });
    }

    $scope.eventSources.push($scope.events);

    $scope.selectedEvents = [];

    $scope.eventClick = function (event, jsEvent) {
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
            lang: $translate.use(),
            editable: false,
            firstDay: 1,
            header: {
                left: 'title',
                center: '',
                right: 'today prev,next'
            },
            slotDuration: '02:00:00',
            defaultView: 'agendaWeek',
            allDaySlot: false,
            height: 'auto',
            eventClick: $scope.eventClick
        }
    };

    $scope.save = function () {
        _.assignIn(schedule.data, _.assign($scope.selectedEvents));
        schedule.data.post();
    };

    $scope.clear = function () {
        $uibModalInstance.dismiss('cancel');
    };
});
