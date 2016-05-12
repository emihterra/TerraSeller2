(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomController', ClientRoomController);

    ClientRoomController.$inject = ['$scope', 'ClientRoom'];

    function ClientRoomController ($scope, ClientRoom) {
        var vm = this;

        vm.clientRooms = [];

        vm.deleteRoom = deleteRoom;

        vm.loadAll = function() {
            ClientRoom.query(function(result) {
                vm.clientRooms = result;
            });
        };

        vm.loadAll();

        function deleteRoom(id){
            ClientRoom.delete(id);
            vm.loadAll();
        }
    }
})();
