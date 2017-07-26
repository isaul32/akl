angular.module('app')
.controller('LanguageController', ($scope, $translate, Language) => {
    $scope.changeLanguage = languageKey => {
        $translate.use(languageKey);
    };

    Language.getAll().then(languages => {
        $scope.languages = languages;
    });
})
.filter('findLanguageFromKey', () => {
    return lang => {
        return { "fi": "Suomi", "en": "English" }[lang];
    }
});
