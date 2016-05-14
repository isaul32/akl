'use strict';

angular.module('aklApp')
    .factory('Team', function ($resource, DateUtils, API_URL) {
        return $resource(API_URL + '/teams/:id', {id: '@id', userId: '@userId'}, {
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
    .factory('AccountTeam', function TeamService($q, Team) {
        var _team;
        var _deferred;

        return {
            setTeam: function (team) {
                _team = team;
            },
            team: function(force) {
                if (_deferred) {
                    return _deferred.promise;
                }
                
                var deferred = $q.defer();

                if (force === true) {
                    _team = undefined;
                }

                if (angular.isDefined(_team)) {
                    deferred.resolve(_team);

                    return deferred.promise;
                }

                _deferred = deferred;

                Team.self().$promise
                    .then(function (team) {
                        _team = team;
                        _deferred.resolve(_team);
                        _deferred = null;
                    })
                    .catch(function() {
                        _team = null;
                        _deferred.resolve(_team);
                        _deferred = null;
                    });

                return _deferred.promise;
            }
        };
    });
