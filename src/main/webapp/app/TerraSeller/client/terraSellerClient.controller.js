/**
 * Created by emih on 22.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('terraSellerClientController', terraSellerClientController);

    terraSellerClientController.$inject = ['$window', '$scope', 'Principal', 'terraSellerClientService'];

    function terraSellerClientController ($window, $scope, Principal, terraSellerClientService) {
        var vm = this;

        vm.searchBox = '';              // модель поисковой строки
        vm.searchType = "byName";       // тип поиска клиента
        vm.searchResult = [];           // найденный клиент
        vm.possibleClients = [];        // список клиентов, при поиске по части наименования
        vm.progSearchChanged = false;   // значение в поисковой строке изменено программно
        vm.employeeID = '';             // код продавца

        vm.clickSearch = clickSearch;
        vm.choosePossibleClient = choosePossibleClient;
        vm.onKeyUp = onKeyUp;

        Principal.identity().then(function(account) {
            vm.employeeID = account.emplcode;
        });

        function clickSearch() {
            vm.searchResult = [];
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

            terraSellerClientService.searchClient(clientName, vm.employeeID).then(function(searchResult) {
                vm.searchResult = searchResult;

                if((vm.searchResult)&&(vm.searchResult.length > 0)){
                    vm.possibleClients = [];
                }

            });
        }

        function choosePossibleClient (client) {
            vm.progSearchChanged = true;
            vm.searchBox = client.name;
            vm.possibleClients = [];
            vm.searchResult = client;
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

            vm.searchResult = [];

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

