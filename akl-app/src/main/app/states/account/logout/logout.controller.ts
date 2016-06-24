angular.module('app')
.controller('LogoutController', Auth => {
    Auth.logout();
});
