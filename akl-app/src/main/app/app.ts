'use strict';

angular.module('app', [
    'LocalStorageModule', 'pascalprecht.translate',
    'ui.bootstrap', 'ngResource', 'ui.router', 'ui.router.state.events', 'ngCookies', 'angularFileUpload',
    'angularMoment', 'ui.calendar', 'ckeditor', 'templateCache', 'restangular', 'ngSanitize',
    'ui.sortable', 'angulartics', 'angulartics.google.analytics'
])
.run(($rootScope, $location, $window, $http, $state, $translate, Language, Auth, Principal, amMoment) => {
    amMoment.changeLocale('fi');

    // Todo: https://ui-router.github.io/guide/ng1/migrate-to-1_0#state-change-events
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

    // Todo: https://ui-router.github.io/guide/ng1/migrate-to-1_0#state-change-events
    $rootScope.$on('$stateChangeSuccess', (event, toState, toParams, fromState, fromParams) => {
        $rootScope.previousStateName = fromState.name;
        $rootScope.previousStateParams = fromParams;
    });

    // Todo: https://ui-router.github.io/guide/ng1/migrate-to-1_0#state-change-events
    $rootScope.$on('$stateChangeError', (event, toState, toParams, fromState, fromParams, error) => {
        console.error("State error", event, toState, toParams, fromState, fromParams);
        $state.go('error', {}, { reload:true });
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
