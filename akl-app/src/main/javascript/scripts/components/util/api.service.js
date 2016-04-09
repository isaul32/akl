'use strict';

angular.module('aklApp')
    .factory('Api', function (Restangular) {
        return Restangular.withConfig(function (RestangularConfigurer) {
            RestangularConfigurer.setFullResponse(true);
        });
    });
