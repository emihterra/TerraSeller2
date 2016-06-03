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
            get: get,
            searchCollections: searchCollections,
            isCollectionType: isCollectionType,
            getPicLinks: getPicLinks,
            recountQty: recountQty
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
                        itemsize: item.itemsize.trim(),
                        checked: false
                    });
                });

                return data;
//                var orderBy = $filter('orderBy');
//                return orderBy(properties, 'prefix');
            }
        }

        function searchCollections (emplid, itemcode) {
            return $http.get(appConfig.apiSIUrl + 'product/' + emplid + '/' + itemcode + '/search')
                .then(getCollectionsComplete);

            function getCollectionsComplete (response) {
                return response.data;
            }
        }

        function isCollectionType(itemcode) {
            return $http.get(appConfig.apiSIUrl + 'product/' + itemcode + '/gettype')
                .then(getCollectionType);

            function getCollectionType (response) {
                if(response.data == 'Item'){
                    return false;
                } else {
                    return true;
                }
            }
        }

        function recountQty(itemcode, qty) {
            return $http.get(appConfig.apiSIUrl + 'product/' + itemcode + '/recount/' + qty)
                .then(getNewQty);

            function getNewQty (response) {
               return response.data;
            }
        }

        function getPicLinks (itemCollectionID, itemProducerID) {
            return $http.get(appConfig.apiSIUrl + 'product/piclinks/' + itemCollectionID + '/' + itemProducerID)
                .then(getPicLinksComplete);

            function getPicLinksComplete (response) {

                var data = [];
                var thumbUrl = "";
                var bigUrl = "";
                var picIndex = 0;

                angular.forEach(response.data, function (item) {

                    thumbUrl = item.name;
                    thumbUrl = 'http://' + thumbUrl.substring(6);

                    bigUrl = thumbUrl.substring(0, thumbUrl.length - 4);
                    bigUrl = bigUrl + '_Z.jpg';

                    data.push({
                        thumbUrl: thumbUrl,
                        bigUrl: bigUrl,
                        idx: picIndex,
                        active: false
                    });
                    picIndex += 1;
                });

                return data;
            }
        }

    }
})();
