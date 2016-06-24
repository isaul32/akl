angular.module('app')
.controller('TeamsMemberDialogController', ($scope, $stateParams, $uibModalInstance, Team, team) => {
    $scope.team = team;

     $scope.save = () => {
         Team.acceptRequest({id: $stateParams.id,
                             userId: $stateParams.userId,
                             role: $scope.role}
         ).$promise.then(() => {
             $uibModalInstance.close();
         });
     };

     $scope.clear = () => {
         $uibModalInstance.dismiss('cancel');
     };
});
