/**
 * Created by emih on 22.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('terraSellerClientController', terraSellerClientController);

    terraSellerClientController.$inject = ['Principal', 'terraSellerClientService', 'terraSellerSettingsService'];

    function terraSellerClientController (Principal, terraSellerClientService, terraSellerSettingsService) {
        var vm = this;

        vm.employeeID = '';             // код продавца
        vm.clientStatistic = '';        // статистика по клиенту
        vm.clientInfo = {};             // информация по клиенту
        vm.emplSettings = terraSellerSettingsService.getDefault(); // настройки продавца
        vm.clientsList = {list: []};

        vm.selectClient = selectClient;
        vm.deleteClient = deleteClient;

        function LoadClientInfo(clientCode){
            terraSellerClientService.getInfo(vm.emplSettings.lastClientCode).then(function(clientInfo) {
                vm.clientInfo = clientInfo;
            });
            terraSellerClientService.getStatistic(vm.emplSettings.lastClientCode).then(function(clientStatistic) {
                vm.clientStatistic = clientStatistic;
            });
        };

        Principal.identity().then(function(account) {
            vm.employeeID = account.emplcode;
            terraSellerSettingsService.get(account.login).then(function(settings){
                vm.emplSettings = settings;

                if(vm.emplSettings.clients) {
                    vm.clientsList = angular.fromJson(vm.emplSettings.clients);
                };

                if ((vm.emplSettings.lastClientCode)/*&&(!vm.emplSettings.useDefaultClient)*/){
                    LoadClientInfo(vm.emplSettings.lastClientCode);
                }
            });
        });

        function selectClient() {
            vm.clientStatistic = '';        // статистика по клиенту
            vm.clientInfo = {};             // информация по клиенту
            LoadClientInfo(vm.emplSettings.lastClientCode);
            terraSellerSettingsService.save(vm.emplSettings);
        }

        function deleteClient(){
            var i = 0;
            vm.clientStatistic = '';        // статистика по клиенту
            vm.clientInfo = {};             // информация по клиенту
            if(vm.clientsList&&vm.clientsList.list){
                angular.forEach(vm.clientsList.list, function(item){
                    if(item.code==vm.emplSettings.lastClientCode){
                        vm.clientsList.list.splice(i);
                    };
                    i++;
                });
                vm.emplSettings.lastClientCode = "";
                if(vm.clientsList&&vm.clientsList.list&&(vm.clientsList.list.length > 0)){
                    vm.emplSettings.lastClientCode = vm.clientsList.list[0].code;
                    vm.emplSettings.clients = angular.toJson(vm.clientsList);
                    terraSellerSettingsService.save(vm.emplSettings);
                    LoadClientInfo(vm.emplSettings.lastClientCode);
                }
            };
        };

    }
})();

