/**
 * Created by emih on 03.06.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .factory('terraSellerOrderService', terraSellerOrderService);

    terraSellerOrderService.$inject = ['$filter', '$http'];

    function terraSellerOrderService ($filter, $http) {
        var service = {
            createOrderHeader: createOrderHeader,
            createOrderBody: createOrderBody,
            getOrderNumber: getOrderNumber,
            getDivisions: getDivisions
        };

        return service;

        function createOrderHeader (OrderHeader) {
            return $http.post(appConfig.apiSIUrl + 'cart/create/', OrderHeader, {transformResponse: undefined})
                .then(createOrderHeaderComplete);

            function createOrderHeaderComplete (response) {
                return response.data;
            }
        }

        function createOrderBody (OrderBody) {
            return $http.post(appConfig.apiSIUrl + 'cart/createLine', OrderBody, {transformResponse: undefined})
                .then(createOrderBodyComplete);

            function createOrderBodyComplete (response) {
                return response;
            }
        }

        function getOrderNumber (basketCode) {
            return $http.get(appConfig.apiSIUrl + 'cart/order_number/' + basketCode)
                .then(getOrderNumberComplete);

            function getOrderNumberComplete (response) {
                console.log(response);
                return response;
            }
        }

        function getDivisions (id) {
            return $http.get(appConfig.apiSIUrl + '/cart/divisions/')
                .then(getDivisionsComplete);

            function getDivisionsComplete (response) {
                return response.data;
            }
        }

    }
})();
