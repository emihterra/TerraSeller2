/**
 * Created by emih on 14.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('terraSellerSearchController', terraSellerSearchController);

    terraSellerSearchController.$inject =
        ['$window', '$scope', 'terraSellerSearchService', 'terraSellerStockService', 'Principal', 'ClientBasket',
            'ClientBasketItem', 'terraSellerSettingsService'];

    function terraSellerSearchController (
        $window, $scope, terraSellerSearchService, terraSellerStockService, Principal, ClientBasket,
            ClientBasketItem, terraSellerSettingsService) {

        var vm = this;

        vm.searchBox = '';              // модель поисковой строки
        vm.searchResult = [];           // найденный список характеристик номенклатур в коллекции, или по одной номенклатуре
        vm.possibleCollections = [];    // список коллекций, при поиске по части наименования
        vm.collection = '';             // наименование выбранной коллекции (или коллекции, соответствующей выбранной номенклатуре)
        vm.producer = '';               // производитель выбранной коллекции (или коллекции, соответствующей выбранной номенклатуре)
        vm.piclinks = [];               // сыылци на картинки интерьеров выбранной коллекции
        vm.progSearchChanged = false;   // значение в поисковой строке изменено программно
        vm.isCollectionType = false;    // в строке поиска номенклатура или коллекция
        vm.selectedItem = null;         // продукт, на котором нажали кнопку Info
        vm.selectedImgUrl = '';         // ссылка на выбранную картинку
        vm.selectedImgTitle = '';       // заголовок для выбранной картинки
        vm.selectedImgIndex = 0;       // индекс выбранной картинки
        vm.stockData = [];              // Info по продукту - наличие на складах
        vm.stockHeader = ['','','','',''];            // Info по продукту - суммарная информация
        vm.checkAllState = false;       // выбрать все
        vm.isChecked = false;           // есть выбранные продукты
        vm.employeeID = '';             // код продавца
        vm.employeeDimension = '';
        vm.clientBaskets = [];          // список корзин

        vm.clickSearch = clickSearch;
        vm.show2rowName = show2rowName;
        vm.choosePossibleCollection = choosePossibleCollection;
        vm.shownameProd = shownameProd;
        vm.clickInfo = clickInfo;
        vm.clickImg = clickImg;
        vm.clickColPic = clickColPic;
        vm.searchCollection = searchCollection;
        vm.onKeyUp = onKeyUp;
        vm.checkAll = checkAll;
        vm.checkedStateChanged = checkedStateChanged;
        vm.sendToBasket = sendToBasket;

        function clickSearch() {
            vm.searchResult = [];
            vm.isChecked = false;
            search(vm.searchBox);
        };

        function search(itemCode) {

            if(vm.searchBox != itemCode){
                vm.progSearchChanged = true;
                vm.searchBox = itemCode;
            };

            terraSellerSearchService.get(vm.employeeID, itemCode).then(function(searchResult) {
                vm.searchResult = searchResult;
                vm.isChecked = false;

                if((vm.searchResult)&&(vm.searchResult.length > 0)){

                    vm.collection = vm.searchResult[0].itemCollectionID;
                    vm.producer = vm.searchResult[0].itemProducerID;
                    vm.possibleCollections = [];

                    terraSellerSearchService.isCollectionType(itemCode).then(function (value) {
                        vm.isCollectionType = value;
                    });

                    terraSellerSearchService.getPicLinks(vm.collection, vm.producer).then(function (value) {
                        vm.piclinks = value;

                        if(vm.piclinks&&(vm.piclinks.length > 0)){
                            vm.selectedImgUrl = vm.piclinks[0].bigUrl;
                            vm.selectedImgIndex = vm.piclinks[0].idx;
                        }
                    });
                }

            });
        }

        Principal.identity().then(function(account) {
            terraSellerSettingsService.get(account.login).then(function(settings){
                if(settings.lastClientCode) {
                    vm.clientCode = settings.lastClientCode;
                    vm.employeeID = settings.emplcode;
                    vm.employeeDimension = settings.dimension;

                    ClientBasket.query({client: vm.clientCode, deleted: false}, function(result) {
                        vm.clientBaskets = result;
                    });
                }
            });
        });

        function clickInfo(item){
          vm.selectedItem = item;
          vm.stockData = [];
          vm.stockHeader = ['','','','',''];

          terraSellerStockService.get(vm.employeeID, item.sidUser).then(function (stockInfo){
              vm.stockData = stockInfo;

              terraSellerStockService.getHeader(vm.employeeID).then(function (headerInfo){
                  if(headerInfo) {
                      vm.stockHeader = headerInfo.split(',');
                  }
              });
          }),
            function(err) {
                //
            }
        }

        function clickImg(item){
            vm.selectedImgIndex = 0;
            vm.selectedImgUrl = item.imgUrl;
            vm.selectedImgTitle = item.sidUser + ' - ' + item.name;
        }

        function clickColPic(item){

            vm.selectedImgIndex = item.idx;
            vm.selectedImgUrl = item.bigUrl;

            angular.forEach(vm.piclinks, function(pic){
               pic.active = false;
            });

            item.active = true;

            vm.selectedImgTitle = vm.collection + ' - ' + vm.producer;
        }

        function searchCollection(){
            if (vm.searchResult[0].itemCollectionID) {
                vm.progSearchChanged = true;
                vm.searchBox = vm.searchResult[0].itemCollectionID + ' - ' + vm.searchResult[0].itemProducerID;
                search(vm.searchResult[0].itemCollectionID + ' - ' + vm.searchResult[0].itemProducerID);
            }
        };

        function choosePossibleCollection (collection) {
            vm.progSearchChanged = true;
            vm.searchBox = vm.shownameProd(collection.name);
            vm.possibleCollections = [];
            search(vm.searchBox);
        };

        function shownameProd (fullName) {
            var arStr = fullName.split(';');
            return arStr[0] + ' - ' + arStr[1];
        }

        function show2rowName (fullName) {
            return fullName.split('|');
        }

        function checkAll(){
            angular.forEach(vm.searchResult, function (item) {
                item.checked = vm.checkAllState;
            });
            checkedStateChanged();
        };

        function checkedStateChanged() {
            var ret = false;

            angular.forEach(vm.searchResult, function (item) {
                if (item.checked) {
                    ret = true;
                }
            });
            vm.isChecked = ret;
        }

        $scope.$watch('vm.searchBox', function (val) {
            if (vm.progSearchChanged) {
                vm.progSearchChanged = false;
                return;
            }

            if (val === undefined || val === null || val.length < 3) {
                vm.possibleCollections = [];
                return;
            }

            vm.searchResult = [];
            vm.piclinks = [];
            vm.checkAllState = false;
            vm.isChecked = false;

            terraSellerSearchService.searchCollections(vm.employeeID, val).then(function(collections) {
                vm.possibleCollections = collections;

                // убираем фокус , чтобы скрыть системное меню в мобильнике
                if(vm.possibleCollections&&(vm.possibleCollections.length > 0)) {
                    var element = $window.document.getElementById('search-product');
                    if (element)
                        element.blur();
                }
            },
                function(err) {
                    vm.possibleCollections = [];
                }
            );
        });

        function onKeyUp ($event) {
            var onKeyUpResult = ($window.event ? $event.keyCode : $event.which);
            if (onKeyUpResult === 13) {
                clickSearch();
            }
        };

        function sendToBasket (basketID) {

            var basketItem = {};

            angular.forEach(vm.searchResult, function (item) {
                if (item.checked) {

                    basketItem = {
                        idClientBasket: basketID,
                        code: item.sidUser,
                        name: item.name,
                        qty: 0,
                        price: item.price,
                        imglink: item.imgUrl,
                        unit: item.unit,
                        itemsize: item.itemsize,
                        reserv: 0,
                        part: "",
                        combo: "",
                        stock: "",
                        useType: "",
                        info: ""
                    };

                    ClientBasketItem.save(basketItem);
                }
            });
        }
    }
})();
