(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomController', ClientRoomController);

    ClientRoomController.$inject = ['$scope', '$state', 'ClientRoom'];

    function ClientRoomController ($scope, $state, ClientRoom) {
        var vm = this;
        vm.clientRooms = [];
        vm.loadAll = function() {
            ClientRoom.query(function(result) {
                vm.clientRooms = result;
            });
        };

        vm.loadAll();
        
    }
})();
