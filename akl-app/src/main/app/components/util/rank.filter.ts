angular.module('app')
.filter('rank', () => {
    return rank => {
        return rank ? rank.toUpperCase() : '';
    };
});
