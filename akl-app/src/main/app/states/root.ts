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
            authorize: (Auth, $timeout) => {

                const t = $timeout(() => {
                    alert("AKL API didn't respond in 10 seconds. Please try again later.");
                }, 10000);

                const p = Auth.authorize();
                p.then(() => {
                    $timeout.cancel(t);
                });

                return p;
            }
        }
    })
);
