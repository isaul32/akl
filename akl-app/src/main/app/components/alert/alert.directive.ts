angular.module('app')
.directive('aklAlert', AlertService => {
    return {
        restrict: 'E',
        template:
        '<div class="alerts">' +
        '<uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="alert.close()"  class="alert">' +
        '<div class="alert alert-success">' +
        '{{ alert.msg }}' +
        '</div>' +
        '</uib-alert>' +
        '</div>',
        controller: ['$scope',
            function($scope) {
                $scope.alerts = AlertService.get();
                $scope.$on('$destroy', function () {
                    $scope.alerts = [];
                });
            }
        ]
    }
})
.directive('aklAlertError', (AlertService, $rootScope, $translate) => {
    return {
        restrict: 'E',
        template:
        '<div class="alerts">' +
        '<uib-alert ng-repeat="alert in alerts" type="{{alert.type}}" close="alert.close()" >' +
        '<div class="alert alert-danger">' +
        '{{ alert.msg }}' +
        '</div>' +
        '</uib-alert>' +
        '</div>',
        controller: ['$scope',
            function($scope) {
                $scope.alerts = AlertService.get();

                let cleanHttpErrorListener = $rootScope.$on('aklApp.httpError', function (event, httpResponse) {
                    let i;
                    event.stopPropagation();

                    switch (httpResponse.status) {
                        // connection refused, server not reachable
                        case 0:
                            addErrorAlert("Server not reachable",'error.serverNotReachable');
                            break;

                        case 400:
                            if (httpResponse.data && httpResponse.data.fieldErrors) {
                                for (i = 0; i < httpResponse.data.fieldErrors.length; i++) {
                                    let fieldError = httpResponse.data.fieldErrors[i];
                                    // convert 'something[14].other[4].id' to 'something[].other[].id' so translations can be written to it
                                    let convertedField = fieldError.field.replace(/\[\d*\]/g, "[]");
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
                    if(cleanHttpErrorListener !== undefined && cleanHttpErrorListener !== null){
                        cleanHttpErrorListener();
                    }
                });

                let addErrorAlert = (message, key?, data?) => {
                    key = key && key != null ? key : message;
                    AlertService.error(key, data);
                };
            }
        ]
    }
});
