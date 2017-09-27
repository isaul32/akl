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
            authorize: (Auth) => {
                return Auth.authorize();
            }
        }
    })
);
