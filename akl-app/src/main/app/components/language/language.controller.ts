angular.module('app')
.controller('LanguageController', ($scope, $translate, Language, tmhDynamicLocale) => {
    $scope.changeLanguage = languageKey => {
        $translate.use(languageKey);
        tmhDynamicLocale.set(languageKey);
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
