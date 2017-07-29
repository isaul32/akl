angular.module('app')
.controller('HealthController', ($scope, MonitoringService, $uibModal) => {
    $scope.updatingHealth = true;
    $scope.separator = '.';

    $scope.refresh = function () {
        $scope.updatingHealth = true;
        MonitoringService.checkHealth().then(function (response) {
            $scope.healthData = $scope.transformHealthData(response);
            $scope.updatingHealth = false;
        }, function (response) {
            $scope.healthData =  $scope.transformHealthData(response.data);
            $scope.updatingHealth = false;
        });
    };

    $scope.refresh();

    $scope.getLabelClass = function (statusState) {
        if (statusState === 'UP') {
            return 'label-success';
        } else {
            return 'label-danger';
        }
    };

    $scope.transformHealthData = function (data) {
        const response = [];
        $scope.flattenHealthData(response, null, data);
        return response;
    };

    $scope.flattenHealthData = function (result, path, data) {
        angular.forEach(data, function (value, key) {
            if ($scope.isHealthObject(value)) {
                if ($scope.hasSubSystem(value)) {
                    $scope.addHealthObject(result, false, value, $scope.getModuleName(path, key));
                    $scope.flattenHealthData(result, $scope.getModuleName(path, key), value);
                } else {
                    $scope.addHealthObject(result, true, value, $scope.getModuleName(path, key));
                }
            }
        });
        return result;
    };

    $scope.getModuleName = function (path, name) {
        let result;
        if (path && name) {
            result = path + $scope.separator + name;
        }  else if (path) {
            result = path;
        } else if (name) {
            result = name;
        } else {
            result = '';
        }
        return result;
    };


    $scope.showHealth = health => {
        const modalInstance = $uibModal.open({
            templateUrl: 'states/admin/health/health.modal.html',
            controller: 'HealthModalController',
            size: 'lg',
            resolve: {
                currentHealth: function() {
                    return health;
                },
                baseName: function() {
                    return $scope.baseName;
                },
                subSystemName: function() {
                    return $scope.subSystemName;
                }

            }
        });
        modalInstance.result.catch(() => {});
    };

    $scope.addHealthObject = function (result, isLeaf, healthObject, name) {

        const healthData: any = {
            'name': name
        };
        const details = {};
        let hasDetails = false;

        angular.forEach(healthObject, function (value, key) {
            if (key === 'status' || key === 'error') {
                healthData[key] = value;
            } else {
                if (!$scope.isHealthObject(value)) {
                    details[key] = value;
                    hasDetails = true;
                }
            }
        });

        // Add the of the details
        if (hasDetails) {
            angular.extend(healthData, { 'details': details});
        }

        // Only add nodes if they provide additional information
        if (isLeaf || hasDetails || healthData.error) {
            result.push(healthData);
        }
        return healthData;
    };

    $scope.hasSubSystem = function (healthObject) {
        let result = false;
        angular.forEach(healthObject, function (value) {
            if (value && value.status) {
                result = true;
            }
        });
        return result;
    };

    $scope.isHealthObject = function (healthObject) {
        let result = false;
        angular.forEach(healthObject, function (value, key) {
            if (key === 'status') {
                result = true;
            }
        });
        return result;
    };

    $scope.baseName = function (name) {
        if (name) {
          const split = name.split('.');
          return split[0];
        }
    };

    $scope.subSystemName = function (name) {
        if (name) {
          const split = name.split('.');
          split.splice(0, 1);
          const remainder = split.join('.');
          return remainder ? ' - ' + remainder : '';
        }
    };
});
