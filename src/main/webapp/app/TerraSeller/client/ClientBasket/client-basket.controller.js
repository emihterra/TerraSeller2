(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientBasketController', ClientBasketController);

    ClientBasketController.$inject =
        ['$state', 'ClientBasket', 'Principal', 'terraSellerSettingsService', 'ClientBasketItem', 'terraSellerStockService', 'ClientRoom'];

    function ClientBasketController (
        $state, ClientBasket, Principal, terraSellerSettingsService, ClientBasketItem, terraSellerStockService, ClientRoom) {

        var vm = this;
        vm.clientBaskets = [];
        vm.clientBasket = {};
        vm.basketItems = {};
        vm.newBasket = {};
        vm.clientCode = "";
        vm.emplcode = "";
        vm.error = null;
        vm.success = null;
        vm.selectedItem = null;         // продукт, на котором нажали кнопку Info
        vm.stockData = [];              // Info по продукту - наличие на складах
        vm.stockHeader = ['','','','',''];            // Info по продукту - суммарная информация
        vm.basketSum = 0;
        vm.selectedItemType = {};

        vm.addNewBasket = addNewBasket;
        vm.isBasketActive = isBasketActive;
        vm.selectBasket = selectBasket;
        vm.show2rowName = show2rowName;
        vm.clickInfo = clickInfo;
        vm.checkItemQty = checkItemQty;
        vm.countBasketSum = countBasketSum;
        vm.getTypeStr = getTypeStr;
        vm.countByRoom = countByRoom;
        vm.cancelUseType = cancelUseType;
        vm.applyUseType = applyUseType;

        vm.loadByClient = function() {
            ClientBasket.query({client: vm.clientCode}, function(result) {
                vm.clientBaskets = result;

                if((vm.clientBaskets)&&(vm.clientBaskets.length > 0)) {
                    vm.selectBasket(vm.clientBaskets[0]);
                    vm.countBasketSum();
                }

                vm.error = null;
                vm.success = null;
            });
        };

        Principal.identity().then(function(account) {
            terraSellerSettingsService.get(account.login).then(function(settings){
                if(settings.lastClientCode) {
                    vm.clientCode = settings.lastClientCode;
                    vm.emplcode = settings.emplcode;

                    ClientRoom.query({client: vm.clientCode}, function(result) {
                        vm.clientRooms = result;
                    });

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
          return vm.clientBasket == basketID;
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
            ClientBasketItem.query({idbasket: basket.id}, function(result) {
                vm.basketItems = result;
                vm.countBasketSum();
            });
        };

        function show2rowName (fullName) {
            return fullName.split('|');
        }

        function clickInfo(item){
            vm.selectedItem = item;
            vm.stockData = [];
            vm.stockHeader = ['','','','',''];

            terraSellerStockService.get(vm.emplcode, item.code).then(function (stockInfo){
                vm.stockData = stockInfo;

                terraSellerStockService.getHeader(vm.emplcode).then(function (headerInfo){
                    if(headerInfo) {
                        vm.stockHeader = headerInfo.split(',');
                    }
                });
            }),
                function(err) {
                    //
                }
        }

        function countBasketSum(){
            vm.basketSum = 0;

            var qtyN = 0;

            angular.forEach(vm.basketItems, function(item){
                var qtyN = parseFloat(item.qty);

                if(!qtyN){
                    qtyN = 0;
                };

                vm.basketSum = vm.basketSum + qtyN*item.price;
            });
            vm.basketSum = vm.basketSum.toFixed(2);
        };

        function checkItemQty(item) {
            var qty = "";

            qty = item.qty.toString();
            item.qty = qty.replace(",", ".");

            var qtyN = parseFloat(item.qty);

            if(!qtyN){
                qtyN = 0;
            };

            var newItem = JSON.parse(JSON.stringify(item));

            newItem.qty = qtyN;

            countBasketSum();

            ClientBasketItem.update(newItem);
        };

        function getTypeStr(item){
            var retStr = "тип не опр";

            switch(item.useType) {
                case "1": retStr = "тип: Светлая"; break;
                case "2": retStr = "тип: Темная"; break;
                case "3": retStr = "тип: Пол"; break;
                case "4": retStr = "тип: Мозаика"; break;
                case "5": retStr = "тип: Бордюр нижний"; break;
                case "6": retStr = "тип: Бордюр верхний"; break;
                case "7": retStr = "тип: Декор"; break;
                default: retStr = "тип не опр"; break;
            };

            return retStr;
        };

        function countByRoom(roomId){

            ClientRoom.get({id: roomId}, function(room){
                if(room){

                }
            });
        };

        function cancelUseType(){
            vm.selectedItemType = {};
        };

        function applyUseType() {
            ClientBasketItem.update(vm.selectedItemType);
        };
    }
})();
