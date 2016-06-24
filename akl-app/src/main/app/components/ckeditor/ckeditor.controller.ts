angular.module('app')
.controller('CkeditorCtrl', ($scope, $rootScope, $translate, AlertService, Principal) => {
    $scope.isAuthenticated = Principal.isAuthenticated;

    // Toolbar configs
    let toolbarGroups = [
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

    $scope.options = {
        language: $translate.use() || 'en',
        extraPlugins: 'autogrow,widgetbootstrap,base64image,inlinesave,inlinecancel,justify,youtube,tableresize',
        toolbarGroups: toolbarGroups,
        disableNativeSpellChecker: true,
        allowedContent: true,
        entities: false,
        disableAutoInline: true,
        inlinesave: {
            postUrl: '/',
            onSave: () => {
                $scope.text.save().then(res => {
                    $scope.text = res.data;
                    $scope.editMode = false;
                });
            }
        },
        inlinecancel: {
            onCancel: () => {
                $scope.text = $scope.text.get().then(res => {
                    $scope.text = res.data;
                    $scope.editMode = false;
                })
            }
        }
    };
});
