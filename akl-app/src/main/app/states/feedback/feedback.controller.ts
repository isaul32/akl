angular.module('app')
.controller('FeedbackController', ($scope, $rootScope, Principal, $translate, API_URL, Api, RECAPTCHA_SITE_KEY,
                                   $state, AlertService) => {
    $scope.feedback = {
        message: '',
        sender: ''
    };
    $scope.recaptchaKey = RECAPTCHA_SITE_KEY;

    $scope.save = () => {
        Api.all('feedback').post($scope.feedback).then(() => {
            AlertService.success('feedback.messages.success', {});
            $state.reload();
        });
    };
});
