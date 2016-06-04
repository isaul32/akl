'use strict';

angular.module('aklApp')
    .controller('ScheduleController', function ($scope, SERVICE_URL, $compile) {

        $scope.gcalEvents = {
            googleCalendarApiKey: 'AIzaSyBrwxD8d4zhA7kDMXt-KH-prQvyrSRnZXk',
            googleCalendarId: 'jk1adevi3b3jcm2oobcea3ldf8@group.calendar.google.com'
        };

        /*$scope.eventSource = {
            url: SERVICE_URL + "/api/calendar"
        };*/

        $scope.eventSources = [
            $scope.gcalEvents
            /*$scope.eventSource*/
        ];

        $scope.eventRender = function (event, element, view) {
            element.attr({'tooltip': event.title,
                'tooltip-append-to-body': true});
            $compile(element)($scope);
        };

        $scope.eventClick = function (event) {
            if (event.url) {
                window.open(event.url);
                return false;
            }
        };

        $scope.uiConfig = {
            calendar: {
                editable: false,
                firstDay: 1,
                displayEventEnd: true,
                timeFormat: 'H(:mm)',
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
