(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientBasketController', ClientBasketController);

    ClientBasketController.$inject = ['$state', 'ClientBasket', 'Principal', 'terraSellerSettingsService'];

    function ClientBasketController ($state, ClientBasket, Principal, terraSellerSettingsService) {
        var vm = this;
        vm.clientBaskets = [];
        vm.clientBasket = {};
        vm.newBasket = {};
        vm.clientCode = "";
        vm.emplcode = "";
        vm.error = null;
        vm.success = null;

        vm.addNewBasket = addNewBasket;
        vm.isBasketActive = isBasketActive;
        vm.selectBasket = selectBasket;

        vm.loadByClient = function() {
            ClientBasket.query({client: vm.clientCode}, function(result) {
                vm.clientBaskets = result;
                vm.error = null;
                vm.success = null;
            });
        };

        Principal.identity().then(function(account) {
            terraSellerSettingsService.get(account.login).then(function(settings){
                if(settings.lastClientCode) {
                    vm.clientCode = settings.lastClientCode;
                    vm.emplcode = settings.emplcode;
                    vm.loadByClient();
                }
            });
        });

        var onSaveSuccess = function (result) {
            vm.error = null;
            vm.success = 'OK';
            vm.newBasket = {};
            vm.loadByClient();
        };

        var onSaveError = function () {
            vm.success = null;
            vm.newBasket = {};
            vm.error = 'ERROR';
        };

        function isBasketActive(basketID) {
          return false;
        };

        function addNewBasket() {
            vm.newBasket.client = vm.clientCode;
            vm.newBasket.emplcode = vm.emplcode;
            vm.newBasket.idClientRoom = "";
            vm.newBasket.info = "";
            ClientBasket.save(vm.newBasket, onSaveSuccess, onSaveError);
        };
        
        function selectBasket(basket) {
            vm.clientBasket = basket;
        };
    }
})();
