angular.module('app')
.factory('Password', ($resource, API_URL) => {
    return $resource(API_URL + '/account/change_password', {}, {
    });
})
.factory('PasswordResetInit', ($resource, API_URL) => {
    return $resource(API_URL + '/account/reset_password/init', {}, {
    })
})
.factory('PasswordResetFinish', ($resource, API_URL) => {
    return $resource(API_URL + '/account/reset_password/finish', {}, {
    })
});
