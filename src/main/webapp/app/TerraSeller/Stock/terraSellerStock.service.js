/**
 * Created by emih on 18.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .factory('terraSellerStockService', terraSellerStockService);

    terraSellerStockService.$inject = ['$filter', '$http'];

    function terraSellerStockService ($filter, $http) {
        var service = {
            get: get,
            getHeader: getHeader
        };

        return service;

        function get (emplid, itemcode) {
            return $http.get(appConfig.apiSIUrl + 'stocks/' + itemcode + '?employee=' + emplid)
                .then(getStockInfoComplete);

            function getStockInfoComplete (response) {
                var data = [];

                angular.forEach(response.data, function (item) {
                    data.push({
                        stock: item.stock,
                        analitics: item.sizeTon + "/" + item.color,
                        age: item.total
                    });
                });

                return data;
//                var orderBy = $filter('orderBy');
//                return orderBy(properties, 'prefix');
            }
        }

        function getHeader (emplid) {
            return $http.get(appConfig.apiSIUrl + 'stocks/header?employee=' + emplid)
                .then(getHeaderComplete);

            function getHeaderComplete (response) {
                return response.data;
            }
        }

    }
})();
