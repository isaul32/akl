angular.module('app')
.factory('Language', ($q, $http, $translate, LANGUAGES) => {
    return {
        getCurrent: () => {
            let deferred = $q.defer();
            let language = $translate.storage().get('NG_TRANSLATE_LANG_KEY');

            if (angular.isUndefined(language)) {
                language = 'fi';
            }

            deferred.resolve(language);
            return deferred.promise;
        },
        getAll: () => {
            let deferred = $q.defer();
            deferred.resolve(LANGUAGES);
            return deferred.promise;
        }
    };
})
.constant('LANGUAGES', ['fi', 'en']);




