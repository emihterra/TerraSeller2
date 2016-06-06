/**
 * Created by emih on 06.06.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .factory('terraSellerLocationsService', terraSellerLocationsService);

    terraSellerLocationsService.$inject = ['$http'];

    function terraSellerLocationsService ($http) {
        var service = {
            get: get
        };

        return service;

        function get() {
            return $http.get(appConfig.apiSIUrl + 'employee-service/locations')
                .then(getDimensionsComplete);
            function getDimensionsComplete (response) {
                var data = [];

                angular.forEach(response.data, function (item) {
                    data.push({
                        code: item.inventLocationID,
                        name: item.name
                    });
                });

                return data;

            }
        }

    }
})();
