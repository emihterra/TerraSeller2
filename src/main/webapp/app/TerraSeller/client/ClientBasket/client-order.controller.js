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
            'terraSellerStockService', 'terraSellerSearchService'];

    function ClientOrderController (
        $state, ClientBasket, Principal, terraSellerSettingsService, ClientBasketItem,
        terraSellerStockService, terraSellerSearchService) {

        var vm = this;
        vm.orderItems = {};
        vm.clientCode = "";
        vm.emplcode = "";
        vm.selectedItem = null;         // продукт, на котором нажали кнопку Info
        vm.stockData = [];              // Info по продукту - наличие на складах
        vm.stockHeader = ['','','','',''];            // Info по продукту - суммарная информация
        vm.orderSum = 0;

        vm.show2rowName = show2rowName;
        vm.clickInfo = clickInfo;
        vm.checkItemQty = checkItemQty;
        vm.countOrderSum = countOrderSum;
        vm.getTypeStr = getTypeStr;
        vm.reCount = reCount;
        vm.makeOrder = makeOrder;

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
            if (!$scope.clientParams.client.code) {
                toastr.warning('Укажите код клиента', 'Ошибка валидации');
                $scope.clientParams.error = true;
                //var clientInput = document.querySelector("#clientName");
                //clientInput.scrollIntoView(true);
                //window.scrollBy(0, -35);
                return;
            }
            // Запрос на создание корзины товаров в Axapta
            $http({
                url: UrlApi.baseAPI + UrlApi.endPoint.basket,
                method: 'POST',
                data: {
                    custaccount: $scope.clientParams.client.code,
                    priceGroup: 'РОЗН',
                    division: $scope.clientParams.division.merch_name,
                    dimensionnum: $scope.dimension,
                    emlcode: $scope.employee.emplcode
                },
                transformResponse: undefined
            })
                .success(function (result) {

                    var bsCode = result.trim();
                    var bsOrderNumber = '';

                    toastr.success('Код вашей корзины - ' + bsCode, 'Корзина успешно сохранена');
                    var data = [];
                    angular.forEach($scope.finalBasketItems, function (item) {
                        var ids = item.data.combo.split('/');
                        data.push({
                            itemcode: item.itemcode,
                            qty: item.qty.toString(),
                            price: item.price,
                            stock: item.data.stock,
                            colorID: ids[1],
                            sizeID: ids[0],
                            bs_code: bsCode,
                            status: '10',
                            cID: 'еtest,'
                        });
                    });

                    // Запрос на создание позиций товаров корзины в Axapta
                    $http({
                        url: UrlApi.baseAPI + UrlApi.endPoint.basketLines,
                        method: 'POST',
                        data: data,
                        transformResponse: undefined
                    })
                        .success(function (result) {

                            // Получаем номер заказа по коду корзины в Axapta
                            $http({
                                url: UrlApi.baseAPI + UrlApi.endPoint.orderNumber + bsCode,
                                method: 'GET',
                                transformResponse: undefined
                            })
                                .success(function (result) {
                                    bsOrderNumber = result.trim();

                                    var dataPreorder = [];

                                    dataPreorder.push({
                                        id: '',
                                        clientCode: $scope.clientParams.client.code,
                                        clientName: $scope.clientParams.client.name,
                                        basketCode: bsCode,
                                        orderNumber: bsOrderNumber,
                                        employeeID: $scope.employeeID
                                    });

                                    // Скидываем инфу по корзине в Mongo
                                    $http({
                                        url: UrlApi.mongoURL + UrlApi.endPoint.addPreorder,
                                        method: 'POST',
                                        data: dataPreorder,
                                        transformResponse: undefined
                                    }).success(function (result) {
                                    });

                                    angular.forEach($scope.finalBasketItems, function (item) {
                                        $scope.deleteFBItem(item);
                                    });
                                });
                        });
                })
                .error(function () {
                    toastr.error('Корзина НЕ сохранена. Попробуйте ещё раз', 'Ошибка сохранения');
                });
        }


    }
})();

