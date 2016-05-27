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
        vm.calcSum = 0;

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
        vm.cancelDelBasket = cancelDelBasket;
        vm.delBasket = delBasket;

        vm.loadByClient = function() {
            ClientBasket.query({client: vm.clientCode, deleted: false}, function(result) {
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
            vm.newBasket.deleted = false;
            vm.newBasket.idClientRoom = "";
            vm.newBasket.info = "";
            ClientBasket.save(vm.newBasket, onSaveSuccess, onSaveError);
        };

        function selectBasket(basket) {
            vm.clientBasket = basket;
            ClientBasketItem.query({idbasket: basket.id}, function(result) {
                vm.basketItems = result;

                angular.forEach(vm.basketItems, function(item){
                   if(item.info) {
                       item.infoJSON = angular.fromJson(item.info);
                   }
                });

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
            vm.updateCalcSum();
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

        function getSize(item) {
            var itemWidth = 0;
            var itemHeight = 0;
            var tmpAr = [];
            var tmpStr = "";

            tmpAr = item.name.split('|');
            if (tmpAr.length >= 2) {
                tmpStr = tmpAr[1];
                tmpAr = tmpStr.split('x');
                if (tmpAr.length >= 2) {
                    itemWidth = parseFloat(tmpAr[0]);
                    if(!itemWidth){itemWidth = 0;};
                    itemHeight = parseFloat(tmpAr[1]);
                    if(!itemHeight){itemHeight = 0;};
                };
            };

            return {width: itemWidth, height: itemHeight};
        };

        vm.updateCalcSum = function() {
            vm.calcSum = 0;
            var tmpPrice = 0;
            angular.forEach(vm.basketItems, function (item) {
                if ((item.infoJSON) && (item.infoJSON.newprice)) {
                    tmpPrice = tmpPrice + parseFloat(item.infoJSON.newprice);
                }
            });
            vm.calcSum = tmpPrice.toFixed(2);
        }

        function countByRoom(roomId){

            var santechHeight = 0.9;
            var itemWidth = 0;
            var itemHeight = 0;
            var tmpSize = {};
            var darkRows = 0;
            var darkItems = 0;
            var darkItemsHeight = 0;
            var darkSquare = 0;
            var lightRows = 0;
            var lightItems = 0;
            var lightSquare = 0;
            var roomPerimetr = 0;
            var BottomBorderHeight = 0;
            var TopBorderHeight = 0;
            var newprice = 0;

            ClientRoom.get({id: roomId}, function(room){
                if(room){

                    vm.clientBasket.idClientRoom = room.name;
                    ClientBasket.update(vm.clientBasket);

                    roomPerimetr = (room.r_width + room.r_length)*2;

                    angular.forEach(vm.basketItems, function(item) {
                        item.info = "";
                        item.infoJSON = {
                            rows: 0,
                            items: 0,
                            square: 0,
                            newprice: 0
                        };
                    });

                    angular.forEach(vm.basketItems, function(item) {
                        if(item.useType == "5"){ // Бордюр нижний
                            tmpSize = getSize(item);
                            BottomBorderHeight = itmpSize.height;

                            item.qtycalc = roomPerimetr/tmpSize.width;
                            item.qtycalc = Math.round(item.qtycalc);
                            newprice = item.price*item.qtycalc;
                            newprice = newprice.toFixed(2);
                            item.info = JSON.stringify({
                                rows: 0,
                                items: item.qtycalc,
                                square: 0,
                                newprice: newprice
                            });
                            item.infoJSON = angular.fromJson(item.info);
                            ClientBasketItem.update(item);
                        }
                    });

                    angular.forEach(vm.basketItems, function(item) {
                        if(item.useType == "6"){ // Бордюр верхний
                            tmpSize = getSize(item);
                            BottomBorderHeight = itmpSize.height;

                            item.qtycalc = roomPerimetr/tmpSize.width;
                            item.qtycalc = Math.round(item.qtycalc);
                            newprice = item.price*item.qtycalc;
                            newprice = newprice.toFixed(2);
                            item.info = JSON.stringify({
                                rows: 0,
                                items: item.qtycalc,
                                square: 0,
                                newprice: newprice
                            });
                            item.infoJSON = angular.fromJson(item.info);
                            ClientBasketItem.update(item);
                        }
                    });

                    itemWidth = 0;
                    itemHeight = 0;
                    angular.forEach(vm.basketItems, function(item){

                        if(item.useType == "2"){ // Темная
                            tmpSize = getSize(item);
                            itemWidth = tmpSize.width;
                            itemHeight = tmpSize.height;

                            darkRows = santechHeight/itemHeight;
                            darkRows = darkRows.toFixed(2);
                            darkItems = (roomPerimetr/itemWidth)*darkRows;
                            darkItems = darkItems.toFixed(2);
                            darkSquare = darkItems*itemWidth*itemHeight/10000;
                            darkSquare = darkSquare.toFixed(2);
                            darkItemsHeight = itemHeight*darkRows;
                            item.qtycalc = darkSquare;
                            newprice = item.price*item.qtycalc;
                            newprice = newprice.toFixed(2);

                            item.info = JSON.stringify({
                                rows:darkRows,
                                items: darkItems,
                                square: darkSquare,
                                newprice: newprice
                            });
                            item.infoJSON = angular.fromJson(item.info);

                            ClientBasketItem.update(item);

                        }
                    });

                    itemWidth = 0;
                    itemHeight = 0;
                    angular.forEach(vm.basketItems, function(item) {
                        if(item.useType == "1"){ // Светлая
                            tmpSize = getSize(item);
                            itemWidth = tmpSize.width;
                            itemHeight = tmpSize.height;

                            lightRows = (room.r_height - darkItemsHeight - BottomBorderHeight - TopBorderHeight)/itemHeight;
                            lightRows = lightRows.toFixed(2);
                            lightItems = (roomPerimetr/itemWidth)*lightRows;
                            lightItems = lightItems.toFixed(2);
                            lightSquare = lightItems*itemWidth*itemHeight/10000;
                            lightSquare = lightSquare.toFixed(2);

                            item.qtycalc = lightSquare;
                            newprice = item.price*item.qtycalc;
                            newprice = newprice.toFixed(2);

                            item.info = JSON.stringify({
                                rows: lightRows,
                                items: lightItems,
                                square: lightSquare,
                                newprice: newprice
                            });
                            item.infoJSON = angular.fromJson(item.info);

                            ClientBasketItem.update(item);
                        }
                    });

                    angular.forEach(vm.basketItems, function(item) {
                        if(item.useType == "3"){ // Пол
                            item.qtycalc = room.r_width*room.r_length/10000;
                            item.qtycalc = item.qtycalc.toFixed(2);
                            newprice = item.price*item.qtycalc;
                            newprice = newprice.toFixed(2);

                            item.info = JSON.stringify({
                                rows: 0,
                                items: 0,
                                square: item.qtycalc,
                                newprice: newprice
                            });
                            item.infoJSON = angular.fromJson(item.info);

                            ClientBasketItem.update(item);
                        }
                    });

                    vm.updateCalcSum();
                }
            });
        };

        function cancelUseType(){
            vm.selectedItemType = {};
        };

        function applyUseType() {
            ClientBasketItem.update(vm.selectedItemType);
        };

        function cancelDelBasket() {
            vm.clientBasket.deleted = false;
            vm.clientBasket.infoJSON.delReason = 0;
            vm.clientBasket.infoJSON.delCustom = "";
        };

        function delBasket() {
            vm.clientBasket.deleted = true;
            vm.clientBasket.info = JSON.stringify(vm.clientBasket.infoJSON);
            ClientBasket.update(vm.clientBasket, function(){
                vm.clientBasket = {};
                vm.basketItems = {};
                vm.basketSum = 0;
                vm.selectedItemType = {};
                vm.calcSum = 0;
                vm.loadByClient();
            });
        };

    }
})();
