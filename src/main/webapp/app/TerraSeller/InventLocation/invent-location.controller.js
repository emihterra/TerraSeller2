(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('InventLocationController', InventLocationController);

    InventLocationController.$inject = ['InventLocation', 'terraSellerLocationsService'];

    function InventLocationController (InventLocation, terraSellerLocationsService) {
        var vm = this;
        vm.Locations = [];

        vm.loadLocations = function() {
            terraSellerLocationsService.get().then(function(result) {
                vm.Locations = result;
            });
        };

        vm.loadLocations();

    }
})();
