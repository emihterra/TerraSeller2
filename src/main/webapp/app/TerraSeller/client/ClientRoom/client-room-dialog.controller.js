(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomDialogController', ClientRoomDialogController);

    ClientRoomDialogController.$inject = ['$state', 'ClientRoom', 'entity', 'Principal', 'terraSellerSettingsService'];

    function ClientRoomDialogController ($state, ClientRoom, entity, Principal, terraSellerSettingsService) {
        var vm = this;

        vm.clientRoom = entity;
        vm.error = null;
        vm.success = null;
        vm.errorNameExists = null;
        vm.toList = toList;

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
    }
})();
