angular.module('app')
.directive('minbytes', () => {
    let endsWith = (suffix, str) => {
        return str.indexOf(suffix, str.length - suffix.length) !== -1;
    };

    let paddingSize = base64String => {
        if (endsWith('==', base64String)) {
            return 2;
        }
        if (endsWith('=', base64String)) {
            return 1;
        }
        return 0;
    };

    let numberOfBytes = base64String => {
        return base64String.length / 4 * 3 - paddingSize(base64String);
    };

    return {
        restrict: 'A',
        require: '?ngModel',
        link: (scope, element, attrs: any, ngModel: any) => {
            if (!ngModel) return;

            ngModel.$validators.minbytes = modelValue => {
                return ngModel.$isEmpty(modelValue) || numberOfBytes(modelValue) >= attrs.minbytes;
            };
        }
    };
});
