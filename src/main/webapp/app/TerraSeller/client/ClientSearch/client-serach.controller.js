/**
 * Created by emih on 14.06.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('terraSellerClientSearchController', terraSellerClientSearchController);

    terraSellerClientSearchController.$inject = ['$state', '$window', '$scope', 'Principal', 'terraSellerClientService', 'terraSellerSettingsService'];

    function terraSellerClientSearchController ($state, $window, $scope, Principal, terraSellerClientService, terraSellerSettingsService) {
        var vm = this;

        vm.searchBox = '';              // модель поисковой строки
        vm.searchType = "byName";       // тип поиска клиента
        vm.searchedClient = {};         // найденный клиент
        vm.possibleClients = [];        // список клиентов, при поиске по части наименования
        vm.progSearchChanged = false;   // значение в поисковой строке изменено программно
        vm.employeeID = '';             // код продавца
        vm.clientStatistic = '';        // статистика по клиенту
        vm.clientInfo = null;             // информация по клиенту
        vm.emplSettings = terraSellerSettingsService.getDefault(); // настройки продавца

        vm.clickSearch = clickSearch;
        vm.choosePossibleClient = choosePossibleClient;
        vm.onKeyUp = onKeyUp;
        vm.AddClient = AddClient;

        Principal.identity().then(function(account) {
            vm.employeeID = account.emplcode;
            terraSellerSettingsService.get(account.login).then(function(settings){
                vm.emplSettings = settings;
            });
        });

        function clickSearch() {
            vm.searchedClient = {};
            search(vm.searchBox);
        };

        function search(clientName) {

            if(vm.searchBox == ''){
                return;
            }

            vm.clientInfo = null;

            if(vm.searchType == "byName") {
                if (vm.searchBox != clientName) {
                    vm.progSearchChanged = true;
                    vm.searchBox = clientName;
                };

                terraSellerClientService.searchClient(clientName, vm.employeeID).then(function (searchedClient) {
                    if ((searchedClient) && (searchedClient.length > 0)) {
                        vm.searchedClient = searchedClient[0];
                        vm.possibleClients = [];

                        terraSellerClientService.getInfo(vm.searchedClient.code).then(function (clientInfo) {
                            vm.clientInfo = clientInfo;
                            vm.emplSettings.lastClientCode = vm.searchedClient.code;
                        });

                        terraSellerClientService.getStatistic(vm.searchedClient.code).then(function (clientStatistic) {
                            vm.clientStatistic = clientStatistic;
                        });
                    }

                });
            } else {
                terraSellerClientService.getInfo(vm.searchBox).then(function (clientInfo) {
                    vm.clientInfo = clientInfo;
                    vm.emplSettings.lastClientCode = vm.searchBox;
                });

                terraSellerClientService.getStatistic(vm.searchBox).then(function (clientStatistic) {
                    vm.clientStatistic = clientStatistic;
                });
            }
        }

        function choosePossibleClient (client) {
            vm.progSearchChanged = true;
            vm.searchBox = client.name;
            vm.possibleClients = [];
            vm.searchedClient = client;
            search(client.name);
        };

        $scope.$watch('vm.searchBox', function (val) {

            if(vm.searchType != "byName") {
                return;
            }

            if (vm.progSearchChanged) {
                vm.progSearchChanged = false;
                return;
            }

            if (val === undefined || val === null || val.length < 3) {
                vm.possibleClients = [];
                return;
            }

            vm.clientInfo = null;
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

        function AddClient() {
            var clients = {list: []};

            if(vm.emplSettings.clients) {
                clients = angular.fromJson(vm.emplSettings.clients);
            };

            clients.list.push({name: vm.clientInfo.name, code: vm.clientInfo.code});
            vm.emplSettings.clients = angular.toJson(clients);

            terraSellerSettingsService.save(vm.emplSettings);

            $state.go('app.terraSeller.client');
        };

    }
})();
