angular.module('app')
    .config($stateProvider => $stateProvider
        .state('contacts', {
            parent: 'root',
            url: '/contacts',
            data: {
                roles: []
            },
            views: {
                'content@': {
                    templateUrl: 'states/contacts/contacts.html',
                    controller: 'ContactsController'
                }
            },
            resolve: {
                translatePartialLoader: ($translate, $translatePartialLoader) => {
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                },
                text: (Api) => {
                    return Api.one('texts', 7).get();
                }
            }
        })
    );
