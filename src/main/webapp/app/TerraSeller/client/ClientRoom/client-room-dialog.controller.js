(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomDialogController', ClientRoomDialogController);

    ClientRoomDialogController.$inject = ['$state', 'ClientRoom', 'entity', 'Principal', 'terraSellerSettingsService', 'ClientRoomItem'];

    function ClientRoomDialogController ($state, ClientRoom, entity, Principal, terraSellerSettingsService, ClientRoomItem) {
        var vm = this;

        vm.clientRoom = {};
        vm.error = null;
        vm.success = null;
        vm.errorNameExists = null;
        vm.newItemEditing = false;
        vm.clientRoomItems = [];
        vm.itemForDeletion = null;
        vm.clientRoomItem = {
            id: null,
            idClientRoom: '',
            name: '',
            item_type: '1',
            i_width: 0,
            i_height: 0,
            i_top: 0
        };

        vm.newItem = newItem;
        vm.cancelItemEditing = cancelItemEditing;
        vm.saveItemEditing = saveItemEditing;
        vm.confirmItemDelete = confirmItemDelete;
        vm.toList = toList;
        vm.getItemTypeStr = getItemTypeStr;

        function loadItems() {
            if ((vm.clientRoom.id)&&(vm.clientRoom.id !== null)) {
                ClientRoomItem.query({roomId: vm.clientRoom.id}, function (result) {
                    vm.clientRoomItems = result;
                });
            }
        };

        function getClientId() {
            Principal.identity().then(function (account) {
                terraSellerSettingsService.get(account.login).then(function (settings) {
                    if (settings.lastClientCode) {
                        vm.clientRoom.client = settings.lastClientCode;
                    }
                });
            });
        };

        if(entity.$promise) {
            entity.$promise.then(function (clientRoom) {
                vm.clientRoom = clientRoom;
                getClientId();
                loadItems();
            });
        } else {
            getClientId();
        };

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
            vm.clientRoomItem.idClientRoom = vm.clientRoom.id;
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

        function getItemTypeStr(itemType){
            var retStr = "";

            switch(itemType){
              case 1:
                  retStr = "Окно";
                  break;
              case 2:
                  retStr = "Дверь";
                  break;
                default:
                  retStr = "";
            }

            return retStr;
        };

    }
})();
