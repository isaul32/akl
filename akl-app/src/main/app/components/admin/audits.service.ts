angular.module('app')
.factory('AuditsService', ($http, API_URL) => {
    return {
        findAll: () => {
            return $http.get(API_URL + '/audits/all').then(response => {
                return response.data;
            });
        },
        findByDates: (fromDate, toDate) => {

            var formatDate =  dateToFormat => {
                if (dateToFormat !== undefined && !angular.isString(dateToFormat)) {
                    return dateToFormat.getYear() + '-' + dateToFormat.getMonth() + '-' + dateToFormat.getDay();
                }
                return dateToFormat;
            };

            return $http.get(API_URL + '/audits/byDates', {
                params: {
                    fromDate: formatDate(fromDate),
                    toDate: formatDate(toDate)}}).then(response => {
                return response.data;
            });
        }
    };
});
