'use strict';

// Vendor libs
global.jQuery = require('jquery');
global.$ = require('jquery');
global._ = require('lodash');
global.moment = require('moment');
global.SockJS = require('sockjs-client');
global.CKEDITOR_BASEPATH = '/ckeditor/';

require('stompjs');
require('fullcalendar');
require('../../../node_modules/fullcalendar/dist/gcal');
require('../../../node_modules/fullcalendar/dist/lang-all');
require('json3');
require('messageformat');
require('bootstrap-sass');
require('jquery-ui');

require('angular');
require('angular-cookies');
require('angular-sanitize');
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
require('angular-ui-sortable');
require('angular-moment');
require('restangular');
require('angulartics');
require('angulartics-google-analytics');

// App files
require('./scripts/app/app');

require('./scripts/components/auth/auth.service');
require('./scripts/components/auth/principal.service');
require('./scripts/components/auth/authority.directive');
require('./scripts/components/auth/services/account.service');
require('./scripts/components/auth/services/activate.service');
require('./scripts/components/auth/services/password.service');
require('./scripts/components/auth/services/register.service');
require('./scripts/components/auth/services/sessions.service');
require('./scripts/components/auth/provider/auth.session.service');
require('./scripts/components/ckeditor/ckeditor.directive');
require('./scripts/components/ckeditor/ckeditor.controller');
require('./scripts/components/form/form.directive');
require('./scripts/components/form/maxbytes.directive');
require('./scripts/components/form/minbytes.directive');
require('./scripts/components/form/pager.directive');
require('./scripts/components/form/pagination.directive');
require('./scripts/components/admin/audits.service');
require('./scripts/components/admin/logs.service');
require('./scripts/components/admin/configuration.service');
require('./scripts/components/admin/monitoring.service');
require('./scripts/components/interceptor/auth.interceptor');
require('./scripts/components/interceptor/errorhandler.interceptor');
require('./scripts/components/interceptor/notification.interceptor');
require('./scripts/components/navbar/navbar.controller');
require('./scripts/components/navbar/navbar.directive');
require('./scripts/components/footer/footer.controller');
require('./scripts/components/util/api.service');
require('./scripts/components/util/base64.service');
require('./scripts/components/util/dateutil.service');
require('./scripts/components/util/parse-links.service');
require('./scripts/components/util/truncate.filter');
require('./scripts/components/alert/alert.service');
require('./scripts/components/alert/alert.directive');
require('./scripts/components/language/language.service');
require('./scripts/components/language/language.controller');
require('./scripts/components/tracker/tracker.service');
require('./scripts/components/entities/team/team.service');
require('./scripts/components/challonge/challonge.directive');

require('./scripts/app/account/account');
require('./scripts/app/account/activate/activate');
require('./scripts/app/account/activate/activate.controller');
require('./scripts/app/account/login/login');
require('./scripts/app/account/login/login.controller');
require('./scripts/app/account/logout/logout');
require('./scripts/app/account/logout/logout.controller');
require('./scripts/app/account/password/password');
require('./scripts/app/account/password/password.controller');
require('./scripts/app/account/password/password.directive');
require('./scripts/app/account/register/register');
require('./scripts/app/account/register/register.controller');
require('./scripts/app/account/settings/settings');
require('./scripts/app/account/settings/settings.controller');
require('./scripts/app/account/steam/steam');
require('./scripts/app/account/steam/steam.controller');
require('./scripts/app/account/reset/finish/reset.finish');
require('./scripts/app/account/reset/finish/reset.finish.controller');
require('./scripts/app/account/reset/request/reset.request');
require('./scripts/app/account/reset/request/reset.request.controller');
require('./scripts/app/account/sessions/sessions');
require('./scripts/app/account/sessions/sessions.controller');

require('./scripts/app/admin/admin');
require('./scripts/app/admin/audits/audits');
require('./scripts/app/admin/audits/audits.controller');
require('./scripts/app/admin/configuration/configuration');
require('./scripts/app/admin/configuration/configuration.controller');
require('./scripts/app/admin/docs/docs');
require('./scripts/app/admin/health/health');
require('./scripts/app/admin/health/health.controller');
require('./scripts/app/admin/health/health.modal.controller');
require('./scripts/app/admin/logs/logs');
require('./scripts/app/admin/logs/logs.controller');
require('./scripts/app/admin/metrics/metrics');
require('./scripts/app/admin/metrics/metrics.controller');
require('./scripts/app/admin/metrics/metrics.modal.controller');
require('./scripts/app/entities/user/user');
require('./scripts/app/entities/user/user.controller');
require('./scripts/app/entities/user/user-detail.controller');
require('./scripts/app/admin/tracker/tracker');
require('./scripts/app/admin/tracker/tracker.controller');

require('./scripts/app/entities/entity');
require('./scripts/app/error/error');
require('./scripts/app/live/live');
require('./scripts/app/live/live.controller');
require('./scripts/app/main/main');
require('./scripts/app/main/main.controller');
require('./scripts/app/schedule/schedule');
require('./scripts/app/schedule/schedule.controller');
require('./scripts/app/rules/rules');
require('./scripts/app/rules/rules.controller');
require('./scripts/app/rules/rules.controller');

require('./scripts/app/entities/team/team');
require('./scripts/app/entities/team/team.controller');
require('./scripts/app/entities/team/team-detail.controller');
require('./scripts/app/entities/team/team-dialog.controller');
require('./scripts/app/entities/team/team-member-dialog.controller');
require('./scripts/app/entities/team/team-schedule-dialog.controller');
require('./scripts/app/entities/group/group');
require('./scripts/app/entities/group/group.controller');
require('./scripts/app/entities/group/group-dialog.controller');
require('./scripts/app/entities/group/group-teams.controller.js');


(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
    (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
  m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

ga('create', 'UA-78802312-1', 'auto');
