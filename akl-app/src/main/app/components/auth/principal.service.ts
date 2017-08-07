angular.module('app')
.factory('Principal', function Principal($q, Account, Tracker) {
    let _identity,
        _deferred,
        _authenticated = false;

    return {
        isIdentityResolved: function () {
            return angular.isDefined(_identity);
        },
        isAuthenticated: function () {
            return _authenticated;
        },
        isInRole: function (role) {
            const deferred = $q.defer();

            if (!_authenticated) {
                deferred.resolve(false);
            }

            this.identity().then(_id => {
                if (_id) {
                    const val = _id.authorities && _id.authorities.name && _id.authorities.name.indexOf(role) !== -1;
                    deferred.resolve(val);
                } else {
                    deferred.resolve(false);
                }
            }, function () {
                deferred.resolve(false);
            });

            return deferred.promise;
        },
        isInAnyRole: function (roles) {
            const deferred = $q.defer();

            if (!_authenticated || !_identity || !_identity.authorities) {
                deferred.resolve(false);
            }

            roles.forEach(role => {
                this.isInRole(role).then(res => {
                    deferred.resolve(res);
                }).catch(() => {
                    deferred.resolve(false);
                });
            });

            return deferred.promise;
        },
        authenticate: function (identity) {
            _identity = identity;
            _authenticated = identity !== null;
        },
        identity: function (force) {
            if (_deferred) {
                return _deferred.promise;
            }

            const deferred = $q.defer();

            if (force === true) {
                _identity = undefined;
            }

            // check and see if we have retrieved the identity data from the server.
            // if we have, reuse it by immediately resolving
            if (angular.isDefined(_identity)) {
                deferred.resolve(_identity);

                return deferred.promise;
            }

            _deferred = deferred;

            // retrieve the identity data from the server, update the identity object, and then resolve.
            Account.get().$promise.then(account => {
                if (account.data.login !== 'anonymousUser') {
                    _identity = account.data;
                    _authenticated = true;
                    _deferred.resolve(_identity);
                    _deferred = null;
                    Tracker.connect();
                } else {
                    _identity = null;
                    _authenticated = false;
                    _deferred.resolve(_identity);
                    _deferred = null;
                }
            }).catch(() => {
                _identity = null;
                _authenticated = false;
                _deferred.resolve(_identity);
                _deferred = null;
            });

            return _deferred.promise;
        }
    };
});
