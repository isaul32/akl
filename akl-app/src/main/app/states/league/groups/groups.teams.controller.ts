interface Team {
    id: number
}

angular.module('app')
.controller('GroupsTeamsController', ($scope, Api, teams, groups) => {
    $scope.teams = teams.data;
    $scope.groups = groups.data;

    // remove duplicates
    _.forEach($scope.groups, group => {
        _.forEach(group.teams, (t1: Team) => {
            _.remove($scope.teams, (t2: Team) => {
                return t2.id === t1.id;
            });
        });
    });

    let saveChange = (e, ui) => {
        let senderId = Number(ui.sender[0].id);
        let targetId = Number(e.target.id);
        let sender;
        let target;

        // Save sender and target groups
        if (senderId !== 0 && targetId != 0) {
            sender = _.find($scope.groups, { id: senderId });
            sender.put().then(() => {
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
        start: (e, ui) => {
            ui.placeholder.height(ui.item.height());
        },
        receive: saveChange
    };
});
