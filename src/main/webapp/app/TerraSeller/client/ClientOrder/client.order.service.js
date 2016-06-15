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
            getDivisions: getDivisions,
            getTypeStr: getTypeStr
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

        function getTypeStr(item){
            var retStr = "тип не опр";

            switch(item.useType) {
                case "1": retStr = "Светлая"; break;
                case "2": retStr = "Темная"; break;
                case "3": retStr = "Пол"; break;
                case "4": retStr = "Мозаика"; break;
                case "5": retStr = "Бордюр нижний"; break;
                case "6": retStr = "Бордюр верхний"; break;
                case "7": retStr = "Декор"; break;
                default: retStr = "тип не опр"; break;
            };

            return retStr;
        };
        
    }
})();
