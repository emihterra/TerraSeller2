/**
 * Created by emih on 22.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('terraSellerClientController', terraSellerClientController);

    terraSellerClientController.$inject = ['$window', '$scope', 'Principal', 'terraSellerClientService', 'terraSellerSettingsService'];

    function terraSellerClientController ($window, $scope, Principal, terraSellerClientService, terraSellerSettingsService) {
        var vm = this;

        vm.searchBox = '';              // модель поисковой строки
        vm.searchType = "byName";       // тип поиска клиента
        vm.searchedClient = {};         // найденный клиент
        vm.possibleClients = [];        // список клиентов, при поиске по части наименования
        vm.progSearchChanged = false;   // значение в поисковой строке изменено программно
        vm.employeeID = '';             // код продавца
        vm.clientStatistic = '';        // статистика по клиенту
        vm.clientInfo = {};             // информация по клиенту
        vm.emplSettings = terraSellerSettingsService.getDefSettingsJSON(); // настройки продавца

        vm.clickSearch = clickSearch;
        vm.choosePossibleClient = choosePossibleClient;
        vm.onKeyUp = onKeyUp;

        Principal.identity().then(function(account) {
            vm.employeeID = account.emplcode;
            vm.emplSettings = terraSellerSettingsService.getJSON_from_Str(account.emplSettings);
        });

        function clickSearch() {
            vm.searchedClient = {};
            search(vm.searchBox);
        };

        function search(clientName) {

            if(vm.searchBox == ''){
                return;
            }

            if(vm.searchBox != clientName){
                vm.progSearchChanged = true;
                vm.searchBox = clientName;
            };

            terraSellerClientService.searchClient(clientName, vm.employeeID).then(function(searchedClient) {
                if((searchedClient)&&(searchedClient.length > 0)){
                    vm.searchedClient = searchedClient[0];
                    vm.possibleClients = [];

                   terraSellerClientService.getInfo(vm.searchedClient.code).then(function(clientInfo) {
                        vm.clientInfo = clientInfo;
                   });

                    terraSellerClientService.getStatistic(vm.searchedClient.code).then(function(clientStatistic) {
                        vm.clientStatistic = clientStatistic;
                    });
                }

            });
        }

        function choosePossibleClient (client) {
            vm.progSearchChanged = true;
            vm.searchBox = client.name;
            vm.possibleClients = [];
            vm.searchedClient = client;
            search(client.name);
        };

        $scope.$watch('vm.searchBox', function (val) {
            if (vm.progSearchChanged) {
                vm.progSearchChanged = false;
                return;
            }

            if (val === undefined || val === null || val.length < 3) {
                vm.possibleClients = [];
                return;
            }

            vm.searchedClient = [];

            terraSellerClientService.searchClient(val, vm.employeeID).then(function(clients) {
                    vm.possibleClients = clients;
                },
                function(err) {
                    vm.possibleClients = [];
                }
            );
        });

        function onKeyUp ($event) {
            var onKeyUpResult = ($window.event ? $event.keyCode : $event.which);
            if (onKeyUpResult === 13) {
                clickSearch();
            }
        };

    }
})();

