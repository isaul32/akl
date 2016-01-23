'use strict';

angular.module('aklApp')
    .controller('CkeditorCtrl', function ($scope) {


        // Editor options.
        $scope.options = {
            skin: 'bootstrapck,/bower_components/bootstrapck4-skin/skins/bootstrapck/',
            language: 'fi',
            allowedContent: true,
            entities: false
        };

        // Called when the editor is completely ready.
        $scope.onReady = function () {
            // ...
        };
    });
