(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomItemController', ClientRoomItemController);

    ClientRoomItemController.$inject = ['$scope', '$state', 'ClientRoomItem'];

    function ClientRoomItemController ($scope, $state, ClientRoomItem) {
        var vm = this;
        vm.clientRoomItems = [];
        vm.loadAll = function() {
            ClientRoomItem.query(function(result) {
                vm.clientRoomItems = result;
            });
        };

        vm.loadAll();
        
    }
})();
