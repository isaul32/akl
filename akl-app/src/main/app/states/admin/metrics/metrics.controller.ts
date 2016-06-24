angular.module('app')
.controller('MetricsController', ($scope, MonitoringService, $uibModal) => {
    $scope.metrics = {};
    $scope.updatingMetrics = true;

    $scope.refresh = () => {
        $scope.updatingMetrics = true;
        MonitoringService.getMetrics().then(promise => {
            $scope.metrics = promise;
            $scope.updatingMetrics = false;
        }, promise => {
            $scope.metrics = promise.data;
            $scope.updatingMetrics = false;
        });
    };

    $scope.$watch('metrics', newValue => {
        $scope.servicesStats = {};
        $scope.cachesStats = {};
        angular.forEach(newValue.timers, (value, key) => {
            if (key.indexOf('web.rest') !== -1 || key.indexOf('service') !== -1) {
                $scope.servicesStats[key] = value;
            }

            if (key.indexOf('net.sf.ehcache.Cache') !== -1) {
                // remove gets or puts
                let index = key.lastIndexOf('.');
                let newKey = key.substr(0, index);

                // Keep the name of the domain
                index = newKey.lastIndexOf('.');
                $scope.cachesStats[newKey] = {
                    'name': newKey.substr(index + 1),
                    'value': value
                };
            }
        });
    });

    $scope.refresh();

    $scope.refreshThreadDumpData = () => {
        MonitoringService.threadDump().then(data => {

            $uibModal.open({
                templateUrl: 'scripts/app/admin/metrics/metrics.modal.html',
                controller: 'MetricsModalController',
                size: 'lg',
                resolve: {
                    threadDump: () => {
                        return data;
                    }

                }
            });
        });
    };
    
});
