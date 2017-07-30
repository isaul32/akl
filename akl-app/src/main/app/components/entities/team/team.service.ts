angular.module('app')
.factory('Team', ($resource, DateUtils, API_URL) => {
    return $resource(API_URL + '/teams/:id', { id: '@id', userId: '@userId' }, {
        'query': { method: 'GET', isArray: true},
        'get': { method: 'GET' },
        'update': { method:'PUT' },
        'activate': {
            method: 'POST',
            url: API_URL + '/teams/:id/activate'
        },
        'requestInvite': {
            method: 'POST',
            url: API_URL + '/teams/:id/requests'
        },
        'requests': {
            method: 'GET',
            url: API_URL + '/teams/:id/requests',
            isArray: true
        },
        'acceptRequest': {
            method: 'POST',
            url: API_URL + '/teams/:id/requests/:userId'
        },
        'declineRequest': {
            method: 'DELETE',
            url: API_URL + '/teams/:id/requests/:userId'
        },
        'self': {
            method: 'GET',
            url: API_URL + '/account/team'
        }
    });
})
.factory('AccountTeam', ($q, Team) => {
    let _team;
    let _deferred;

    return {
        setTeam: team => {
            _team = team;
        },
        team: force => {
            if (_deferred) {
                return _deferred.promise;
            }

            let deferred = $q.defer();

            if (force === true) {
                _team = undefined;
            }

            if (angular.isDefined(_team)) {
                deferred.resolve(_team);

                return deferred.promise;
            }

            _deferred = deferred;

            Team.self().$promise
                .then(team => {
                    _team = team;
                    _deferred.resolve(_team);
                    _deferred = null;
                })
                .catch(() => {
                    _team = null;
                    _deferred.resolve(_team);
                    _deferred = null;
                });

            return _deferred.promise;
        }
    };
});
