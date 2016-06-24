angular.module('app')
.service('ParseLinks', () => {
    this.parse = header => {
        if (header.length == 0) {
            throw new Error("input must not be of zero length");
        }

        // Split parts by comma
        let parts = header.split(',');
        let links = {};
        // Parse each part into a named link
        angular.forEach(parts, p => {
            let section = p.split(';');
            if (section.length != 2) {
                throw new Error("section could not be split on ';'");
            }
            let url = section[0].replace(/<(.*)>/, '$1').trim();
            let queryString = {};
            url.replace(
                new RegExp("([^?=&]+)(=([^&]*))?", "g"),
                ($0, $1, $2, $3) => {
                    queryString[$1] = $3;
                }
            );
            let page = queryString['page'];
            if( angular.isString(page) ) {
                page = parseInt(page);
            }
            let name = section[1].replace(/rel="(.*)"/, '$1').trim();
            links[name] = page;
        });

        return links;
    }
});
