'use strict';

angular.module('aklApp').controller('TeamMemberDialogController',
     ['$scope', '$stateParams', '$uibModalInstance', 'Team', 'team',
     function($scope, $stateParams, $uibModalInstance, Team, team) {
         $scope.team = team;

         $scope.save = function () {
             Team.acceptRequest({id: $stateParams.id,
                                 userId: $stateParams.userId,
                                 role: $scope.role}).$promise
             .then(function() {
                 $uibModalInstance.close();
             });
         };

         $scope.clear = function() {
             $uibModalInstance.dismiss('cancel');
         };
}]);
