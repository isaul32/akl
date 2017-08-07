angular.module('app')
.directive('aklAlert', AlertService => {
    return {
        restrict: 'E',
        templateUrl: 'components/alert/alert.directive.html',
        controller: ($scope, AlertService, $rootScope, $translate) => {
            $scope.alerts = AlertService.get();

            let cleanHttpErrorListener = $rootScope.$on('aklApp.httpError', function (event, httpResponse) {
                let i;
                event.stopPropagation();

                switch (httpResponse.status) {
                    // connection refused, server not reachable
                    case 0:
                        addErrorAlert("Server not reachable", 'error.serverNotReachable');
                        break;

                    case 400:
                        if (httpResponse.data && httpResponse.data.fieldErrors) {
                            for (i = 0; i < httpResponse.data.fieldErrors.length; i++) {
                                let fieldError = httpResponse.data.fieldErrors[i];
                                let convertedField = fieldError.field.replace(/\[\d*]/g, "[]");
                                let fieldName = $translate.instant('aklApp.' + fieldError.objectName + '.' + convertedField);
                                addErrorAlert('Field ' + fieldName + ' cannot be empty', 'error.' + fieldError.message, {fieldName: fieldName});
                            }
                        } else if (httpResponse.data && httpResponse.data.message) {
                            addErrorAlert(httpResponse.data.message, httpResponse.data.message, httpResponse.data);
                        } else {
                            addErrorAlert(httpResponse.data);
                        }
                        break;

                    default:
                        if (httpResponse.data && httpResponse.data.message) {
                            addErrorAlert(httpResponse.data.message);
                        } else {
                            addErrorAlert(JSON.stringify(httpResponse));
                        }
                }
            });

            $scope.$on('$destroy', function () {
                if (cleanHttpErrorListener !== undefined && cleanHttpErrorListener !== null) {
                    cleanHttpErrorListener();
                }
            });

            let addErrorAlert = (message, key?, data?) => {
                key = key && key != null ? key : message;
                AlertService.error(key, data);
            };
        }
    }
});
