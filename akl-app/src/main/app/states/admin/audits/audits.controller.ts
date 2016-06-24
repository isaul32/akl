angular.module('app')
    .controller('AuditsController', ($scope, $filter, AuditsService) => {
        $scope.onChangeDate = () => {
            let dateFormat = 'yyyy-MM-dd';
            let fromDate = $filter('date')($scope.fromDate, dateFormat);
            let toDate = $filter('date')($scope.toDate, dateFormat);

            AuditsService.findByDates(fromDate, toDate).then(data => {
                $scope.audits = data;
            });
        };

        // Date picker configuration
        $scope.today = () => {
            // Today + 1 day - needed if the current day must be included
            let today = new Date();
            $scope.toDate = new Date(today.getFullYear(), today.getMonth(), today.getDate() + 1);
        };

        $scope.previousMonth = () => {
            let fromDate = new Date();
            if (fromDate.getMonth() === 0) {
                fromDate = new Date(fromDate.getFullYear() - 1, 0, fromDate.getDate());
            } else {
                fromDate = new Date(fromDate.getFullYear(), fromDate.getMonth() - 1, fromDate.getDate());
            }

            $scope.fromDate = fromDate;
        };

        $scope.today();
        $scope.previousMonth();
        $scope.onChangeDate();
    });
