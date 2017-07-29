angular.module('app')
.filter('age', () => {
    return age => {
        return age ? (age !== 0 ? age : '') : '';
    };
});
