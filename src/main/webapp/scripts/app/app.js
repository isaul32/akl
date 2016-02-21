'use strict';

require('jquery');
require('lodash');
require('stomp-client');
require('sockjs-client');
require('moment');
require('fullcalendar');
require('json3');
require('messageformat');

require('angular');
require('angular-cache-buster');
require('angular-cookies');
require('angular-dynamic-locale');
require('angular-file-upload');
require('angular-local-storage');
require('angular-resource');
require('angular-translate');
require('angular-translate-interpolation-messageformat');
require('angular-translate-loader-partial');
require('angular-translate-storage-cookie');
require('angular-ui-bootstrap');
require('angular-ui-calendar');
require('angular-ui-router');

require('restangular');

require('../components/auth/auth.service');
require('../components/auth/principal.service');
require('../components/auth/authority.directive');
require('../components/auth/services/account.service');
require('../components/auth/services/activate.service');
require('../components/auth/services/password.service');
require('../components/auth/services/register.service');
require('../components/auth/services/sessions.service');
require('../components/auth/provider/auth.session.service');
require('../components/ckeditor/ckeditor.controller');
require('../components/ckeditor/ckeditor.directive');
require('../components/form/form.directive');
require('../components/form/maxbytes.directive');
require('../components/form/minbytes.directive');
require('../components/form/pager.directive');
require('../components/form/pagination.directive');
require('../components/admin/audits.service');
require('../components/admin/logs.service');
require('../components/admin/configuration.service');
require('../components/admin/monitoring.service');
require('../components/interceptor/auth.interceptor');
require('../components/interceptor/errorhandler.interceptor');
require('../components/interceptor/notification.interceptor');
require('../components/navbar/navbar.controller');
require('../components/navbar/navbar.directive');
require('../components/footer/footer.controller');
require('../components/user/user.service');
require('../components/util/base64.service');
require('../components/util/dateutil.service');
require('../components/util/parse-links.service');
require('../components/util/truncate.filter');
require('../components/alert/alert.service');
require('../components/alert/alert.directive');
require('../components/language/language.service');
require('../components/language/language.controller');
require('../components/tracker/tracker.service');
require('../components/entities/team/team.service');

require('./account/account');
require('./account/activate/activate');
require('./account/activate/activate.controller');
require('./account/login/login');
require('./account/login/login.controller');
require('./account/logout/logout');
require('./account/logout/logout.controller');
require('./account/password/password');
require('./account/password/password.controller');
require('./account/password/password.directive');
require('./account/register/register');
require('./account/register/register.controller');
require('./account/settings/settings');
require('./account/settings/settings.controller');
require('./account/steam/steam');
require('./account/steam/steam.controller');
require('./account/reset/finish/reset.finish');
require('./account/reset/finish/reset.finish.controller');
require('./account/reset/request/reset.request');
require('./account/reset/request/reset.request.controller');
require('./account/sessions/sessions');
require('./account/sessions/sessions.controller');

require('./admin/admin');
require('./admin/audits/audits');
require('./admin/audits/audits.controller');
require('./admin/configuration/configuration');
require('./admin/configuration/configuration.controller');
require('./admin/docs/docs');
require('./admin/health/health');
require('./admin/health/health.controller');
require('./admin/health/health.modal.controller');
require('./admin/logs/logs');
require('./admin/logs/logs.controller');
require('./admin/metrics/metrics');
require('./admin/metrics/metrics.controller');
require('./admin/metrics/metrics.modal.controller');
require('./admin/user/user');
require('./admin/user/user.controller');
require('./admin/tracker/tracker');
require('./admin/tracker/tracker.controller');

require('./entities/entity');
require('./error/error');
require('./live/live');
require('./live/live.controller');
require('./main/main');
require('./main/main.controller');
require('./schedule/schedule');
require('./schedule/schedule.controller');
require('./rules/rules');
require('./rules/rules.controller');
require('./rules/rules.controller');

require('./entities/team/team');
require('./entities/team/team.controller');
require('./entities/team/team-detail.controller');
require('./entities/team/team-dialog.controller');

angular.module('aklApp', ['LocalStorageModule', 'tmh.dynamicLocale', 'pascalprecht.translate',
    'ui.bootstrap', 'ngResource', 'ui.router', 'ngCookies', 'ngCacheBuster', 'angularFileUpload',
    'ui.calendar'])

    .run(function ($rootScope, $location, $window, $http, $state, $translate, Language, Auth, Principal, ENV, VERSION) {
        $rootScope.ENV = ENV;
        $rootScope.VERSION = VERSION;
        $rootScope.$on('$stateChangeStart', function (event, toState, toStateParams) {
            $rootScope.toState = toState;
            $rootScope.toStateParams = toStateParams;

            if (Principal.isIdentityResolved()) {
                Auth.authorize();
            }

            // Update the language
            Language.getCurrent().then(function (language) {
                $translate.use(language);
            });

        });

        $rootScope.$on('$stateChangeSuccess',  function(event, toState, toParams, fromState, fromParams) {
            var titleKey = 'global.title' ;

            $rootScope.previousStateName = fromState.name;
            $rootScope.previousStateParams = fromParams;

            // Set the page title key to the one configured in state or use default one
            if (toState.data.pageTitle) {
                titleKey = toState.data.pageTitle;
            }

            $translate(titleKey).then(function (title) {
                // Change window title with translated one
                $window.document.title = title;
            });

        });

        $rootScope.back = function() {
            // If previous state is 'activate' or do not exist go to 'home'
            if ($rootScope.previousStateName === 'activate' || $state.get($rootScope.previousStateName) === null) {
                $state.go('home');
            } else {
                $state.go($rootScope.previousStateName, $rootScope.previousStateParams);
            }
        };
    })
    .config(function ($stateProvider, $urlRouterProvider, $httpProvider, $locationProvider, $translateProvider, tmhDynamicLocaleProvider, httpRequestInterceptorCacheBusterProvider) {

        //enable CSRF
        $httpProvider.defaults.xsrfCookieName = 'CSRF-TOKEN';
        $httpProvider.defaults.xsrfHeaderName = 'X-CSRF-TOKEN';

        //Cache everything except rest api requests
        httpRequestInterceptorCacheBusterProvider.setMatchlist([/.*api.*/, /.*protected.*/], true);

        $urlRouterProvider.otherwise('/');
        $stateProvider.state('site', {
            'abstract': true,
            views: {
                'navbar@': {
                    templateUrl: 'scripts/components/navbar/navbar.html',
                    controller: 'NavbarController'
                },
                'footer@': {
                    templateUrl: 'scripts/components/footer/footer.html',
                    controller: 'FooterController'
                }
            },
            resolve: {
                authorize: ['Auth',
                    function (Auth) {
                        return Auth.authorize();
                    }
                ],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('global');
                }]
            }
        });

        $httpProvider.interceptors.push('errorHandlerInterceptor');
        $httpProvider.interceptors.push('authExpiredInterceptor');
        $httpProvider.interceptors.push('notificationInterceptor');

        // Initialize angular-translate
        $translateProvider.useLoader('$translatePartialLoader', {
            urlTemplate: 'i18n/{lang}/{part}.json'
        });

        $translateProvider.preferredLanguage('fi');
        $translateProvider.useCookieStorage();
        $translateProvider.useSanitizeValueStrategy('escaped');
        $translateProvider.addInterpolation('$translateMessageFormatInterpolation');

        tmhDynamicLocaleProvider.localeLocationPattern('bower_components/angular-i18n/angular-locale_{{locale}}.js');
        tmhDynamicLocaleProvider.useCookieStorage();
        tmhDynamicLocaleProvider.storageKey('NG_TRANSLATE_LANG_KEY');

    });

