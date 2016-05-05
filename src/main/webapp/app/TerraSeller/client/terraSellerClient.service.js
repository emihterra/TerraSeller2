/**
 * Created by emih on 22.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .factory('terraSellerClientService', terraSellerClientService);

    terraSellerClientService.$inject = ['$http'];

    function terraSellerClientService ($http) {
        var service = {
            searchClient: getClients,
            getDivisions: getDivisions,
            getInfo: getInfo,
            getStatistic: getStatistic
        };

        return service;

        function getClients(name, emplcode) {
            return $http.get(appConfig.apiSIUrl + 'clients?search=' + name + '&employee=' + emplcode)
                .then(getClientsComplete);

            function getClientsComplete (response) {
                return response.data;
            }
        }

        function getDivisions() {
            return $http.get(appConfig.apiSIUrl + 'cart/divisions/')
                .then(getDivisionsComplete);

            function getDivisionsComplete (response) {
                return response.data;
            }
        }

        function getInfo(clientCode) {
            return $http.get(appConfig.apiSIUrl + 'clients/info/' + clientCode)
                .then(getClientInfoComplete);

            function getClientInfoComplete (response) {
                return response.data;
            }
        }

        function getStatistic(clientCode) {
            return $http.get(appConfig.apiSIUrl + 'clients/statistic/' + clientCode)
                .then(getStatisticComplete);

            function getStatisticComplete (response) {
                return response.data;
            }
        }
    }
})();
