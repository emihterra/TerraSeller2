(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('InventLocationController', InventLocationController);

    InventLocationController.$inject = [
        'InventLocation', 'InventLocationImport', 'InventLocationUpdateAll', 'terraSellerLocationsService', '$http'];

    function InventLocationController (InventLocation, InventLocationImport, InventLocationUpdateAll, terraSellerLocationsService, $http) {
        var vm = this;
        vm.Locations = [];
        vm.ImportMode = false;

        vm.ImportLocations = ImportLocations;
        vm.save = save;
        vm.cancel = cancel;

        vm.loadLocationsFromTerra = function() {
            terraSellerLocationsService.get().then(function(result) {
                vm.Locations = result;
                vm.ImportMode = true;
            });
        };

        function loadLocations() {
            vm.Locations = [];
            vm.ImportMode = false;
            InventLocation.query(function(result) {
                vm.Locations = result;
            });
        };

        loadLocations();

        function ImportLocations(){
            InventLocation.delete({id: "1", all: true}, function(result) {
                var importLocations = [];

                angular.forEach(vm.Locations, function(location){
                   if(location.checked){
                       importLocations.push({
                           id: null,
                           code: location.code,
                           name: location.name,
                           priority: 0,
                           info: ""
                       });
                   };
                });

                vm.Locations = [];
                vm.ImportMode = false;

                InventLocationImport.save(JSON.stringify(importLocations), function(response) {
                    vm.Locations = response;
                });
            });
        };

        function updatePriorities(){
            var updateLocations = [];

            angular.forEach(vm.Locations, function(location){
                updateLocations.push({
                    id: location.id,
                    code: location.code,
                    name: location.name,
                    priority: location.priority,
                    info: location.info
                });
            });

            vm.Locations = [];

            InventLocationUpdateAll.save(JSON.stringify(updateLocations), function(response) {
                vm.Locations = response;
            });
        };

        function save(){
            if(vm.ImportMode){
                ImportLocations();
            } else {
                updatePriorities();
            }
        };

        function cancel(){
          loadLocations();
        };
    }
})();
