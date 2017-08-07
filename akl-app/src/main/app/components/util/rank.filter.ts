angular.module('app')
.filter('rank', () => {
    return rank => {
        return rank ? rank.toUpperCase() : '';
    };
})
.filter('rankImg', () => {
    return rank => {
        if (rank) {
            return '<img class="rank" alt="' + rank.toUpperCase() + '" src="images/ranks/' + rank + '.png">'
        }

        return '';
    };
});
