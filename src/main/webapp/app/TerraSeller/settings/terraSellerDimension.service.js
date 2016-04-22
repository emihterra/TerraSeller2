/**
 * Created by emih on 21.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .factory('terraSellerDimensionService', terraSellerDimensionService);

    terraSellerDimensionService.$inject = ['$http'];

    function terraSellerDimensionService ($http) {
        var service = {
            get: get
        };

        return service;

        function get() {
            return $http.get(appConfig.apiSIUrl + 'employee-service/dimensions')
                .then(getDimensionsComplete);
            function getDimensionsComplete (response) {
                var data = [];

                angular.forEach(response.data, function (item) {
                    data.push({
                        num: item.num,
                        description: item.description
                    });
                });

                return data;

            }
        }

    }
})();
