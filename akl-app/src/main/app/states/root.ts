angular.module('app')
.config($stateProvider => $stateProvider
    .state('root', {
        'abstract': true,
        views: {
            'navbar@': {
                templateUrl: 'components/navbar/navbar.html',
                controller: 'NavbarController'
            },
            'footer@': {
                templateUrl: 'components/footer/footer.html',
                controller: 'FooterController'
            }
        },
        resolve: {
            translatePartialLoader: ($translate, $translatePartialLoader) => {
                $translatePartialLoader.addPart('global');
            },
            authorize: (Auth, $q, $timeout) => {
                const deferred = $q.defer();

                const t = $timeout(() => {
                    alert("AKL API didn't respond in 10 seconds. Please try again later.");
                }, 10000);

                Auth.authorize().then(res => {
                    $timeout.cancel(t);
                    deferred.resolve(res);
                });

                return deferred.promise;
            }
        }
    })
);
