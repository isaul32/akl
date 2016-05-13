'use strict';

angular.module('aklApp')
    .controller('ScheduleController', function ($scope) {

        // Some test data
        /*var date = new Date();
        var d = date.getDate();
        var m = date.getMonth();
        var y = date.getFullYear();

        $scope.events = [
            {title: 'All Day Event',start: new Date(y, m, 1)},
            {title: 'Long Event',start: new Date(y, m, d - 5),end: new Date(y, m, d - 2)},
            {id: 999,title: 'Repeating Event',start: new Date(y, m, d - 3, 16, 0),allDay: false},
            {id: 999,title: 'Repeating Event',start: new Date(y, m, d + 4, 16, 0),allDay: false},
            {title: 'Birthday Party',start: new Date(y, m, d + 1, 19, 0),end: new Date(y, m, d + 1, 22, 30),allDay: false},
            {title: 'Click for Google',start: new Date(y, m, 28),end: new Date(y, m, 29),url: 'http://google.com/'}
        ];*/
        $scope.events = [
            {
                title: 'Ilmoittautuminen alkaa',
                start: new Date(2016, 5 - 1, 15)
            },
            {
                title: 'Ilmoittautuminen päättyy',
                start: new Date(2016, 6 - 1, 15)
            }
        ];

        $scope.eventSources = [$scope.events];

        $scope.eventRender = function(event, element, view) {
            /*element.attr({'tooltip': event.title,
                'tooltip-append-to-body': true});
            $compile(element)($scope);*/
        };

        $scope.uiConfig = {
            calendar: {
                editable: false,
                firstDay: 1,
                header: {
                    left: 'title',
                    center: '',
                    right: 'today prev,next'
                },
                eventClick: $scope.alertOnEventClick,
                eventDrop: $scope.alertOnDrop,
                eventResize: $scope.alertOnResize,
                eventRender: $scope.eventRender
            }
        };

    });
