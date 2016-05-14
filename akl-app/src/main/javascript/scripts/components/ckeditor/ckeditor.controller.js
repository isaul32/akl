'use strict';

angular.module('aklApp')
    .controller('CkeditorCtrl', function ($scope, $rootScope, $translate, AlertService, Principal) {
        $scope.isAuthenticated = Principal.isAuthenticated;
        
        var lang = $translate.use() || 'en';

        // Toolbar configs
        var toolbarGroups = [
            { name: 'clipboard', groups: ['clipboard', 'undo'] },
            { name: 'editing', groups: ['find', 'selection'] },
            { name: 'links' },
            { name: 'insert'},
            { name: 'forms' },
            '/',
            { name: 'basicstyles', groups: ['basicstyles', 'cleanup'] },
            { name: 'paragraph', groups: ['list', 'indent', 'blocks', 'align', 'bidi'] },
            { name: 'styles' },
            { name: 'colors' },
            { name: 'document', groups: ['mode', 'document', 'doctools'] },
            { name: 'tools' },
            { name: 'others' }
        ];

        // Editor options.
        $scope.options = {
            language: lang,
            extraPlugins: 'autogrow,widgetbootstrap,base64image,inlinesave,inlinecancel,justify,youtube,tableresize',
            toolbarGroups: toolbarGroups,
            disableNativeSpellChecker: true,
            allowedContent: true,
            entities: false,
            disableAutoInline: true,
            inlinesave: {
                postUrl: '/',
                onSave: function() {
                    $scope.text.save().then(function (res) {
                        $scope.text = res.data;
                        $scope.editMode = false;
                    });
                }
            },
            inlinecancel: {
                onCancel: function() {
                    $scope.text = $scope.text.get().then(function (res) {
                        $scope.text = res.data;
                        $scope.editMode = false;
                    })
                }
            },
            ckwebspeech: {
                'culture' : 'fi-FI'
            }
        };
    });
