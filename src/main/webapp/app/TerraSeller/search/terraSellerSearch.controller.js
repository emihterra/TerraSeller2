/**
 * Created by emih on 14.04.2016.
 */

(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('terraSellerSearchController', terraSellerSearchController);

    terraSellerSearchController.$inject = ['terraSellerSearchService'];

    function terraSellerSearchController (terraSellerSearchService) {
        var vm = this;

        vm.searchBox = '';
        vm.clickSearch = clickSearch;
        vm.searchResult = [];
        vm.collection = '';

        function clickSearch() {
            search(vm.searchBox);
        };

        function search(itemCode) {
            terraSellerSearchService.get('5227', itemCode).then(function(searchResult) {
                vm.searchResult = searchResult;

                if(vm.searchResult.length > 0){
                    vm.collection = vm.searchResult[0].itemCollectionID;
                }
            });
        }

    }
})();
