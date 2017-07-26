'use strict';

angular.module('app', [
    'LocalStorageModule', 'pascalprecht.translate',
    'ui.bootstrap', 'ngResource', 'ui.router', 'ngCookies', 'angularFileUpload',
    'angularMoment', 'ui.calendar', 'ckeditor', 'templateCache', 'restangular', 'ngSanitize',
    'ui.sortable', 'angulartics', 'angulartics.google.analytics'
])
.run(($rootScope, $location, $window, $http, $state, $translate, Language, Auth, Principal, amMoment) => {
    amMoment.changeLocale('fi');

    $rootScope.$on('$stateChangeStart', (event, toState, toStateParams) => {
        $rootScope.toState = toState;
        $rootScope.toStateParams = toStateParams;

        if (Principal.isIdentityResolved()) {
            Auth.authorize();
        }

        Language.getCurrent().then(language => {
            $translate.use(language);
        });
    });

    $rootScope.$on('$stateChangeSuccess', (event, toState, toParams, fromState, fromParams) => {
        $rootScope.previousStateName = fromState.name;
        $rootScope.previousStateParams = fromParams;
    });

    // Todo: error page
    $rootScope.$on('$stateChangeError', (event, toState, toParams, fromState, fromParams, error) => {
        console.error("State error", event, toState, toParams, fromState, fromParams);
    });
})
.config(($urlRouterProvider, $httpProvider, $locationProvider, $translateProvider,
         RestangularProvider, API_PATH) => {

    // Enable CSRF
    $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

    $urlRouterProvider.otherwise('/');

    $httpProvider.interceptors.push('errorHandlerInterceptor');
    $httpProvider.interceptors.push('authExpiredInterceptor');
    $httpProvider.interceptors.push('notificationInterceptor');

    $locationProvider.hashPrefix('');

    $translateProvider.useLoader('$translatePartialLoader', {
        urlTemplate: 'i18n/{lang}/{part}.json'
    });
    $translateProvider.preferredLanguage('fi');
    $translateProvider.useCookieStorage();
    $translateProvider.useSanitizeValueStrategy('escaped');

    RestangularProvider.setBaseUrl(API_PATH);
})
.constant('VERSION', '1.0.0')
.constant('SERVICE_URL', window.location.origin + '/akl-service')
.constant('SERVICE_PATH', '/akl-service')
.constant('API_URL', window.location.origin + '/akl-service' + '/api')
.constant('API_PATH', '/akl-service/api');
