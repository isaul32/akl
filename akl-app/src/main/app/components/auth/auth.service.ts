angular.module('app')
.factory('Auth', function Auth($rootScope, $state, $q, $translate, Principal, AuthServerProvider, Account, Register, Activate, Password, PasswordResetInit, PasswordResetFinish, Tracker) {
    return {
        // When use login form
        login: function (credentials, callback) {
            let cb = callback || angular.noop;
            let deferred = $q.defer();

            AuthServerProvider.login(credentials).then((data) => {
                // retrieve the logged account information
                Principal.identity(true).then(account => {
                    // After the login the language will be changed to
                    // the language selected by the user during his registration
                    $translate.use(account.langKey);
                    $translate.refresh();
                    Tracker.sendActivity();
                    deferred.resolve(data);
                });

                return cb();
            }).catch(function (err) {
                this.logout();
                deferred.reject(err);
                return cb(err);
            }.bind(this));

            return deferred.promise;
        },
        logout: function () {
            AuthServerProvider.logout();
            Principal.authenticate(null);
        },
        // When check if already logged in
        authorize: function (force) {
            return Principal.identity(force).then(() => {
                var isAuthenticated = Principal.isAuthenticated();

                if ($rootScope.toState.hasOwnProperty('roles') && $rootScope.toState.data.roles && $rootScope.toState.data.roles.length > 0 && !Principal.isInAnyRole($rootScope.toState.data.roles)) {
                    if (isAuthenticated) {
                        // user is signed in but not authorized for desired state
                        $state.go('accessdenied');
                    }
                    else {
                        // user is not authenticated. stow the state they wanted before you
                        // send them to the signin state, so you can return them when you're done
                        $rootScope.returnToState = $rootScope.toState;
                        $rootScope.returnToStateParams = $rootScope.toStateParams;

                        // now, send them to the signin state so they can log in
                        $state.go('login');
                    }
                }
            });
        },
        createAccount: function (account, callback) {
            let cb = callback || angular.noop;

            return Register.save(account,
                () => cb(account),
                function (err) {
                    this.logout();
                    return cb(err);
                }.bind(this)).$promise;
        },
        updateAccount: function (account, callback) {
            let cb = callback || angular.noop;

            return Account.save(account,
                () => cb(account),
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        },
        activateAccount: function (key, callback) {
            let cb = callback || angular.noop;

            return Activate.get(key,
                response => cb(response),
                function (err) {
                    return cb(err);
                }.bind(this)).$promise;
        },
        changePassword: function (newPassword, callback) {
            let cb = callback || angular.noop;

            return Password.save(newPassword,
                () => cb(),
                err => cb(err)).$promise;
        },
        resetPasswordInit: function (mail, callback) {
            let cb = callback || angular.noop;

            return PasswordResetInit.save(mail,
                () => cb(),
                err => cb(err)).$promise;
        },
        resetPasswordFinish: function (keyAndPassword, callback) {
            let cb = callback || angular.noop;

            return PasswordResetFinish.save(keyAndPassword,
                () => cb(),
                err => cb(err)).$promise;
        }
    };
});
