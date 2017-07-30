angular.module('app')
.factory('Tracker', ($rootScope, $cookies, $http, $q, SERVICE_PATH) => {
    let stompClient = null;
    let subscriber = null;
    let listener = $q.defer();
    let connected = $q.defer();
    let alreadyConnectedOnce = false;
    let sendActivity = () => {
        if (stompClient != null && stompClient.connected && $rootScope.toState != null) {
            stompClient.send('/topic/activity',
                {},
                JSON.stringify({'page': $rootScope.toState.name}));
        }
    };

    return {
        connect: () => {
            if (!stompClient) {
                let loc = window.location;
                //let url = '//' + loc.host + loc.pathname + 'websocket/tracker';
                let url = '//' + loc.host + SERVICE_PATH + '/websocket/tracker';
                let socket = new SockJS(url);
                stompClient = Stomp.over(socket);
                // Disable debug logging
                stompClient.debug = null;

                let headers = {};
                headers['X-CSRF-TOKEN'] = $cookies[$http.defaults.xsrfCookieName];
                stompClient.connect(headers, frame => {
                    connected.resolve("success");
                    sendActivity();
                    if (!alreadyConnectedOnce) {
                        // Todo: https://ui-router.github.io/guide/ng1/migrate-to-1_0#state-change-events
                        $rootScope.$on('$stateChangeStart', event => {
                            sendActivity();
                        });
                        alreadyConnectedOnce = true;
                    }
                });
            }
        },
        subscribe: () => {
            connected.promise.then(() => {
                subscriber = stompClient.subscribe("/topic/tracker", data => {
                    listener.notify(JSON.parse(data.body));
                });
            }, null, null);
        },
        unsubscribe: () => {
            if (subscriber != null) {
                subscriber.unsubscribe();
            }
        },
        receive: () => {
            return listener.promise;
        },
        sendActivity: () => {
            if (stompClient != null) {
                sendActivity();
            }
        },
        disconnect: () => {
            if (stompClient != null) {
                stompClient.disconnect();
                stompClient = null;
            }
        }
    };
});
