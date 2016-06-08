'use strict';

angular.module('aklApp')
.controller('GroupTeamsController', function ($scope, Api, teams, groups) {
    $scope.teams = teams.data;
    $scope.groups = groups.data;

    // remove duplicates
    _.forEach($scope.groups, function(group) {
        _.forEach(group.teams, function (t1) {
            _.remove($scope.teams, function(t2) {
                return t2.id === t1.id;
            });
        });
    });

    var saveChange = function (e, ui) {
        var senderId = Number(ui.sender[0].id);
        var targetId = Number(e.target.id);
        var sender;
        var target;

        // Save sender and target groups
        if (senderId !== 0 && targetId != 0) {
            sender = _.find($scope.groups, { id: senderId });
            sender.put().then(function () {
                target = _.find($scope.groups, { id: targetId });
                target.put();
            });
        } else if (senderId !== 0) {
            sender = _.find($scope.groups, { id: senderId });
            sender.put();
        } else if (targetId != 0) {
            target = _.find($scope.groups, { id: targetId });
            target.put();
        }
    };

    $scope.sortableOptions = {
        delay: 100,
        tolerance: "pointer",
        connectWith: ".group-container",
        start: function (e, ui) {
            ui.placeholder.height(ui.item.height());
        },
        receive: saveChange
    };
});
