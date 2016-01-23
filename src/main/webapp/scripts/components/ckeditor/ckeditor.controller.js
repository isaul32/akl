'use strict';

angular.module('aklApp')
    .controller('CkeditorCtrl', function ($scope, $translate) {

        // Load plugins from custom paths
        CKEDITOR.plugins.addExternal('youtube',
            '/bower_components/ckeditor-youtube-plugin/youtube/', 'plugin.js');
        CKEDITOR.plugins.addExternal('autogrow',
            '/scripts/components/ckeditor/plugins/autogrow/', 'plugin.js');

        // Editor options.
        $scope.options = {
            skin: 'bootstrapck,/bower_components/bootstrapck4-skin/skins/bootstrapck/',
            language: $translate.use(),
            extraPlugins: 'youtube,autogrow',
            allowedContent: true,
            entities: false
        };

        // Called when the editor is completely ready.
        $scope.onReady = function () {
            // ...
        };
    });
