'use strict';

angular.module('app', [
    'LocalStorageModule', 'tmh.dynamicLocale', 'pascalprecht.translate',
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
        console.log("State error");
    });
})
.config(($urlRouterProvider, $httpProvider, $locationProvider,
                  $translateProvider, tmhDynamicLocaleProvider, RestangularProvider, API_PATH) => {
    //enable CSRF
    $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
    $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

    $urlRouterProvider.otherwise('/');

    $httpProvider.interceptors.push('errorHandlerInterceptor');
    $httpProvider.interceptors.push('authExpiredInterceptor');
    $httpProvider.interceptors.push('notificationInterceptor');

    $translateProvider.useLoader('$translatePartialLoader', {
        urlTemplate: 'i18n/{lang}/{part}.json'
    });
    $translateProvider.preferredLanguage('fi');
    $translateProvider.useCookieStorage();
    $translateProvider.useSanitizeValueStrategy('escaped');

    tmhDynamicLocaleProvider.localeLocationPattern('i18n/angular-locale_{{locale}}.js');
    tmhDynamicLocaleProvider.useCookieStorage();
    tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');

    RestangularProvider.setBaseUrl(API_PATH);
})
.constant('VERSION', '1.0.0')
.constant('SERVICE_URL', window.location.origin + '/akl-service')
.constant('SERVICE_PATH', '/akl-service')
.constant('API_URL', window.location.origin + '/akl-service' + '/api')
.constant('API_PATH', '/akl-service/api');
