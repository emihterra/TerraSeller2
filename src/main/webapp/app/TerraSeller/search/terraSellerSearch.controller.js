/**
 * Created by emih on 14.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('terraSellerSearchController', terraSellerSearchController);

    terraSellerSearchController.$inject = ['$window', '$scope', 'terraSellerSearchService'];

    function terraSellerSearchController ($window, $scope, terraSellerSearchService) {
        var vm = this;

        vm.searchBox = '';              // модель поисковой строки
        vm.searchResult = [];           // найденный список характеристик номенклатур в коллекции, или по одной номенклатуре
        vm.possibleCollections = [];    // список коллекций, при поиске по части наименования
        vm.collection = '';             // наименование выбранной коллекции (или коллекции, соответствующей выбранной номенклатуре)
        vm.producer = '';               // производитель выбранной коллекции (или коллекции, соответствующей выбранной номенклатуре)
        vm.piclinks = [];               // сыылци на картинки интерьеров выбранной коллекции
        vm.progSearchChanged = false;   // значение в поисковой строке изменено программно
        vm.isCollectionType = false;    // в строке поиска номенклатура или коллекция
        vm.employeeID = '5227';         // код пользователя

        vm.clickSearch = clickSearch;
        vm.show2rowName = show2rowName;
        vm.choosePossibleCollection = choosePossibleCollection;
        vm.shownameProd = shownameProd;

        function clickSearch() {
            vm.searchResult = [];
            search(vm.searchBox);
        };

        function search(itemCode) {

            if(vm.searchBox != itemCode){
                vm.progSearchChanged = true;
                vm.searchBox = itemCode;
            };

            terraSellerSearchService.get(vm.employeeID, itemCode).then(function(searchResult) {
                vm.searchResult = searchResult;

                if((vm.searchResult)&&(vm.searchResult.length > 0)){
                    vm.collection = vm.searchResult[0].itemCollectionID;
                    vm.producer = vm.searchResult[0].itemProducerID;
                    vm.possibleCollections = [];

                    terraSellerSearchService.isCollectionType(itemCode).then(function (value) {
                        vm.isCollectionType = value;
                    });
                }

            });
        }

        function clickProductInfo(item){

        }

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

            terraSellerSearchService.searchCollections(vm.employeeID, val).then(function(collections) {
                vm.possibleCollections = collections;

                // убираем фокус , чтобы скрыть системное меню в мобильнике
                var element = $window.document.getElementById('search-product');
                if(element)
                    element.blur();
            },
                function(err) {
                    vm.possibleCollections = [];
                }
            );


        });

    }
})();
