angular.module('app')
.controller('TrackerController', ($scope, $cookies, $http, Tracker) => {
    $scope.activities = [];
    Tracker.receive().then(null, null, activity => {
        showActivity(activity);
    });

    let showActivity = (activity) => {
        let existingActivity = false;
        for (let index = 0; index < $scope.activities.length; index++) {
            if($scope.activities[index].sessionId == activity.sessionId) {
                existingActivity = true;
                if (activity.page == 'logout') {
                    $scope.activities.splice(index, 1);
                } else {
                    $scope.activities[index] = activity;
                }
            }
        }
        if (!existingActivity && (activity.page != 'logout')) {
            $scope.activities.push(activity);
        }
    };
});
