/**
 * Created by emih on 02.06.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientOrderController', ClientOrderController);

    ClientOrderController.$inject =
        ['$state', 'ClientBasket', 'Principal', 'terraSellerSettingsService', 'ClientBasketItem',
            'terraSellerStockService', 'terraSellerSearchService', 'terraSellerOrderService', 'InventLocation'];

    function ClientOrderController (
        $state, ClientBasket, Principal, terraSellerSettingsService, ClientBasketItem,
        terraSellerStockService, terraSellerSearchService, terraSellerOrderService, InventLocation) {

        var vm = this;
        vm.orderItems = {};
        vm.clientCode = "";
        vm.emplcode = "";
        vm.division = "";
        vm.dimension = "";
        vm.selectedItem = null;         // продукт, на котором нажали кнопку Info
        vm.stockData = [];              // Info по продукту - наличие на складах
        vm.stockHeader = ['','','','',''];            // Info по продукту - суммарная информация
        vm.orderSum = 0;
        vm.divisionList = {};
        vm.locations = [];

        vm.show2rowName = show2rowName;
        vm.clickInfo = clickInfo;
        vm.checkItemQty = checkItemQty;
        vm.countOrderSum = countOrderSum;
        vm.getTypeStr = getTypeStr;
        vm.reCount = reCount;
        vm.makeOrder = makeOrder;
        vm.countAnalitics = countAnalitics;

        terraSellerOrderService.getDivisions().then(function(response){
            vm.divisionList = response;

            if(vm.divisionList.length > 0){
                vm.division = vm.divisionList[0].merch_name;
            }
        });

        function loadOrderItems() {
            ClientBasketItem.query({idbasket: "", orderedOnly: "true"}, function(result) {
                vm.orderItems = result;

                angular.forEach(vm.orderItems, function(item){
                    if(item.info) {
                        item.infoJSON = angular.fromJson(item.info);
                    }
                });

                vm.countOrderSum();
            });
        };

        Principal.identity().then(function(account) {
            terraSellerSettingsService.get(account.login).then(function(settings){
                if(settings.lastClientCode) {
                    vm.clientCode = settings.lastClientCode;
                    vm.dimension = settings.dimension;
                    vm.emplcode = settings.emplcode;
                    loadOrderItems();
                }
            });
        });

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

        function countOrderSum(){
            vm.orderSum = 0;

            var qtyN = 0;

            angular.forEach(vm.orderItems, function(item){
                var qtyN = parseFloat(item.qty);

                if(!qtyN){
                    qtyN = 0;
                };

                vm.orderSum = vm.orderSum + qtyN*item.price;
            });
            vm.orderSum = vm.orderSum.toFixed(2);
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

            countOrderSum();

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

        function reCount() {
            angular.forEach(vm.orderItems, function (cartItem) {

                if((!cartItem.recounted)&&(cartItem.unit == 'кв.м.')) {

                    terraSellerSearchService.recountQty(cartItem.code, cartItem.qty).then(function(newQty) {
                        cartItem.qty = newQty;
                        cartItem.recounted = true;

                        ClientBasketItem.update(cartItem);
                    });
                }
            });
        };

        function makeOrder() {
            // Запрос на создание корзины товаров в Axapta
            terraSellerOrderService.createOrderHeader({
                custaccount: vm.clientCode,
                priceGroup: 'РОЗН',
                division: vm.division,
                dimensionnum: vm.dimension,
                emlcode: vm.emplcode
            }).then(function(bsCode){
                var data = [];
                var ids = "";
                var orderNumber = "";

                angular.forEach(vm.orderItems, function (item) {
                    ids = item.combo.split('/');
                    data.push({
                        itemcode: item.code,
                        qty: item.qty.toString(),
                        price: item.price,
                        stock: item.stock,
                        colorID: ids[1],
                        sizeID: ids[0],
                        bs_code: bsCode,
                        status: '10',
                        cID: 'еtest,'
                    });
                });

                terraSellerOrderService.createOrderBody(data).then(function(){
                    $.smallBox({
                        title: "Заказ " + bsCode + " создан успешно!",
                        content: "",
                        color: "#0F5933",
                        iconSmall: "fa fa-thumbs-up bounce animated",
                        timeout: 4000
                    });
//                    terraSellerOrderService.getOrderNumber(bsCode).then(function(orderNumberRes){
//                        orderNumber = orderNumberRes;
//                    });
                },
                function(){
                    $.smallBox({
                        title: "Ошибка создания заказа " + bsCode,
                        content: "",
                        color: "#A90329",
                        iconSmall: "fa fa-ban bounce animated",
                        timeout: 4000
                    });
                });
            });
        };

        function getPriority(stockName){
            angular.forEach(vm.locations, function (location) {
                if(location.code == stockName){
                    return location.priority;
                }
            });
            return 0;
        }

        // Автоматический подбор комбинаций аналитик номенклатуры
        // 1. поиск идет по разрешенным складам, где остаток больший или равный чем требуется , ищем с наименьшей разницей от того сколько надо и сколько есть. Если найдено несколько складов с одинаковым количеством, то берем с большим приоритетом
        // 2. если не нашли, то поиск по складам с меньшим количеством, чем надо, но опять с наименьшей разницей, далее по приоритету
        // 3. если опять не нашли, то делаем запрос значений по умолчанию
        function getStockAndAnaliticsByQuantity(orderItem){
            var stocksMore = [];
            var stocksLess = [];
            var res = {stock: "", analitics: "", delta: -1, priority: 0};
            var priority = 0;

            function CompareRes(stockName, stockDelta, stockQty, stockPriority, stockAnalitics) {
                if((res.delta == -1)||(res.delta > stockDelta)||((res.delta == stockQty)&&(res.priority < stockPriority))){
                    res.stock = stockName;
                    res.analitics = stockAnalitics;
                    res.delta = stockDelta;
                    res.priority = stockPriority;
                    res.stockQty = stockQty;
                }
            }

            terraSellerStockService.getStocksFast(orderItem.code).then(function (stockServiceResponse) {

                angular.forEach(stockServiceResponse, function (item) {

                    priority = getPriority(item.stock);

                    // 1. поиск идет по разрешенным складам, где остаток больший или равный чем требуется , ищем с наименьшей разницей от того сколько надо и сколько есть. Если найдено несколько складов с одинаковым количеством, то берем с большим приоритетом
                    if (item.age >= orderItem.qty) {

                        stocksMore.push({
                            stock: item.stock,
                            analitics: item.analitics,
                            qty: item.age,
                            delta: (item.age - orderItem.qty),
                            priority: priority
                        });

                        CompareRes(item.stock, (item.age - orderItem.qty), item.age, priority, item.analitics);

                    } else {
                        stocksLess.push({
                            stock: item.stock,
                            analitics: item.analitics,
                            qty: item.age,
                            delta: (orderItem.qty - item.age),
                            priority: priority
                        });
                    }
                });

                // 2. если не нашли, то поиск по складам с меньшим количеством, чем надо, но опять с наименьшей разницей, далее по приоритету
                if ((res.stock == "") && (stocksLess.length > 0)) {
                    angular.forEach(stocksLess, function (item) {
                        CompareRes(item.stock, item.delta, item.qty, item.priority, item.analitics);
                    });
                };

                // 3. если опять не нашли, то делаем запрос значений по умолчанию
                if (res.stock == "") {
                    terraSellerStockService.getProdDefaults(orderItem.code).then(function (def) {
                        orderItem.stock = def.stock;
                        orderItem.combo = def.color + '/' + def.size_ton;
                        orderItem.stockQty = 0;
                        ClientBasketItem.update(orderItem);
                    });
                } else {

                    // проверяем на всякий случай на пустую строку и на строку "/" (странно, но в базе бывает три символа)
                    if ((res.analitics == "") || (res.analitics == "/") || (res.analitics.trim() == String.fromCharCode(2, 47, 2))) {
                        res.analitics = "НеОпр/НеОпр";
                    };

                    orderItem.stock = res.stock;
                    orderItem.combo = res.analitics;
                    orderItem.stockQty = res.stockQty;
                    ClientBasketItem.update(orderItem);
                }
            });
        };

        function countAnalitics(){
            InventLocation.query(function(InventLocationResponse) {
                vm.locations = InventLocationResponse;

                angular.forEach(vm.orderItems, function (item) {
                    getStockAndAnaliticsByQuantity(item);
                });
            });
        };

    }
})();
