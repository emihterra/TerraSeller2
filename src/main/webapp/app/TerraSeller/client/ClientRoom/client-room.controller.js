(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomController', ClientRoomController);

    ClientRoomController.$inject = ['$scope', 'ClientRoom', 'Principal', 'terraSellerSettingsService'];

    function ClientRoomController ($scope, ClientRoom, Principal, terraSellerSettingsService) {
        var vm = this;

        vm.clientRooms = [];
        vm.clientCode = 0;

        vm.deleteRoom = deleteRoom;

        vm.loadByClient = function() {
            ClientRoom.query({client: vm.clientCode}, function(result) {
                vm.clientRooms = result;
            });
        };

        Principal.identity().then(function(account) {
            terraSellerSettingsService.get(account.login).then(function(settings){
                if(settings.lastClientCode) {
                    vm.clientCode = settings.lastClientCode;
                    vm.loadByClient();
                }
            });
        });

        function deleteRoom(id){
            ClientRoom.delete(id);
            vm.loadByClient();
        }
    }
})();
