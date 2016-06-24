angular.module('app')
.directive('showValidation', () => {
    return {
        restrict: 'A',
        require: 'form',
        link: (scope, element) => {
            element.find('.form-group').each(() => {
                let $formGroup = $(this);
                let $inputs = $formGroup.find('input[ng-model],textarea[ng-model],select[ng-model]');

                if ($inputs.length > 0) {
                    $inputs.each(() => {
                        let $input = $(this);
                        scope.$watch(() => {
                            return $input.hasClass('ng-invalid') && $input.hasClass('ng-dirty');
                        }, isInvalid => {
                            $formGroup.toggleClass('has-error', isInvalid);
                        });
                    });
                }
            });
        }
    };
});
