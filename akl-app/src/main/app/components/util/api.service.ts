angular.module('app')
.factory('Api', (Restangular) => {
    return Restangular.withConfig(RestangularConfigurer => {
        RestangularConfigurer.setFullResponse(true);
    });
});
