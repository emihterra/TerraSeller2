(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomDialogController', ClientRoomDialogController);

    ClientRoomDialogController.$inject = ['$state', 'ClientRoom', 'entity'];

    function ClientRoomDialogController ($state, ClientRoom, entity) {
        var vm = this;

        vm.clientRoom = entity;
        vm.error = null;
        vm.success = null;
        vm.errorNameExists = null;
        vm.toList = toList;

        vm.load = function(id) {
            ClientRoom.get({id : id}, function(result) {
                vm.clientRoom = result;
            });
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
                console.log('ClientRoom.update');
                ClientRoom.update(vm.clientRoom, onSaveSuccess, onSaveError);
            } else {
                console.log('ClientRoom.save');
                ClientRoom.save(vm.clientRoom, onSaveSuccess, onSaveError);
            }
        };

        function toList() {
            $state.go('app.terraSeller.client-room');
        };
    }
})();
