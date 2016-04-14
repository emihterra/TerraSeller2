/**
 * Created by emih on 14.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .factory('terraSellerSearchService', terraSellerSearchService);

    terraSellerSearchService.$inject = ['$filter', '$http'];

    function terraSellerSearchService ($filter, $http) {
        var service = {
            get: get
        };

        return service;

        function get (emplid, itemcode) {
            return $http.get(appConfig.apiSIUrl + 'product/' + emplid + '/' + itemcode)
                .then(getProductInfoComplete);

            function getProductInfoComplete (response) {

                var data = [];

                angular.forEach(response.data, function (item) {
                    data.push({
                        id: item.id,
                        name: item.name,
                        water: item.water,
                        temper: item.temper,
                        iznos: item.iznos,
                        froz: item.froz,
                        sidUser: item.sidUser,
                        imgUrl: item.imgUrl,
                        unit: item.unit,
                        price: item.price,
                        color: item.color,
                        sizeTon: item.sizeTon,
                        fizQty: item.fizQty,
                        tonQty: item.tonQty,
                        itemCollectionID: item.itemCollectionID,
                        itemProducerID: item.itemProducerID,
                        checked: false
                    });
                });

                return data;
//                var orderBy = $filter('orderBy');
//                return orderBy(properties, 'prefix');
            }
        }

    }
})();
