'use strict';

angular.module('aklApp')
    .controller('ScheduleController', function ($scope) {

        $scope.eventSources = [{
            url: "http://www.google.com/calendar/feeds/usa__en%40holiday.calendar.google.com/public/basic"
        }];

        $scope.eventRender = function(event, element, view) {
            /*element.attr({'tooltip': event.title,
                'tooltip-append-to-body': true});
            $compile(element)($scope);*/
        };

        $scope.uiConfig = {
            calendar: {
                editable: false,
                //theme: true,
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
