(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomDialogController', ClientRoomDialogController);

    ClientRoomDialogController.$inject = ['$state', 'ClientRoom', 'entity', 'Principal', 'terraSellerSettingsService', 'ClientRoomItem'];

    function ClientRoomDialogController ($state, ClientRoom, entity, Principal, terraSellerSettingsService, ClientRoomItem) {
        var vm = this;

        vm.clientRoom = entity;
        vm.error = null;
        vm.success = null;
        vm.errorNameExists = null;
        vm.newItemEditing = false;
        vm.clientRoomItems = [];
        vm.itemForDeletion = null;

        vm.newItem = newItem;
        vm.cancelItemEditing = cancelItemEditing;
        vm.saveItemEditing = saveItemEditing;
        vm.confirmItemDelete = confirmItemDelete;
        vm.toList = toList;


        function loadItems() {
            if (vm.clientRoom.id !== null) {
                ClientRoomItem.query({id_room: vm.clientRoom.id}, function (result) {
                    vm.clientRoomItems = result;
                });
            }
        };

        loadItems();

        Principal.identity().then(function(account) {
            terraSellerSettingsService.get(account.login).then(function(settings){
                if(settings.lastClientCode) {
                    vm.clientRoom.client = settings.lastClientCode;
                }
            });
        });

        var onSaveSuccess = function (result) {
            vm.error = null;
            vm.success = 'OK';
        };

        var onSaveError = function () {
            vm.success = null;
            vm.error = 'ERROR';
        };

        vm.save = function () {
            if (vm.clientRoom.id !== null) {
                ClientRoom.update(vm.clientRoom, onSaveSuccess, onSaveError);
            } else {
                ClientRoom.save(vm.clientRoom, onSaveSuccess, onSaveError);
            }
        };

        function toList() {
            $state.go('app.terraSeller.client-room');
        };

        function newItem() {
            vm.newItemEditing = true;
        };

        function cancelItemEditing() {
            vm.newItemEditing = false;
        };

        function saveItemEditing() {
            vm.newItemEditing = false;
            vm.clientRoomItem.id_client_room = vm.clientRoom.id;
            ClientRoomItem.save(vm.clientRoomItem, function() {
                loadItems();
            });
        };

        function confirmItemDelete(id) {
            ClientRoomItem.delete({id: id}, function() {
                loadItems();
            });
        };

        function clearItemDeletion() {
            vm.itemForDeletion = null;
        };

    }
})();
